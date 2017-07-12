import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;


/**
 * Created by lenovo on 2017/6/28.
 */
public class DeleteHbaseSuiteTest {
    @Test
    public void test() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "master,slave1,slave2");
        HBaseHelper util = HBaseHelper.getHelper(configuration);
//        util.dropTabel("person");
//        util.crateTable("person", "base_info", "pet");
//
//        List<Put> puts = new ArrayList<>();
//        Put put = null;
//        for(int i=1; i<=100; i++){
//            put = new Put(Bytes.toBytes("row-" + i ));
//            put.addColumn(Bytes.toBytes("base_info"),Bytes.toBytes("name"), i,  Bytes.toBytes("xiaohong-" + i));
//            put.addColumn(Bytes.toBytes("base_info"),Bytes.toBytes("city"), i, Bytes.toBytes("hagnzhou-" + i));
//            put.addColumn(Bytes.toBytes("base_info"),Bytes.toBytes("job"), i, Bytes.toBytes("banzhuan-" + i));
//            put.addColumn(Bytes.toBytes("pet"),Bytes.toBytes("name"), i, Bytes.toBytes("wangwang-" + i));
//            put.addColumn(Bytes.toBytes("pet"),Bytes.toBytes("color"), i, Bytes.toBytes("hei-" + i));
//            puts.add(put);
//        }
//
//        Table table = util.getTable("person");
//        table.put(puts);
//        Delete delete = new Delete(Bytes.toBytes("row-1"));
//        delete.setTimestamp(1);// 设置timestam
//        delete.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("job"));
//
//        delete = new Delete(Bytes.toBytes("row-2"));
//        // 尽管没有这个timestam 为1的，但是没有关系，会删除最新的timestamp的
//        // 如果delete 里面没有这一行，也不会报错
//        delete.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("job"));
//
//        delete = new Delete(Bytes.toBytes("row1"));// 没有这一行，但是不会报错
//        delete.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("job"));
//        table.delete(delete);

//        // 删除整个列族
//        delete = new Delete(Bytes.toBytes("row-98"));
//        delete.addFamily(Bytes.toBytes("pet"));
//        table.delete(delete);
//
//        table.close();
//        util.close();
    }

    @Test
    public void testDeleteListe() throws IOException {
//        Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum", "master,slave1,slave2");
//        HBaseHelper helper = HBaseHelper.getHBaseHelper(conf);
//        helper.dropTable("testtable");
//        helper.createTable("testtable", 100, "colfam1", "colfam2");
//        helper.put("testtable",
//                new String[] { "row1" },
//                new String[] { "colfam1", "colfam2" },
//                new String[] { "qual1", "qual1", "qual2", "qual2", "qual3", "qual3" },
//                new long[]   { 1, 2, 3, 4, 5, 6 },
//                new String[] { "val1", "val1", "val2", "val2", "val3", "val3" });
//       /* helper.put("testtable",
//                new String[] { "row2" },
//                new String[] { "colfam1", "colfam2" },
//                new String[] { "qual1", "qual1", "qual2", "qual2", "qual3", "qual3" },
//                new long[]   { 1, 2, 3, 4, 5, 6 },
//                new String[] { "val1", "val2", "val3", "val4", "val5", "val6" });
//        helper.put("testtable",
//                new String[] { "row3" },
//                new String[] { "colfam1", "colfam2" },
//                new String[] { "qual1", "qual1", "qual2", "qual2", "qual3", "qual3" },
//                new long[]   { 1, 2, 3, 4, 5, 6 },
//                new String[] { "val1", "val2", "val3", "val4", "val5", "val6" });
//        System.out.println("Before delete call...");*/
//        helper.dump("testtable", new String[]{ "row1", "row2", "row3" }, null, null);
    }
}
