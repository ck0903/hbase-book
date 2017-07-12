import com.hzgosun.HBaseHelper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;
import java.util.*;

public class PageHandler {

    public List<Map> getPage(int startLow, int pageSize) throws IOException {
        byte[] POSTFIX = new byte[] { 0x00 };
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("testtableYaliceshi"));

        FilterList filterList = new FilterList();
        //filterList.addFilter(filter);
        Filter filter1 = new ValueFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^.*-1.*$"));
        filterList.addFilter(filter1);

        Scan scan = new Scan();
        scan.setFilter(filterList);

        ResultScanner results = table.getScanner(scan);
        int flag = 0;
        byte[] startRow = null;
        byte[] endRow = null;
        for (Result result: results) {
            flag++;
            System.out.println(result.getRow());
            System.out.println(flag + " : " + result);
            if (flag == startLow) {
                startRow = result.getRow();
            }
            if (flag == startLow + pageSize ){
                endRow = result.getRow();
                break;
            }
        }

        scan.setStartRow(startRow);
        scan.setStopRow(endRow);
        Filter filter = new PageFilter(pageSize);
        scan.setFilter(filter);
        System.out.println("--------------------------------");
         results = table.getScanner(scan);
        for (Result result: results){
            System.out.println(result);
        }

        results.close();
        connection.close();
        return null;
    }

    @Test
    public void testGetPage() throws IOException {
        getPage(1, 100);
    }


    public byte[] readPitureFromLocal(String path) throws IOException {
        File file = new File(path);
        if (! file.isFile()){
            System.out.println("File not exits!!!");
            return null;
        }
        byte[] data = null;
        FileImageInputStream in = new FileImageInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int numRead = 0;
        while ((numRead = in.read(buf)) != -1){
            out.write(buf, 0, numRead);
        }
        data = out.toByteArray();

        out.close();
        in.close();
        return data;
    }
    @Test
    public void testDemo() throws IOException {
        byte[] data = readPitureFromLocal("C:\\Users\\lenovo\\Desktop\\temp\\HBASECRUD.png");
        FileImageOutputStream out = new FileImageOutputStream(new File("C:\\Users\\lenovo\\Desktop\\temp\\nima.png"));
        out.write(data, 0 , data.length);
        out.close();
    }


    public String getIdCardOrTKey(int lenght){
        byte[] nums = null;
        Random random = new Random();
        String idCard = "";
        if (lenght == 18 ){
            nums = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        } else {
            nums = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'f', 'g', 'h',
                    'g', 'h', 'i', 'j', 'k', 'm', 'n','o', 'p','q','r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        }
        for (int i = 0;i < lenght; i++){
            idCard += nums[random.nextInt(nums.length)];
        }
        return idCard;
    }

    //可以尝试另一种List 的Put方法。
    // 注意有时候会出现tableExixts 中是不存在PersonInfo 而 创建表的时候，却提示的是由这张表这样的矛盾
    // 此时可以清理掉Hbase 的数据目录和ZK 的数据目录，
    @Test
    public void testCreatePersonInfoAndEvalTableAndPerpareData1() throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("starting02 01------------------------------------");
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.master", "192.168.1.222:60000");
        configuration.set("hbase.zookeeper.quorum", "192.168.1.222");
        HBaseHelper hBaseUtil = HBaseHelper.getHBaseHelper(configuration);
        hBaseUtil.dropTable("ObjectInfo");
        hBaseUtil.createTable("ObjectInfo", "pcl");
        hBaseUtil.dropTable("EigenValue");
        hBaseUtil.createTable("EigenValue", "ecl");
        BufferedMutator mutator1 = hBaseUtil.getMutator("PersonInfo");
        BufferedMutator mutator2 = hBaseUtil.getMutator("EigenValue");
        List<Put> puts1 = new ArrayList<>();
        List<Put> puts2 = new ArrayList<>();
        byte[] pic = readPitureFromLocal("C:\\Users\\lenovo\\Desktop\\temp\\HBASECRUD.png");
        for (int i = 0; i <= 10000; i++) {
            String idCard = getIdCardOrTKey(18);
            String tKey = getIdCardOrTKey(3);
            Put rowOfPersonInfo = new Put(Bytes.toBytes( idCard + tKey + "hz1" ))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("name"), Bytes.toBytes("nima-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("idcard"), Bytes.toBytes(idCard + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("sex"), Bytes.toBytes(new Random().nextInt(2)))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("reason"), Bytes.toBytes("reason-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("tkey"), Bytes.toBytes("tkey-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("photo"), pic)
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("warn"), Bytes.toBytes("warn-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("cman"), Bytes.toBytes("cman-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("cmtel"), Bytes.toBytes("cmtel-" + i));
            puts1.add(rowOfPersonInfo);
            Put rowOfEigenValue = new Put(Bytes.toBytes("hz1" + idCard + tKey))
                    .addColumn(Bytes.toBytes("ecl"), Bytes.toBytes("eval"), Bytes.toBytes("eval-" + i));
            puts2.add(rowOfEigenValue);
            if (i % 5 == 0) {
                mutator1.mutate(puts1);
                mutator2.mutate(puts2);
                puts1.clear();
                puts2.clear();
                mutator1.flush();
                mutator2.flush();
            }
        }
        mutator1.flush();
        mutator2.flush();
        long end = System.currentTimeMillis();
        System.out.println("ending 01 02 03------------------------------------");
        System.out.println("spend time: " + (end - start)*1.0/1000 + "  s");
        mutator1.close();
        mutator2.close();
        hBaseUtil.close();
    }
    @Test
    public void testCreatePersonInfoAndEvalTableAndPerpareData2() throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("starting02 01------------------------------------");
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.master", "192.168.59.131:60000");
        configuration.set("hbase.zookeeper.quorum", "192.168.59.131");
        HBaseHelper hBaseUtil = HBaseHelper.getHBaseHelper(configuration);
        hBaseUtil.dropTable("PersonInfo02");
        hBaseUtil.createTable("PersonInfo02", "pcl");
        hBaseUtil.dropTable("EigenValue02");
        hBaseUtil.createTable("EigenValue02", "ecl");
        BufferedMutator mutator1 = hBaseUtil.getMutator("PersonInfo02");
        BufferedMutator mutator2 = hBaseUtil.getMutator("EigenValue02");
        List<Mutation> mutations01 = new ArrayList<>();
        List<Mutation> mutations02 = new ArrayList<>();
        byte[] pic = readPitureFromLocal("C:\\Users\\lenovo\\Desktop\\temp\\HBASECRUD.png");
        for (int i = 0; i < 5000; i++) {
            String idCard = getIdCardOrTKey(18);
            String tKey = getIdCardOrTKey(3);
            Put rowOfPersonInfo = new Put(Bytes.toBytes( idCard + tKey + "hz1" ))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("name"), Bytes.toBytes("nima-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("idcard"), Bytes.toBytes(idCard + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("sex"), Bytes.toBytes(new Random().nextInt(2)))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("reason"), Bytes.toBytes("reason-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("tkey"), Bytes.toBytes("tkey-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("photo"), pic)
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("warn"), Bytes.toBytes("warn-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("cman"), Bytes.toBytes("cman-" + i))
                    .addColumn(Bytes.toBytes("pcl"), Bytes.toBytes("cmtel"), Bytes.toBytes("cmtel-" + i));
            mutations01.add(rowOfPersonInfo);
            Put rowOfEigenValue = new Put(Bytes.toBytes("hz1" + idCard + tKey))
                    .addColumn(Bytes.toBytes("ecl"), Bytes.toBytes("eval"), Bytes.toBytes("eval-" + i));
            mutations02.add(rowOfEigenValue);

            mutator1.mutate(mutations01);
            mutator2.mutate(mutations02);
            if (i % 5 == 0) {
                mutations01.clear();
                mutations02.clear();
                mutator1.flush();
                mutator2.flush();
                System.out.println(mutations01.size() + "------------------------" + mutations02.size());
            }
        }
        mutator1.flush();
        mutator2.flush();
        long end = System.currentTimeMillis();
        System.out.println("ending 01 02 03------------------------------------");
        System.out.println("spend time: " + (end - start)*1.0/1000 + "  s");
        mutator1.close();
        mutator2.close();
        hBaseUtil.close();
    }
    //可以尝试另一种List 的Put方法。
    @Test
    public void testCreatePersonInfoAndEvalTableAndPerpareData() throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("starting------------------------------------");
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.master", "192.168.1.222:60000");
        configuration.set("hbase.zookeeper.quorum", "192.168.1.222");
        HBaseHelper hBaseUtil = HBaseHelper.getHBaseHelper(configuration);

        hBaseUtil.dropTable("ObjectInfo");
        boolean flag = hBaseUtil.exitTable("ObjectInfo");
        hBaseUtil.createTable("ObjectInfo", "pc");
        hBaseUtil.dropTable("EigenValue");
        hBaseUtil.createTable("EigenValue", "ec");

        HTable objectInfo = (HTable) hBaseUtil.getTable("ObjectInfo");
        HTable engenValue = (HTable) hBaseUtil.getTable("EigenValue");
        objectInfo.setWriteBufferSize(24*1024*1024);
        engenValue.setWriteBufferSize(24*1024*1024);
        List<Put> puts1 = new ArrayList<>();
        List<Put> puts2 = new ArrayList<>();
        byte[] pic = readPitureFromLocal("C:\\Users\\lenovo\\Desktop\\temp\\HBASECRUD.png");
        for (int i = 0; i < 5000; i++) {
            String idCard = getIdCardOrTKey(18);
            String tKey = getIdCardOrTKey(3);
            Put rowOfPersonInfo = new Put(Bytes.toBytes( idCard + tKey + "hz1" ))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("name"), Bytes.toBytes("nima-" + i))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("idcard"), Bytes.toBytes(idCard + i))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("sex"), Bytes.toBytes(new Random().nextInt(2)))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("reason"), Bytes.toBytes("reason-" + i))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("tkey"), Bytes.toBytes("tkey-" + i))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("photo"), pic)
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("warn"), Bytes.toBytes("warn-" + i))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("cman"), Bytes.toBytes("cman-" + i))
                    .addColumn(Bytes.toBytes("pc"), Bytes.toBytes("cmtel"), Bytes.toBytes("cmtel-" + i));
            puts1.add(rowOfPersonInfo);
            Put rowOfEigenValue = new Put(Bytes.toBytes("hz1" + idCard + tKey))
                    .addColumn(Bytes.toBytes("ec"), Bytes.toBytes("eval"), Bytes.toBytes("eval-" + i));
            puts2.add(rowOfEigenValue);
            if (i % 5 == 0) {
                objectInfo.put(puts1);
                engenValue.put(puts2);
                puts1.clear();
                puts2.clear();
                System.out.println(i + ":  " + puts1.size() + " ------------ " + puts2.size());
            }

        }
        if (puts1.size() > 0){
            objectInfo.put(puts1);
        }
        if (puts2.size() > 0){
            engenValue.put(puts2);
        }
        objectInfo.close();
        engenValue.close();
        long end = System.currentTimeMillis();
        System.out.println("ending------------------------------------");
        System.out.println("spend time: " + (end - start)*1.0/1000 + "  s");
        hBaseUtil.close();
    }


    @Test
    public void testDemooo() throws InterruptedException, IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.master", "192.168.59.131:60000");
        configuration.set("hbase.zookeeper.quorum", "192.168.59.131");
        HBaseHelper hBaseUtil = HBaseHelper.getHBaseHelper(configuration);
        hBaseUtil.dropTable("PersonInfo");
        hBaseUtil.createTable("PersonInfo", "pcl");
        hBaseUtil.dropTable("EigenValue");
        hBaseUtil.createTable("EigenValue", "ecl");

        HTable personInfo = (HTable) hBaseUtil.getTable("PersonInfo");
        HTable engenValue = (HTable) hBaseUtil.getTable("EigenValue");
        personInfo.setAutoFlush(false, true);
        personInfo.setWriteBufferSize(24*1024*1024);
        engenValue.setAutoFlush(false, true);
        engenValue.setWriteBufferSize(24*1024*1024);

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++){
            threads[i] = new Thread(new MyThread());
            threads[i].start();
        }
        for (int i = 0; i <threads.length; i++){
            threads[i].join();
        }
    }
}

class MyThread implements Runnable{
    PageHandler pageHandler = new PageHandler();
    @Override
    public void run() {
        try {
            pageHandler.testCreatePersonInfoAndEvalTableAndPerpareData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}