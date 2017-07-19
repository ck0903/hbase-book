package com.hzgc.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HBaseHelper {
    public static Configuration innerHBaseConf = null;
    public static Connection innerHBaseConnection = null;

    public HBaseHelper(){}

    // 初始化HBaseConfiguration
    public static void initHBaseConfiguration(){
        innerHBaseConf = HBaseConfiguration.create();
        String dataDir = System.getProperty("user.dir") + File.separator + "conf";
        innerHBaseConf.set("hbase.zookeeper.quorum","s200");
//        innerHBaseConf.set("hbase.zookeeper.property.clientPort","24002");
//        innerHBaseConf.addResource(dataDir + File.separator + "hbase-site.xml");
//        innerHBaseConf.addResource(dataDir + File.separator + "core-site.xml");
//        innerHBaseConf.addResource(dataDir + File.separator + "hdfs-site.xml");
        //innerHBaseConf.set("hbase.zookeeper.quorum", "s100,s101,s102");
    }

    //对外提供接口，返回HBaseConfiguration
    public static Configuration getHBaseConfiguration(){
        if (innerHBaseConf == null){
            initHBaseConfiguration();
        }
        return innerHBaseConf;
    }

    //初始化HBaseConnection
    public static void initHBaseConnection(){
        try {
            innerHBaseConnection = ConnectionFactory.createConnection(HBaseHelper.getHBaseConfiguration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //对外提供接口，返回HBaseConneciton
    public static Connection getHBaseConnection(){
        if (innerHBaseConnection == null){
            initHBaseConnection();
        }
        return  innerHBaseConnection;
    }

    public  Table crateTableWithCoprocessor(String tableName, String observerName, String path,
                                          Map<String, String> mapOfOberserverArgs,
                                          int maxVersion, String... colfams){
        HTableDescriptor tableDescriptor = null;
        Admin admin = null;
        Table table = null;
        // 创建表格
        try {
            admin = HBaseHelper.getHBaseConnection().getAdmin();
            if (admin.tableExists(TableName.valueOf(tableName))){
                System.out.println("Table: " + tableName + " have already exit, quit with status 0.");
                return getTable(tableName);
            }
            tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            for (String columnFamily:colfams){
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(columnFamily);
                columnDescriptor.setMaxVersions(maxVersion);
                tableDescriptor.addFamily(columnDescriptor);
            }
            admin.createTable(tableDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 添加Coprocessor。
        try {
            table = getTable(tableName);
            admin.disableTable(TableName.valueOf(tableName));
            tableDescriptor.addCoprocessor(observerName, new Path(path), Coprocessor.PRIORITY_USER, mapOfOberserverArgs);
            admin.modifyTable(TableName.valueOf(tableName), tableDescriptor);
            admin.enableTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭admin 对象。
        try {
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  table;
    }


    // 删除表
    public  void dropTable(String name) throws IOException {
        dropTable(TableName.valueOf(name));
    }

    // 删除表
    public  void dropTable(TableName name) throws IOException {
        Admin admin = HBaseHelper.getHBaseConnection().getAdmin();
        boolean flag = admin.tableExists(name);
        if (flag){
            if (admin.isTableEnabled(name)){
                admin.disableTable(name);
            }
            admin.deleteTable(name);
        }
        admin.close();
    }

    // 获取表格
    public  Table getTable(String name) throws IOException {
        return HBaseHelper.getHBaseConnection().getTable(TableName.valueOf(name));
    }


    public void close(){
        try {
            HBaseHelper.getHBaseConnection().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
