package com.hzgosun.hbase2es;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HBase2EsObserverSuite {

    @Test
    public void test01() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("ldl", "dsb");
        byte[] buffer = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(map);
            buffer = bo.toByteArray();
            bo.close();
            oo.close();
            Object pp  = null;
            pp = ByteToObject(buffer);
            Map cc = (HashMap<String, String>)pp;
            System.out.println(cc.get("ldl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
    @Test
    public void testSearchByName(){

    }
}
