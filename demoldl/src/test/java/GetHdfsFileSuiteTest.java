import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class GetHdfsFileSuiteTest {

    //method 1 ,through api
    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }
    @Test
    public void testToUseUrlToGetFile() throws IOException {
        InputStream in = null;
        in = new URL("hdfs://master:9000/test/hadoop-root-namenode-master.log").openStream();
        IOUtils.copyBytes(in, System.out, 4096,false);
        IOUtils.closeStream(in);
    }


    // method 2 ,throught FileSystem
    @Test
    public  void testToUseFileSystemToGetFile() throws IOException {
        String uri = "hdfs://master:9000/test/ldl.txt";
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://master:9000");
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        InputStream in = null;
        in = fs.open(new Path(uri));
        IOUtils.copyBytes(in, System.out, 4096, false);
        IOUtils.closeStream(in);
    }

    // method 3, through  FSDataInputStream
    @Test
    public void testToUseFsDataInputStreamToGetFile() throws IOException {
        String uri = "hdfs://master:9000/test/ldl.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FSDataInputStream in = null;
        in = fs.open(new Path(uri));
        IOUtils.copyBytes(in, System.out, 4096, false);
        IOUtils.closeStream(in);
    }
}
