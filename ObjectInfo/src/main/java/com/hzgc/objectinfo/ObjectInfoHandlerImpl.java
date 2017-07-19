package com.hzgc.objectinfo;

import com.hzgc.modle.ObjectSearchResult;

import java.util.List;
import java.util.Map;

public class ObjectInfoHandlerImpl implements ObjectInfoHandler {
    @Override
    public byte addObjectInfo(String platformId, Map<String, String> person) {
        return 0;
    }

    @Override
    public int deleteObjectInfo(String Id) {
        return 0;
    }

    @Override
    public int updateObjectInfo(Map<String, String> person) {
        return 0;
    }

    @Override
    public ObjectSearchResult getObjectInfo(String platformId, String id, byte[] image, int threshold, List<String> pkeys, Map<String, String> rowClomn, long start, long pageSize, int serachId) {
        return null;
    }

    @Override
    public ObjectSearchResult getObjectInfo(String id) {
        return null;
    }

    @Override
    public ObjectSearchResult getObjectInfo(String paltformId, String searchID) {
        return null;
    }

    @Override
    public byte[] getEigenValue(String tag, byte[] photo) {
        return new byte[0];
    }
}
