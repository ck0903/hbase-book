package com.hzgosun.hbase2es;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class HBase2EsObserverSuite {

    @Test
    public void test01() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","s200");
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("OInfo"));
        Put put = new Put(Bytes.toBytes("row2"));
        put.addColumn(Bytes.toBytes("ocl"), Bytes.toBytes("name"), Bytes.toBytes("xiaoma"));
        put.addColumn(Bytes.toBytes("ocl"), Bytes.toBytes("age"), Bytes.toBytes("12"));
        table.put(put);
        table.close();
        connection.close();
    }
}
