package com.sjw.file.io.all.oniondb.memory;

import lombok.Data;

import java.util.Map;

/**
 * @author shijiawei
 * @version MemoryCachePutResult.java -> v 1.0
 * @date 2020/3/4
 * 内存表put返回结果
 */
@Data
public class MemoryCachePutResult {

    private boolean success;

    private int setNum;

    private boolean isFull;

    /**
     * 内存表满后的全量搬迁数据
     */
    private Map<String, String> fullData;
    /**
     * 数据size
     */
    private int fullDataSize;

    /**
     * 索引数据
     */
    private Map<String, Integer> indexData;

    //预留字段 -> 墓碑table todo

    public static MemoryCachePutResult success(int setNum) {
        return new MemoryCachePutResult(true, setNum, false, null, 0, null);
    }

    public static MemoryCachePutResult successAndFull(int setNum, Map<String, String> data, int fullDataSize, Map<String, Integer> indexData) {
        return new MemoryCachePutResult(true, setNum, true, data, fullDataSize, indexData);
    }

    private MemoryCachePutResult(boolean success, int setNum, boolean isFull, Map<String, String> fullData, int fullDataSize, Map<String, Integer> indexData) {
        this.success = success;
        this.setNum = setNum;
        this.isFull = isFull;
        this.fullData = fullData;
        this.fullDataSize = fullDataSize;
        this.indexData = indexData;
    }

    public boolean isFull() {
        return isFull;
    }
}
