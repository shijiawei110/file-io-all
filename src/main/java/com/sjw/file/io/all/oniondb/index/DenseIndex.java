package com.sjw.file.io.all.oniondb.index;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author shijiawei
 * @version DenseIndex.java -> v 1.0
 * @date 2020/3/6
 * 密集索引 ：每个kv都创建内存索引
 */
public class DenseIndex implements OnionDbTableIndex {

    Map<String, Integer> indexMap = Maps.newHashMap();

    @Override
    public void batchSetIndex(Map<String, Integer> indexs, Integer offset) {
        indexs.forEach((k, v) -> {
            int finalOffset = v + offset;
            indexMap.put(k, finalOffset);
        });
    }

    @Override
    public Integer getIndex(String key) {
        return indexMap.get(key);
    }
}
