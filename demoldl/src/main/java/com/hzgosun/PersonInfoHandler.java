package com.hzgosun;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PersonInfoHandler {
    String ADD_STATUS_SUCCESS  = "0";
    String ADD_STATUS_FAILED = "1";

    /**
     * 针对单个person 的添加处理
     * @param person K-V 对，里面存放的是字段和值之间的一一对应关系
     * @return 返回值为0，表示插入成功，返回值为1，表示插入失败
     */
    public int addPersonInfo(Map<String, String> person);

    /**
     * 针对过个person 的批量插入处理????????????????????????????????????????????????????????????????????????????? 应用层的批量操作不会调用这个接口，而是调用单个插入的接口，而数据行数多，的情况下，调用单个的接口是不理智的。
     * @param persons 一些列的Map, 每个Map 对应着一条人员的信息，Map 里面存放的K-V 对，对应着字段和值
     * @return 返回值为0，表示插入成功，返回值为1，表示插入失败
     */
    public int addPersonInfo(List<Map> persons);

    /**
     * 传入一个平台ID，和具体的人员信息ID，进行删除这一条信息。
     * @param Id 具体的一个人员信息的ID，值唯一
     * @return 返回值为0，表示删除成功，返回值为1，表示删除失败
     */
    public int deletPersonInfo(String Id);

    /**
     * 传入平台ID，和具体人员的id, 以及要修改的信息
     * @param person K-V 对，里面存放的是字段和值之间的一一对应关系
     * @return 返回值为0，表示更新成功，返回值为1，表示更新失败
     */
    public int updatePersonInfo(Map<String, String> person);

    /**
     *
     * 可以匹配精确查找，或者以图搜索人员信息，模糊查找，关键看输入的是什么内容。
     * 如果输入只有一个kv 对，且K 的内容id，则是根据ID 进行查找
     * 如果输入只有一个kv 对，且K 的内容是身份证号,则根据身份证号模糊查找
     * 如果输入的是一些列的KV 对，则进行模糊查找
     * @param rowClomn 一些列的KV 对，即查找的条件
     * @return 返回查找到的人员信息，或者为空
     */
    public List<Map> getPersonInfo(Map<String, String> rowClomn);

    /**
     * 针对平台ID + 身份证号的精确查找接口
     * @param platformId ，平台ID，
     * @param idcard , 身份证号
     * @return  单个人员的信息，用Map 来封装，K 代表字段，V 代表值
     */
    public Map<String, String> gerPersonInfo(String platformId, String idcard);
}
