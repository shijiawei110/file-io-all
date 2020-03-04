package com.sjw.file.io.all.oniondb;

import com.google.common.collect.Maps;
import com.sjw.file.io.all.oniondb.common.MemoryCachePutResult;
import com.sjw.file.io.all.oniondb.common.ParamConstans;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author shijiawei
 * @version MemCache.java -> v 1.0
 * @date 2020/2/27
 * 前置内存表，用完内存表之后才会存入磁盘表
 */
@Component
public class MemoryCacheTable {

    private TreeMap<String, String> tree = Maps.newTreeMap();

    /**
     * 读写锁控制 (默认非公平锁)
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    //todo  需要维护一个墓碑表，表示一个数据被删除

    /**
     * 单个put
     */
    MemoryCachePutResult put(String k, String v) {
        try {
            lock.writeLock().lock();
            tree.put(k, v);
            //检测是否full
            if (isFull()) {
                //reset and get map
                Map<String, String> map = reset();
                return MemoryCachePutResult.successAndFull(1, map);
            } else {
                return MemoryCachePutResult.success(1);
            }
        } finally {
            lock.writeLock().unlock();
        }
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
    public Map<String, String> reset() {
        Map<String, String> map = Maps.newHashMap();
        tree.forEach(map::put);
        clear();
        return map;
    }

    private void clear() {
        if (!tree.isEmpty()) {
            tree.clear();
        }
    }
}
