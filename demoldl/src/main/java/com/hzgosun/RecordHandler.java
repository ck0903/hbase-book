package com.hzgosun;

public interface RecordHandler {
    /**
     * id 代表的是查询的记录在Hbase 数据库中的id ，与图片在Hbase 数据库中的id 一致。
     * @return  返回一个ObjectSearchResult 对象，里面包含了本次查询ID，查询成功标识，查询照片ID（无照片，此参数为空），结果数，人员信息列表
     */
    public ObjectSearchResult getObjectInfo(String id);
}
