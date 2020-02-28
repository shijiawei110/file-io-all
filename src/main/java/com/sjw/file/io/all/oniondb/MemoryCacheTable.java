package com.sjw.file.io.all.oniondb;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.TreeMap;

/**
 * @author shijiawei
 * @version MemCache.java -> v 1.0
 * @date 2020/2/27
 * 前置内存表，用完内存表之后才会存入磁盘表
 */
public class MemoryCacheTable {

    private TreeMap<String, String> tree = Maps.newTreeMap();

    /**
     * 单个put
     */
    public int put(String k, String v) {
        if (StringUtils.isBlank(k) || StringUtils.isBlank(v)) {
            return 0;
        }
        tree.put(k, v);
        return 1;
    }


    public int size() {
        return tree.size();
    }
}
