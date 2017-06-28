package admin;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Map;

// cc HColumnDescriptorExample Example how to create a HColumnDescriptor in code
public class HColumnDescriptorExample {

  public static void main(String[] args) throws IOException, InterruptedException {
    // vv HColumnDescriptorExample
    // 创建一个HColumnDescriptor  对象，即创建一个列族，列族的名字是colfam1,
    // 其中有一个kv 对， K 的名字是test-key V 的值是test-value
    // 后面的setBloomFilterType 的作用是，设置一个'布隆'过滤器，在一定的条件下，可以提高其随机读取的效率，
    // 但是会比较耗费内存
    HColumnDescriptor desc = new HColumnDescriptor("colfam1")
      .setValue("test-key", "test-value")
      .setBloomFilterType(BloomType.ROWCOL);

    System.out.println("Column Descriptor: " + desc);

    System.out.print("Values: ");
    for (Map.Entry<ImmutableBytesWritable, ImmutableBytesWritable>
      entry : desc.getValues().entrySet()) {
      System.out.print(Bytes.toString(entry.getKey().get()) +
        " -> " + Bytes.toString(entry.getValue().get()) + ", ");
    }
    System.out.println();

    System.out.println("Defaults: " +
      HColumnDescriptor.getDefaultValues());

    System.out.println("Custom: " +
      desc.toStringCustomizedValues());

    System.out.println("Units:");
    System.out.println(HColumnDescriptor.TTL + " -> " +
      desc.getUnit(HColumnDescriptor.TTL));
    System.out.println(HColumnDescriptor.BLOCKSIZE + " -> " +
      desc.getUnit(HColumnDescriptor.BLOCKSIZE));
    // ^^ HColumnDescriptorExample
  }
}
