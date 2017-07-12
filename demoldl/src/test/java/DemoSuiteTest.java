import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DemoSuiteTest {
    @Test
    public void testGeproperties(){
        Properties properties = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream("hbase_config.properties");
            properties.load(in);
            String hbase_master = properties.getProperty("hbase.master");
            String hbase_zookeeper_quorum = properties.getProperty("hbase.zookeeper.quorum");
            System.out.println(hbase_master);
            System.out.println(hbase_zookeeper_quorum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void forTestVariableLengthArgs(String... clos){
        System.out.println(clos);
        if (clos != null){
            for (String clo:clos){
                System.out.println(clo);
            }
        }else {
            System.out.println("it ' null..");
        }
    }

    @Test
    public void testVariableLengthArgs(){
        Map<String, String> map = new HashMap<>();
        map.put("key01", "value01");
        map.put("key02", "value02");
        map.put("key03", "value03");
        map.put("key04", "value04");
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            System.out.println("kye: ==>  " + key  + "   value  ===>  " + map.get(key));
        }
    }

    @Test
    public void testCopyArray() throws IOException {
        final byte[] POSTFIX = new byte[] { 0x00 };
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtableYaliceshi");
        helper.createTable("testtableYaliceshi", "colfam1","colfam2","colfam3","colfam4","colfam5","clofam6");
        System.out.println("Adding rows to table...");
        helper.fillTable("testtableYaliceshi", 1, 1000000, 10, "colfam1","colfam2","colfam3","colfam4","colfam5","clofam6");

//        Connection connection = ConnectionFactory.createConnection(conf);
//        Table table = connection.getTable(TableName.valueOf("testtable"));
//
//        // vv PageFilterExample
//        // 逻辑，通过设置每次返回的行数，
//        // 判断当前页的最后一行是否空
//        Filter filter = new PageFilter(15);
//
//        int totalRows = 0;
//        byte[] lastRow = null;
//        while (true) {
//            Scan scan = new Scan();
//            scan.setFilter(filter);
//            if (lastRow != null) {
//                byte[] startRow = Bytes.add(lastRow, POSTFIX);
//                System.out.println("start row: " +
//                        Bytes.toStringBinary(startRow));
//                scan.setStartRow(startRow);
//            }
//            ResultScanner scanner = table.getScanner(scan);
//            int localRows = 0;
//            Result result;
//            while ((result = scanner.next()) != null) {
//                System.out.println(localRows++ + ": " + result);
//                totalRows++;
//                lastRow = result.getRow();
//            }
//            scanner.close();
//            if (localRows == 0) break;
//        }
//        System.out.println("total rows: " + totalRows);
//        // ^^ PageFilterExample
    }
}
