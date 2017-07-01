package client;

// cc DeleteExample Example application deleting data from HBase
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import util.HBaseHelper;
// Done
public class DeleteExample {


  // 可以查询Hbase 官网API
  public static void main(String[] args) throws IOException {
    Configuration conf = HBaseConfiguration.create();

    HBaseHelper helper = HBaseHelper.getHelper(conf);
//    helper.dropTable("testtable");
//    helper.createTable("testtable", 100, "colfam1", "colfam2");
//    helper.put("testtable",
//      new String[] { "row1" },
//      new String[] { "colfam1", "colfam2" },
//      new String[] { "qual1", "qual1", "qual2", "qual2", "qual3", "qual3" },
//      new long[]   { 1, 2, 3, 4, 5, 6 },
//      new String[] { "val1", "val1", "val2", "val2", "val3", "val3" });
//    System.out.println("Before delete call...");
//    helper.dump("testtable", new String[]{ "row1" }, null, null);
    // 数据
/*
    ROW                                                                  COLUMN+CELL
    row1                                                                column=colfam1:qual1, timestamp=2, value=val1
    row1                                                                column=colfam1:qual1, timestamp=1, value=val1
    row1                                                                column=colfam1:qual2, timestamp=4, value=val2
    row1                                                                column=colfam1:qual2, timestamp=3, value=val2
    row1                                                                column=colfam1:qual3, timestamp=6, value=val3
    row1                                                                column=colfam1:qual3, timestamp=5, value=val3
    row1                                                                column=colfam2:qual1, timestamp=2, value=val1
    row1                                                                column=colfam2:qual1, timestamp=1, value=val1
    row1                                                                column=colfam2:qual2, timestamp=4, value=val2
    row1                                                                column=colfam2:qual2, timestamp=3, value=val2
    row1                                                                column=colfam2:qual3, timestamp=6, value=val3
    row1                                                                column=colfam2:qual3, timestamp=5, value=val3*/

    Connection connection = ConnectionFactory.createConnection(conf);
    Table table = connection.getTable(TableName.valueOf("testtable"));

    // vv DeleteExample
    Delete delete = new Delete(Bytes.toBytes("row1")); // co DeleteExample-1-NewDel Create delete with specific row.
//
//    delete.setTimestamp(1); // co DeleteExample-2-SetTS Set timestamp for row deletes.，
//    //此处的timestamp 对addCloumn 和addCloumns 有影响,不要轻易的设置一个全局的timetaamp
//
//    delete.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1")); // co DeleteExample-3-DelColNoTS Delete the latest version only in one column.
//    delete.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual3"), 3); // 没有则不删除co DeleteExample-4-DelColTS Delete specific version in one column.
//    delete.addColumns(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1")); // co DeleteExample-5-DelColsNoTS Delete all versions in one column.，所有的row,colfam1,qual1
//    // co DeleteExample-6-DelColsTS Delete the given and all older versions in one column.
//    delete.addColumns(Bytes.toBytes("colfam1"), Bytes.toBytes("qual3"), 3);  // 小于或者等于这个值
//
//    delete.addFamily(Bytes.toBytes("colfam1")); // co DeleteExample-7-AddCol Delete entire family, all columns and versions.
    delete.addFamily(Bytes.toBytes("colfam1"), 3); // co DeleteExample-8-AddCol Delete the given and all older versions in the entire column family, i.e., from all columns therein.
//
    table.delete(delete); // co DeleteExample-9-DoDel Delete the data from the HBase table.

   //  ^^ DeleteExample
    table.close();
    connection.close();
    System.out.println("After delete call...");
    helper.dump("testtable", new String[] { "row1" }, null, null);
    helper.close();
  }
}
