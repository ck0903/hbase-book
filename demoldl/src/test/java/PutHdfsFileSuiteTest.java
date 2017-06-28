import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hbase.security.access.Permission;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.Test;

import java.io.*;
import java.net.URI;

/**
 * Created by lenovo on 2017/6/24.
 */
public class PutHdfsFileSuiteTest {
//    public void testToUse
    @Test
    public void toTestPutSomeFilesToHdfs() throws IOException {

        String localDir = "F:\\hadoop-root-namenode-master.log.temp1";
        String desDir = "hdfs://master:9000/test/hadoop-root-namenode-master.log.temp1";
        InputStream in = new BufferedInputStream(new FileInputStream(localDir));

        Configuration conf = new Configuration();
        conf.set("dfs.permissions","false");

        FileSystem fs = FileSystem.get(URI.create(desDir), conf);
        OutputStream out = fs.create(new Path(desDir), new Progressable() {
            @Override
            public void progress() {
                System.out.print(".");
            }
        });

        IOUtils.copyBytes(in, out, 4096, false);
        IOUtils.closeStream(out);
        IOUtils.closeStream(in);

    }

    public void testToUseFSDataOutPutStreamToWriteFile(){
        String localDir = "F:\\hadoop-root-namenode-master.log.temp1";
        String descDir = "hdfs://master:9000/test/test.txt";

    }
}
