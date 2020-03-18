package com.sjw.file.io.all.oniondb.index;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shijiawei
 * @version DenseIndex.java -> v 1.0
 * @date 2020/3/6
 * 密集索引 ：每个kv都创建内存索引
 */
@Slf4j
@Component
public class DenseIndex implements OnionDbTableIndex {

    private Map<String, Integer> indexMap = Maps.newHashMap();

    @Override
    public void createIndex(String key, int offset) {
        indexMap.put(key, offset);
    }

    public void setIndex(Map<String, Integer> map) {
        indexMap = map;
    }
}
