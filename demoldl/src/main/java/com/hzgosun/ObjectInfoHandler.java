package com.hzgosun;

import java.util.List;
import java.util.Map;

public interface ObjectInfoHandler {

    /**
     * 针对单个对象信息的添加处理
     * @param  platformId 表示的是平台的ID， 平台的唯一标识。
     * @param person K-V 对，里面存放的是字段和值之间的一一对应关系,
     *               例如：传入一个Map 里面的值如下map.put("idcard", "450722199502196939")
     *               表示的是身份证号（idcard）是450722199502196939，其中的K 的具体，请参考给出的数据库字段设计
     * @return 返回值为0，表示插入成功，返回值为1，表示插入失败
     */
    public byte addObjectInfo(String platformId, Map<String, String>person);

    /**
     * 删除对象的信息
     * @param Id 具体的一个人员信息的ID，值唯一
     * @return 返回值为0，表示删除成功，返回值为1，表示删除失败
     */
    public int deleteObjectInfo(String Id);

    /**
     * 修改对象的信息
     * @param person K-V 对，里面存放的是字段和值之间的一一对应关系，参考添加里的描述
     * @return返回值为0，表示更新成功，返回值为1，表示更新失败
     */
    public int updateObjectInfo(Map<String, String> person);

    /**
     * 可以匹配精确查找，以图搜索人员信息，模糊查找，关键看输入的是什么内容。
     * 如果输入只有一个kv 对，且K 的内容id，则是根据ID 进行查找
     * 如果输入只有一个kv 对，且K 的内容是身份证号,则根据身份证号模糊查找
     * 如果输入的是一些列的KV 对，则进行模糊查找
     * @param platformId 对应的是平台的ID
     * @param id 对应的是一个人在HBase 数据库中的唯一标志
     * @param image  传过来的图片
     * @param threshold  图片比对的阈值
     * @param pkeys 人员类型列表
     * @param rowClomn 一些列的KV 对，即查找的条件
     * @param start 需要返回的起始行
     * @param pageSize 需要返回的每页的大小
     * @param serachId 搜索Id
     * @return 返回一个ObjectSearchResult 对象，里面包含了本次查询ID，查询成功标识，查询照片ID（无照片，此参数为空），结果数，人员信息列表
     */
    public ObjectSearchResult getObjectInfo(String platformId, String id, byte[] image, int threshold, List<String> pkeys,
                                            Map<String, String> rowClomn, long start, long pageSize, int serachId);

    /**
     * 可以只传入一个精确的id ，或者身份证号进行查找，（单个人员的查找）
     * @param id  精确的rowkey 或者是身份证id
     * @return  返回一个ObjectSearchResult 对象，里面包含了本次查询ID，查询成功标识，查询照片ID（无照片，此参数为空），结果数，人员信息列表
     */
    public ObjectSearchResult getObjectInfo(String id);


    /**
     * 根据搜索Id 重新搜索一遍
     * @param paltformId 平台的唯一标识
     * @param searchID  搜索ID，即图片的ID，
     * @return
     */
    public ObjectSearchResult getObjectInfo(String paltformId, String searchID);


    // rowkey 的设计： 身份证号+平台id+人员类型Key+ 性别(会超过16个字节，理论上rowkeys 的设计越短越好)
    // row
}
