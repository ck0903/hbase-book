package com.hzgosun.hbase2es;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// 存在的问题: 现在没有在ES 上生成InFo
// 以上问题已经解决
// 后续问题，过滤掉不需要添加到Es 中的字段，又或者把图片单独从表中提取出来，这个不现实。
public class HBase2EsObserver  extends BaseRegionObserver{

    private static Logger logger = Logger.getLogger(BaseRegionObserver.class);

    //  从外部获取Es 集群的信息
    private static void readConfiguration(CoprocessorEnvironment env){
        Configuration conf = env.getConfiguration();  // 集群信息
        EsClientUtils.clusterName = conf.get("es_cluster");  // es集群名字
        EsClientUtils.nodeHost = conf.get("es_host");    // es 列表
        EsClientUtils.nodePort = conf.getInt("es_port", 9300);  // es的端口
        EsClientUtils.indexName = conf.get("es_index");  // 索引
        EsClientUtils.typeName = conf.get("es_type");       // 类型


        logger.info("============================================================================================");
        logger.info("=============================================================================================");
        logger.info("the es cluster info :===================== cluser_name " + EsClientUtils.clusterName
                +  ", cluster_hosts: , " + EsClientUtils.nodeHost + ", cluster_port: "
                + EsClientUtils.nodePort + ",index: " + EsClientUtils.indexName +
                ",type: " + EsClientUtils.typeName  + "===========================");
        logger.info("=================================================================================================");
        logger.info("==============================================================================================");
    }

    @Override
    public void start(CoprocessorEnvironment env) throws IOException {
        readConfiguration(env);       // 读取Es 配置信息
        EsClientUtils.initEsClient();  // 初始化Es 客户端
    }

    @Override
    public void stop(CoprocessorEnvironment e) throws IOException {
        EsClientUtils.clostEsClient();  // 关闭Es 客户端
    }

    // 添加或者修改时候的自动触发的操作，
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability)
            throws IOException {
//        System.out.println("------------------something triggred when afere put-------------------");
        logger.info("=================================================================================================");
        logger.info("==============================================================================================");
        logger.info("==================================something triggred when afere put================================");
        String indexId = new String(put.getRow());   // 通过Put 对象获取rowkey 作为id，
        NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
        Map<String, Object> infoJson = new HashMap<String, Object>();
//        Map<String, Object> json = new HashMap<String, Object>();
        for (Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()) {
            for (Cell cell : entry.getValue()) {
                String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                logger.info("==========================属性========================================");
                logger.info("=============key: " + key + "  value: " + value);
                infoJson.put(key, value);
            }
        }
        ElasticSearchBulkOperator.addUpdateBuilderToBulk(EsClientUtils.client.prepareUpdate(EsClientUtils.indexName,
                EsClientUtils.typeName, indexId).setDocAsUpsert(true).setDoc(infoJson));
    }

    @Override
    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit, Durability durability)
            throws IOException {
        logger.info("=================================================================================================");
        logger.info("==============================================================================================");
        logger.info("==================================something triggred when afere DELETE================================");
        String indexId = new String(delete.getRow());
        logger.info("===============================inde: " + indexId + " ============================================");
        try {
            //ElasticSearchBulkOperator.addDeleteBuilderToBulk(EsClientUtils.client.prepareDelete(EsClientUtils.indexName, EsClientUtils.typeName, indexId));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class EsClientUtils{
    public static String clusterName;
    public static String nodeHost;
    public static int nodePort;
    public static String indexName;
    public static String typeName;
    public static Client client;

    public static void initEsClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", EsClientUtils.clusterName).build();
        client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(
                InetAddress.getByName(EsClientUtils.nodeHost), EsClientUtils.nodePort));
    }

    public static void clostEsClient(){
        EsClientUtils.client.close();
    }
}


class ElasticSearchBulkOperator {
    private static final int MAX_BULK_COUNT = 10000;
    private static BulkRequestBuilder bulkRequestBuilder = null;

    private static final Lock commitLock = new ReentrantLock();

    private static ScheduledExecutorService scheduledExecutorService = null;

    static {
        // init es bulkRequestBuilder
        bulkRequestBuilder = EsClientUtils.client.prepareBulk();
        bulkRequestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        // init thread pool and set size 1
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // create beeper thread( it will be sync data to ES cluster)
        // use a commitLock to protected bulk es as thread-save
        final Runnable beeper = new Runnable() {
            public void run() {
                commitLock.lock();
                try {
                    bulkRequest(0);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    commitLock.unlock();
                }
            }
        };

        // set time bulk task
        // set beeper thread(10 second to delay first execution , 30 second period between successive executions)
        scheduledExecutorService.scheduleAtFixedRate(beeper, 10, 30, TimeUnit.SECONDS);

    }

    /**
     * shutdown time task immediately
     */
    public static void shutdownScheduEx() {
        if (null != scheduledExecutorService && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * bulk request when number of builders is grate then threshold
     *
     * @param threshold
     */
    private static void bulkRequest(int threshold) {
        if (bulkRequestBuilder.numberOfActions() > threshold) {
            BulkResponse bulkItemResponse = bulkRequestBuilder.execute().actionGet();
            if (!bulkItemResponse.hasFailures()) {
                bulkRequestBuilder = EsClientUtils.client.prepareBulk();
            }
        }
    }

    /**
     * add update builder to bulk
     * use commitLock to protected bulk as thread-save
     * @param builder
     */
    public static void addUpdateBuilderToBulk(UpdateRequestBuilder builder) {
        commitLock.lock();
        try {
            bulkRequestBuilder.add(builder);
            bulkRequest(MAX_BULK_COUNT);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            commitLock.unlock();
        }
    }

    /**
     * add delete builder to bulk
     * use commitLock to protected bulk as thread-save
     *
     * @param builder
     */
    public static void addDeleteBuilderToBulk(DeleteRequestBuilder builder) {
        commitLock.lock();
        try {
            bulkRequestBuilder.add(builder);
            bulkRequest(MAX_BULK_COUNT);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            commitLock.unlock();
        }
    }
}