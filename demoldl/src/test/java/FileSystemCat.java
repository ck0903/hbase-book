import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class FileSystemCat {
    @Test
    public  void testFileSystemCat(String []args) throws IOException {
       String uri = "hdfs://master:9000/test/hadoop-root-namenode-master.log";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        InputStream in = null;
        in = fs.open(new Path(uri));
        IOUtils.copyBytes(in, System.out, 4096, false);
        IOUtils.closeStream(in);

    }
}
