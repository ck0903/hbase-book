package ldl_utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;


/**
 * Created by lenovo on 2017/6/28.
 */
public class HBaseUtil {
    private Configuration configuration = null;
    private Admin admin = null;
    private Connection connection =null;

    public HBaseUtil(Configuration configuration) throws IOException {
        this.configuration = configuration;
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();
    }

    public  boolean exitTable(String name) throws IOException {
        return  this.admin.tableExists(TableName.valueOf(name));
    }

    public static HBaseUtil getHBaseUtils(Configuration configuration) throws IOException {
        return  new HBaseUtil(configuration);
    }
    // 删除表
    public void dropTabel(String name) throws IOException {
        dropTabel(TableName.valueOf(name));
    }

    // 删除表
    public void dropTabel(TableName name) throws IOException {
        if (admin.tableExists(name)){
            if (admin.isTableEnabled(name)){
                admin.disableTable(name);
            }
            admin.deleteTable(name);
        }
    }

    // 创建表
    public void crateTable(String name, String... colfams) throws IOException {
        crateTable(TableName.valueOf(name), colfams);
    }

    // 创建表
    public void crateTable(TableName name, String... colfams) throws IOException {
        HTableDescriptor hTableDescriptor = new HTableDescriptor(name);
        for (String colfam:colfams){
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(colfam);
            hTableDescriptor.addFamily(hColumnDescriptor);
        }
        admin.createTable(hTableDescriptor);
    }

    // 获取表格
    public Table getTable(String name) throws IOException {
        return this.connection.getTable(TableName.valueOf(name));
    }

    public void close() throws IOException {
        connection.close();
    }
}
