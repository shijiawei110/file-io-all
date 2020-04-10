package com.sjw.file.io.all.oniondb.index;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author shijiawei
 * @version DenseIndex.java -> v 1.0
 * @date 2020/3/6
 * 密集索引 ：每个kv都创建内存索引
 */
@Slf4j
public class DenseIndex implements OnionDbTableIndex {

    @Override
    public void setIndex(String key, int offset) {
        indexMap.put(key, offset);
    }

    @Override
    public void batchSetIndex(Map<String, Integer> indexs) {
        indexMap.putAll(indexs);
    }

    @Override
    public Integer getIndex(String key) {
        return indexMap.get(key);
    }
}
