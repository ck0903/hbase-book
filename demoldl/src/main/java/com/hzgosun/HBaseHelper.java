package com.hzgosun;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;


/**
 * Created by lenovo on 2017/6/28.
 */
public class HBaseHelper {
    private Configuration configuration = null;
    private Admin admin = null;
    private Connection connection =null;


    public Connection getConnection(){
        return this.connection;
    }
    // 构造删除
    public HBaseHelper(Configuration configuration) throws IOException {
        this.configuration = configuration;
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();
    }

    public BufferedMutator getMutator(String table) throws IOException {
        return connection.getBufferedMutator(TableName.valueOf(table));
    }

    // 判断表格是否存在
    public  boolean exitTable(String name) throws IOException {
        return  this.admin.tableExists(TableName.valueOf(name));
    }

    // 返回HbaseUtil
    public static HBaseHelper getHBaseHelper(Configuration configuration) throws IOException {
        return  new HBaseHelper(configuration);
    }
    // 删除表
    public void dropTable(String name) throws IOException {
        dropTable(TableName.valueOf(name));
    }

    // 删除表
    public void dropTable(TableName name) throws IOException {
        boolean flag = admin.tableExists(name);
        if (flag){
            if (admin.isTableEnabled(name)){
                admin.disableTable(name);
            }
            admin.deleteTable(name);
        }
    }

    // 创建表
    public void createTable(String name, String... colfams) throws IOException {
        createTable(TableName.valueOf(name), colfams);
    }

    // 创建表
    public void createTable(TableName name, String... colfams) throws IOException {
        createTable(name, 1, colfams);
    }

    // 创建表格
    public void createTable(String name, int maxVersions, String... colfams) throws IOException {
        createTable(TableName.valueOf(name), maxVersions, colfams);
    }

    // 创建表格
    public void createTable(TableName name, int maxVersions, String... colfams) throws IOException {
        boolean flag = admin.tableExists(name);
        if (admin.tableExists(name)){
            System.out.println("Table exit");
            return;
        }
        HTableDescriptor hTableDescriptor = new HTableDescriptor(name);
        for (String colfam:colfams){
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(colfam);
            hColumnDescriptor.setMaxVersions(maxVersions);
            hTableDescriptor.addFamily(hColumnDescriptor);
        }
//        HTableDescriptor descriptor = admin.getTableDescriptor(name);
        boolean nima = true;
        System.out.println();
        admin.createTable(hTableDescriptor);
    }

    // 获取表格
    public Table getTable(String name) throws IOException {
        return this.connection.getTable(TableName.valueOf(name));
    }

    // 关闭连接
    public void close() throws IOException {
        admin.close();
        connection.close();
    }
}
