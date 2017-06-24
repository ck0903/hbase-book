import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlCat {
    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }
    @Test
    public void testUrlCat(String []args) throws IOException {
        InputStream in = null;
        in = new URL("hdfs://master:9000/test/hadoop-root-namenode-master.log").openStream();
        IOUtils.copyBytes(in, System.out, 4096,false);
        IOUtils.closeStream(in);
    }
}