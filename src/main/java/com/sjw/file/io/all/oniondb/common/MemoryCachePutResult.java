package com.sjw.file.io.all.oniondb.common;

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

    //预留字段 -> 墓碑table todo

    public static MemoryCachePutResult success(int setNum) {
        return new MemoryCachePutResult(true, setNum, false, null, 0);
    }

    public static MemoryCachePutResult successAndFull(int setNum, Map<String, String> data, int fullDataSize) {
        return new MemoryCachePutResult(true, setNum, true, data, fullDataSize);
    }

    public static MemoryCachePutResult fail(int setNum) {
        return new MemoryCachePutResult(false, setNum, false, null, 0);
    }

    public static MemoryCachePutResult failAndFull(int setNum, Map<String, String> data, int fullDataSize) {
        return new MemoryCachePutResult(false, setNum, true, data, fullDataSize);
    }

    private MemoryCachePutResult(boolean success, int setNum, boolean isFull, Map<String, String> fullData, int fullDataSize) {
        this.success = success;
        this.setNum = setNum;
        this.isFull = isFull;
        this.fullData = fullData;
        this.fullDataSize = fullDataSize;
    }

    public boolean isFull() {
        return isFull;
    }
}
