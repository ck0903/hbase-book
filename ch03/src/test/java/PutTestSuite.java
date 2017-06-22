import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by lenovo on 2017/6/22.
 */
public class PutTestSuite {
    @Test
    public void testPut_OldMethod() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        HTable table = new HTable(configuration,"test");// 此方法已经不推荐使用。
        Put put = new Put(Bytes.toBytes("row1"));
        put.add(Bytes.toBytes("cf"),Bytes.toBytes("g"),Bytes.toBytes("value6"));//此方法已经不推荐使用。
        put.add(Bytes.toBytes("cf"),Bytes.toBytes("h"),Bytes.toBytes("value7"));//推荐使用addCloumn
        table.put(put);
    }
    @Test
    public void testPut_NewMethod() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf("test"));
        Put put = new Put(Bytes.toBytes("row4"));
        put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("name"),Bytes.toBytes("xiaoming"));
        put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("age"),Bytes.toBytes("18"));
        table.put(put);
        table.close();
        conn.close();

    }
}
