package com.sjw.file.io.all.oniondb.manager;

import com.google.common.collect.Maps;
import com.sjw.file.io.all.oniondb.common.OnionDbResult;
import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import com.sjw.file.io.all.oniondb.helper.HashHelper;
import com.sjw.file.io.all.oniondb.memory.MemoryCachePutResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * 主控制器
 */
@Component
@Slf4j
public class MainControllerManager {

    private Map<String, MainControllerHolder> mainControllerHolders;

    /**
     * 内存表初始化
     */
    @PostConstruct
    public void init() {
        //内存表
        mainControllerHolders = Maps.newHashMap();
        //init
        for (int i = 0; i < ParamConstans.HASH_SIZE; i++) {
            String position = String.valueOf(i);
            mainControllerHolders.put(position, new MainControllerHolder(position));
        }
    }

    public int doSet(String key, String value) throws IOException {
        //基础check -> null等
        checkParams(key, value);
        //value 序列化选择，默认直接String，其他可以用protostuff等替换
        String vStr = value.toString();
        //检查字段，是否超过最大的key的size或者value的size
        checkMaxLimit(key, vStr);
        //获取桶值
        String position = HashHelper.hashPosition(key);
//        log.info("onion db set action -> key = {} | value = {} | position = {}", key, value, position);
        MainControllerHolder holder = getHolder(position);
        try {
            //lock
            holder.wLock();
            //memoryTable put
            MemoryCachePutResult memoryCachePutResult = holder.memoryCachePut(key, vStr);
            if (memoryCachePutResult.isFull()) {
                log.info("onion db memory table full -> size = {} , position = {}", ParamConstans.MAX_NODE_SIZE, position);
                //执行数据入盘
                holder.fileWrite(memoryCachePutResult);
            }
            return memoryCachePutResult.getSetNum();
        } finally {
            //unlock
            holder.wUnLock();
        }
    }

    public OnionDbResult doGet(String key) throws IOException {
        checkParams(key);
        checkKeyMaxLimit(key);
        //获取桶值
        String position = HashHelper.hashPosition(key);
        MainControllerHolder holder = getHolder(position);
        try {
            //lock
            holder.rLock();
            //首先查memory table
            String memResult = holder.memoryCacheGet(key);
            if (null != memResult) {
                return OnionDbResult.successResult(memResult);
            }
            //查磁盘
            String dbResult = holder.fileGet(key);
            return OnionDbResult.successResult(dbResult);
        } finally {
            //unlock
            holder.rUnLock();
        }

    }

    private MainControllerHolder getHolder(String position) {
        return mainControllerHolders.get(position);
    }

    private void checkParams(String... params) {
        for (String str : params) {
            if (StringUtils.isBlank(str)) {
                throw OnionDbException.REQUEST_PARAM_ERROR;
            }
        }
    }

    private void checkMaxLimit(String key, String value) {
        if (key.length() >= ParamConstans.KEY_MAX_SIZE) {
            throw OnionDbException.OUT_OF_MAX_LENGTH;
        }
        if (value.length() >= ParamConstans.VALUE_MAX_SIZE) {
            throw OnionDbException.OUT_OF_MAX_LENGTH;
        }
    }

    private void checkKeyMaxLimit(String key) {
        if (key.length() >= ParamConstans.KEY_MAX_SIZE) {
            throw OnionDbException.OUT_OF_MAX_LENGTH;
        }
    }

}
