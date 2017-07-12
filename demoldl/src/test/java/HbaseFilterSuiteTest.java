import com.hzgosun.HBaseHelper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HbaseFilterSuiteTest {
    private Connection connection = null;
    private Configuration configuration = null;
    private HBaseHelper hBaseUtil = null;
    @Before
    public void toInitSomethingForSuiteTest() throws IOException {
         this.configuration = HBaseConfiguration.create();
         this.hBaseUtil = HBaseHelper.getHBaseHelper(configuration);
    }

//    @After
//    public void toCloseSomtingUnUse() throws IOException {
//        this.connection.close();
//    }

    @Test
    public void testQuarifierFilter() throws IOException {
        Table table = hBaseUtil.getTable("ldltest");
        Filter filter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("person_info2-087")));
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        System.out.println("testing------------------------");
        for (Result result:scanner){
            System.out.println(result);
        }

        System.out.println("testing--------------------");
        Scan scan1 = new Scan().addColumn(Bytes.toBytes("person_info2"), Bytes.toBytes("person_info2-087"));
        ResultScanner results = table.getScanner(scan);
        for (Result result:results){
            System.out.println(result);
        }
        table.close();
        hBaseUtil.close();
    }



    // 以下是关于列族过滤器的测试
    @Test
    public void testFamilyFiter() throws IOException {
        Table table = hBaseUtil.getTable("ldltest");
//        Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.LESS, new BinaryComparator(Bytes.toBytes("person_info3")));
//        Scan scan = new Scan();
//        scan.setFilter(filter1);
//        ResultScanner results = table.getScanner(scan);
//        for (Result result:results){
//            System.out.println(result);
//        }
//
//        System.out.println("testing2---------------------");
//        Get get1 = new Get(Bytes.toBytes("row-005"));
//        get1.setFilter(filter1);
//        Result result = table.get(get1);
//        System.out.println(result);

        System.out.println("testing-----------------------");
        System.out.println();
        Filter filter2 = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("person_info3")));
        Scan scan1 = new Scan();
        scan1.setFilter(filter2);
        ResultScanner results1 = table.getScanner(scan1);
        for (Result result1: results1){
            System.out.println(result1);
        }
        table.close();
        hBaseUtil.close();
    }
    // 以下是关于行过滤器的使用测试
    //1，同样的基础的部分，首先；要获取Connection以及Table 实例对象。
    //2，用Scan 来查找一批数据
    //3，创建Fiter 实例对象，传入两个参数，一个比较运算符，一个比较器，作用，对Scan 查找出来的数据进一步的过滤。
    //4，如下的fiter 是行过滤器，则针对过滤的是行，
    //5，filter 属于精确匹配。会过滤掉小于row-096 的值（匹配大于或者等于row-96 的行）
    //6，filter1 属于正则表达式查找。（匹配）
    //7，filter2 属于子串匹配查找
    @Test
    public void testRowFiter01() throws IOException {
        Table table = hBaseUtil.getTable("ldltest");
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes("person_info1"), Bytes.toBytes("person_info1-041"));

        Filter filter = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes("row-096")));
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        System.out.println("精确匹配：.........................");
        for (Result result:scanner){
            System.out.println("result:" + result);
        }

        System.out.println("正则表达式比较：........................");
        Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^.*-.*6$"));
        scan.setFilter(filter1);
        ResultScanner scanner1 = table.getScanner(scan);
        for(Result result:scanner1){
            System.out.println("result:" + result);
        }

        System.out.println("子串匹配................................");
        Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("-09"));
        scan.setFilter(filter2);
        ResultScanner scanner2 = table.getScanner(scan);
        for (Result result:scanner2){
            System.out.println("result:"+ result);
        }
        table.close();
        hBaseUtil.close();
    }

    /*@Test
    public void testRowFilter02(){

    }*/

   //@Before
    public void createTestDataForSuiteTest() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        HBaseHelper hBaseUtil = HBaseHelper.getHBaseHelper(configuration);
        hBaseUtil.dropTable("ldltest");
        hBaseUtil.createTable("ldltest", "person_info1", "person_info2","person_info3");
        Table table = hBaseUtil.getTable("ldltest");
        List<Put> puts = new ArrayList<>();
        for(int row = 1; row <= 100; row++){
            int left = 3 - Integer.toString(row).length();
            String add = "";
            if (left == 2){
                add = "00";
            } else if (left == 1) {
                add = "0";
            } else {
                add = "";
            }
            Put put = new Put(Bytes.toBytes("row-" + add + row));
            Random random = new Random();
            for(int col = 1; col <= 100; col++){
                int left_col = 3 - Integer.toString(col).length();
                String add_col = "";
                if (left_col == 2){
                    add_col = "00";
                } else if (left_col == 1) {
                    add_col = "0";
                } else {
                    add_col = "";
                }
                put.addColumn(Bytes.toBytes("person_info1"),
                        Bytes.toBytes("person_info1-" + add_col  + col),
                        Bytes.toBytes("value-"  + add_col + col ));
                put.addColumn(Bytes.toBytes("person_info2"),
                        Bytes.toBytes("person_info2-" + add_col + col),
                        Bytes.toBytes("value-"+ add_col + col ));
                put.addColumn(Bytes.toBytes("person_info3"),
                        Bytes.toBytes("person_info3-" + add_col + col),
                        Bytes.toBytes("value-"+ add_col + col ));
            }
            puts.add(put);
        }
        table.put(puts);
        table.close();
        hBaseUtil.close();
    }
}
