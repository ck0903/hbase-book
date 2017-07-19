package com.hzgc.objectinfosuite;

import com.hzgc.hbase.HBaseHelper;
import com.hzgc.unuse.InitHBaseConfiguration;
import com.sun.org.apache.xml.internal.security.Init;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FIHBaseAPISuite {
    @Test
    public void testGetHBaseConfiguration(){
//        Configuration conf = InitHBaseConfiguration.getHBaseConfiguration();
//        System.out.println(conf);
    }

    @Test
    public void testGetHBaseConnection(){
//        Connection connection = InitHBaseConnection.getHbaseConnection();
//        System.out.println(connection);
        Connection connection = HBaseHelper.getHBaseConnection();
        try {
            Admin admin = connection.getAdmin();
            System.out.println(connection + "," + admin);
            boolean flag = admin.tableExists(TableName.valueOf("OnjectInfo"));
            System.out.println(connection + "," + admin + "," + flag);
            admin.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCrateTableWithCoprocessor(){
        //es_cluster=zcits,es_type=zcestestrecord,es_index=zcestestrecord,es_port=9100,es_host=master
        Map<String, String> mapOfOberserverArgs = new HashMap<String, String>();
        mapOfOberserverArgs.put("es_cluster","my-cluser");
        mapOfOberserverArgs.put("es_index","objectinfo");
        mapOfOberserverArgs.put("es_type","pcl");
        mapOfOberserverArgs.put("es_host1","s200");
//        mapOfOberserverArgs.put("es_host2","s101");
//        mapOfOberserverArgs.put("es_host3","s102");
        HBaseHelper helper = new HBaseHelper();
        try {
            helper.dropTable("ObjectInfo");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Table table = helper.crateTableWithCoprocessor("ObjectInfo", "com.hzgosun.hbase2es.HBase2EsObserver",
                "hdfs:///user/ldl/hbase2es-2.0-jar-with-dependencies.jar", mapOfOberserverArgs, 3, "pcl");

        try {
            HBaseHelper.getHBaseConnection().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(table);
    }
}
