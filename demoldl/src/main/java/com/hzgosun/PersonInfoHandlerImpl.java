package com.hzgosun;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;

public class PersonInfoHandlerImpl implements PersonInfoHandler {

    public PersonInfoHandlerImpl(){}

    // 合处理身份证号的过程合并。
    public String createIdcard(){
        byte[] nums = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random random = new Random();
        String idcard = "";
        for (int i = 0; i < 18; i++){
            idcard += nums[random.nextInt(nums.length)];
        }
        return idcard;
    }

    @Override
    public int addPersonInfo(Map<String, String> person) {
        // 初始化参数
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.master", "192.168.59.131:60000");  // 这两个需要写在配置文件里面
        configuration.set("hbase.zookeeper.quorum", "192.168.59.131");
        HBaseHelper helper = null;
        Table personInfo = null;
        Table eigenValue = null;
        try {
            helper = HBaseHelper.getHBaseHelper(configuration);
            personInfo = helper.getTable("PersonInfo");
            eigenValue = helper.getTable("EigenValue");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对身份证号的特殊处理。
        // 如果是18位，如果是15位，如果没有身份证号
        // 可以把这一部分提取成一个函数，值留下逻辑上的东西。
        Set<String> keys = person.keySet();
        Iterator<String> iterator = keys.iterator();
        List<String> list = new ArrayList<>();
        String idcard = null;
        while (iterator.hasNext()){
            String key = iterator.next();
            list.add(key);
        }
        if (list.contains("idcard")){
            idcard = person.get("idcard");
            if (idcard.length() == 15){
                idcard += (new Random().nextInt(900) + 100);
            }
        } else {
            idcard = createIdcard();
        }
        person.put("idcard", idcard);

        String platformID = person.get("platformID");

        String row = idcard + platformID;
        Put putOfPersonInfo = new Put(Bytes.toBytes(row));
        Put putOfEigenValue = new Put(Bytes.toBytes(row));

        putOfEigenValue.addColumn(Bytes.toBytes("ecl"), Bytes.toBytes("eval"), Bytes.toBytes(person.get("eval")));
        for (String key: list){
            if (!"eval".equals(key)){
                putOfPersonInfo.addColumn(Bytes.toBytes("pcl"), Bytes.toBytes(key), Bytes.toBytes(person.get(key)));
            }
        }
        try {
            personInfo.put(putOfPersonInfo);
            eigenValue.put(putOfEigenValue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            personInfo.close();
            eigenValue.close();
            helper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int addPersonInfo( List<Map> persons) {
        return 0;
    }

    @Override
    public int deletPersonInfo(String Id) {
        return 0;
    }

    @Override
    public int updatePersonInfo(Map<String, String> person) {
        return 0;
    }

    @Override
    public List<Map> getPersonInfo( Map<String, String> rowClomn) {
        return null;
    }

    @Override
    public Map<String, String> gerPersonInfo(String platformId, String idcard) {
        return null;
    }
}
