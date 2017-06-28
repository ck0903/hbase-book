package client;

// cc RowKeyExample Example row key usage from existing array
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class RowKeyExample {

  public static void main(String[] args) {
    // vv RowKeyExample
    byte[] data = new byte[100];
    Arrays.fill(data, (byte) '@');
    System.out.println(Arrays.toString(data));
    String username = "johndoe";
    byte[] username_bytes = username.getBytes(Charset.forName("UTF8"));

    // 系统的ArrayCopy
    // 把username_bytes 中下标从0 开始一直到最后的数据， 拷贝到 data 中下表为第45 开始，增加7位
    System.arraycopy(username_bytes, 0, data, 45, username_bytes.length);
    System.out.println("data length: " + data.length +
      ", data: " + Bytes.toString(data));

    Put put = new Put(data, 45, username_bytes.length);
    System.out.println("Put: " + put);
    // ^^ RowKeyExample
  }
}
