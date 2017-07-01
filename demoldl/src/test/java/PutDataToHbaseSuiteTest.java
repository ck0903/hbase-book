import javafx.scene.control.Tab;
import ldl_utils.HBaseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/28.
 */
public class PutDataToHbaseSuiteTest {
    // 不推荐的put 方法
    @Test
    public void testPutSomethingToHbase_OldMethod() throws IOException {
        Configuration conf = new HBaseConfiguration();
        conf.set("hbase.zookeeper.quorum", "master:2181,slave1:2181,slave2:2181");
        HTable table = new HTable(conf, "test");
        Put put = new Put(Bytes.toBytes("row1"));
        put.add(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("xiaoming"));
        put.add(Bytes.toBytes("cf"), Bytes.toBytes("age"), Bytes.toBytes("20"));
        put.add(Bytes.toBytes("cf"), Bytes.toBytes("city"), Bytes.toBytes("hangzhou"));
        table.put(put);
        table.close();
    }

    // 推荐使用的put 方法
    // 另外Hbase 的集群中zk 所在的机器，必须是可以外置的，不推荐写死在代码内部
    // 另外 配置文件中，ZK 名字最好可以是相应的名字的映射，即一个对应ip 的映射
    @Test
    public void testPutSomethingToHbase_NewMethod01() throws IOException {
        Configuration hbase_conf = HBaseConfiguration.create();
        hbase_conf.set("hbase.zookeeper.quorum","master:2181,slave1:2181,slave2:2181");
        Connection hbase_conn = ConnectionFactory.createConnection(hbase_conf);
        Table test_tabel = hbase_conn.getTable(TableName.valueOf("test"));
        Put put = new Put(Bytes.toBytes("row2"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("xiaoming"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("age"), Bytes.toBytes("20"));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("city"), Bytes.toBytes("lijiang"));
        test_tabel.put(put);
        test_tabel.close();
    }

    // 测试删除表，
    // 删除表，1，表明要存在，2，表要被disable 掉，
    @Test
    public void testDropTabel() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","master,slave1,slave2");
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        if (admin.tableExists(TableName.valueOf("test01"))){
            if (admin.isTableEnabled(TableName.valueOf("test01"))) {
                admin.disableTable(TableName.valueOf("test01"));
                admin.deleteTable(TableName.valueOf("test01"));
            }else {
                System.out.println("Table is disabel..");
            }
        }else {
            System.out.println("There is no table named test01...");
        }

        admin.close();
        conn.close();
    }

    // 测试创建表格
    @Test
    public void testCreateTable() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","master,slave1,slave2");
        Connection conn = ConnectionFactory.createConnection(conf);
    }

    // 测试HBaseUtils 类
    @Test
    public  void testHbaseUtils() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "master,slave1,slave2");
        HBaseUtil hBaseUtil = HBaseUtil.getHBaseUtils(configuration);
        hBaseUtil.dropTable("testDemo");
        hBaseUtil.createTable("testDemo", "nima");
        hBaseUtil.close();
    }

    // Test, 测试插入数据之后的简单地获取数据
    @Test
    public void testAfterPut() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","master,slave1,slave2");

        HBaseUtil hBaseUtil = HBaseUtil.getHBaseUtils(configuration);

        hBaseUtil.dropTable("person");
        hBaseUtil.createTable("person", 10, "base_info", "pet");

        Table table = hBaseUtil.getTable("person");

        Put put1 = new Put(Bytes.toBytes("row1"));
        put1.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("name"), 2,  Bytes.toBytes("李第亮"));
        put1.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("city"),  2, Bytes.toBytes("杭州"));
        put1.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("jobs"),  2, Bytes.toBytes("大数据开发工程师"));

        put1.addColumn(Bytes.toBytes("pet"), Bytes.toBytes("name"), 2,  Bytes.toBytes("浪里个浪里个浪"));
        put1.addColumn(Bytes.toBytes("pet"), Bytes.toBytes("colour"),  2, Bytes.toBytes("橘黄色"));
        table.put(put1);

        Put put = new Put(Bytes.toBytes("row1"));

        put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("name"), Bytes.toBytes("李第亮"));
        put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("city"), Bytes.toBytes("杭州"));
        put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("jobs"), Bytes.toBytes("大数据开发工程师"));

        put.addColumn(Bytes.toBytes("pet"), Bytes.toBytes("name"), Bytes.toBytes("浪里个浪"));
        put.addColumn(Bytes.toBytes("pet"), Bytes.toBytes("colour"), Bytes.toBytes("橘黄色"));

        table.put(put);


        Get get = new Get(Bytes.toBytes("row1"));
        Result result = table.get(get);

        System.out.println("result:" + result + "\n" + "person name: "  +
                Bytes.toString( result.getValue(Bytes.toBytes("base_info"), Bytes.toBytes("name")))
                + "  pet ' name: "  + Bytes.toString(result.getValue(Bytes.toBytes("pet"), Bytes.toBytes("name"))) );
        table.close();
        hBaseUtil.close();
    }

    @Test
    public void testUsePutListToHbase() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","master,slave1,slave2");
        HBaseUtil hBaseUtil = HBaseUtil.getHBaseUtils(configuration);
        hBaseUtil.dropTable("test01");
        hBaseUtil.createTable("test01", "person_info");
        Table table = hBaseUtil.getTable("test01");
        List<Put> puts = new ArrayList<>();

        Put put01 = new Put(Bytes.toBytes("row1"));
        put01.addColumn(Bytes.toBytes("person_info"), Bytes.toBytes("name"), Bytes.toBytes("langligelang"));
        put01.addColumn(Bytes.toBytes("person_info"), Bytes.toBytes("city"), Bytes.toBytes("hangzhou"));
        puts.add(put01);

        Put put02 = new Put(Bytes.toBytes("row2"));
        put02.addColumn(Bytes.toBytes("person_info"), Bytes.toBytes("name"), Bytes.toBytes("lianggeenha"));
        put02.addColumn(Bytes.toBytes("person_info"), Bytes.toBytes("city"), Bytes.toBytes("yulin"));
        puts.add(put02);

        table.put(puts);

        table.close();
        hBaseUtil.close();
    }

    @Test
    public void testPutBuffered(){

    }

    public void createTable(TableName name, int maxVersions, String... colfams) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","master,slave1,slave2");
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();
        HTableDescriptor descriptor = new HTableDescriptor(name);
        HColumnDescriptor columnDescriptor = null;
        for (String colfam:colfams){
            columnDescriptor = new HColumnDescriptor(Bytes.toBytes(colfam));
            columnDescriptor.setMaxVersions(maxVersions);
        }
        if (admin.tableExists(name)){
            System.out.println("Table exit.");
        } else {

        }
    }
}
