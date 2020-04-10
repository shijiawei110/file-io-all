package com.sjw.file.io.all.oniondb.memory;

import com.google.common.collect.Maps;
import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.helper.NodeSerializeHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author shijiawei
 * @version MemCache.java -> v 1.0
 * @date 2020/2/27
 * 前置内存表，用完内存表之后才会存入磁盘表
 */
@Slf4j
public class MemoryCacheTable {

    private TreeMap<String, String> tree = Maps.newTreeMap();
    //todo  需要维护一个墓碑表，表示一个数据被删除

    /**
     * 单个put
     */
    public MemoryCachePutResult put(String k, String v) {
        tree.put(k, v);
        //检测是否full
        if (isFull()) {
            //reset and get map
            return reset();
        } else {
            return MemoryCachePutResult.success(1);
        }
    }

    public String get(String key) {
        return tree.get(key);
    }

    /**
     * 是否超过最大节点个数
     */
    private boolean isFull() {
        return tree.size() >= ParamConstans.MAX_NODE_SIZE;
    }

    /**
     * 重置内存树
     */
    private MemoryCachePutResult reset() {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        Map<String, Integer> indexData = Maps.newLinkedHashMap();
        int dataSize = 0;
        //提前计算总size和offset方便后面放内存索引
        for (Map.Entry<String, String> entry : tree.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            int size = NodeSerializeHelper.calNodeSize(k, v);
            dataMap.put(k, v);
            indexData.put(k, dataSize);
            dataSize += size;
        }
        clear();
        return MemoryCachePutResult.successAndFull(1, dataMap, dataSize, indexData);
    }

    private void clear() {
        if (!tree.isEmpty()) {
            tree.clear();
        }
    }
}
