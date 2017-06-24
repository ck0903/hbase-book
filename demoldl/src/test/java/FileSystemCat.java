import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

public class FileSystemCat {
    @Test
    public  void testFileSystemCat(String []args) throws IOException {
       String uri = "hdfs://master:9000/test/hadoop-root-namenode-master.log";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
    }
}
