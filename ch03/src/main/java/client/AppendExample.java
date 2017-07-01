package client;

// cc AppendExample Example application appending data to a column in  HBase

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;


public class AppendExample {

  public static void main(String[] args) throws IOException {
    Configuration conf = HBaseConfiguration.create(); //获取Hbase 的集群配置。

    HBaseHelper helper = HBaseHelper.getHelper(conf); //通过工具类，获取链接，并且得到Admin 对象。
    helper.dropTable("testtable"); // 删除表格
    helper.createTable("testtable", 100, "colfam1", "colfam2");// 创建表格，表格有两个列族
    helper.put("testtable",    // 表明
      new String[] { "row1" },  // Row key
      new String[] { "colfam1" },  // 列族
      new String[] { "qual1" },    // K
      new long[]   { 1 },     // Time,Version
      new String[] { "oldvalue" });   // values
    System.out.println("Before append call...");
    helper.dump("testtable", new String[]{ "row1" }, null, null);

//    Connection connection = ConnectionFactory.createConnection(conf);
//    Table table = connection.getTable(TableName.valueOf("testtable"));
//
//    // vv AppendExample
//    Append append = new Append(Bytes.toBytes("row1"));
//    append.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
//      Bytes.toBytes("newvalue"));
//    append.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"),
//      Bytes.toBytes("anothervalue"));
//
//    table.append(append);
//    // ^^ AppendExample
//    System.out.println("After append call...");
//    helper.dump("testtable", new String[]{"row1"}, null, null);
//    table.close();
//    connection.close();
    helper.close();
  }
}
