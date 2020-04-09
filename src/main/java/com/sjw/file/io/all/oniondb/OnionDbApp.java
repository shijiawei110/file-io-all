package com.sjw.file.io.all.oniondb;

import com.sjw.file.io.all.oniondb.common.OnionDbResult;
import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.common.PositionManager;
import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import com.sjw.file.io.all.oniondb.file.FileSystemTable;
import com.sjw.file.io.all.oniondb.helper.HashHelper;
import com.sjw.file.io.all.oniondb.memory.MemoryCachePutResult;
import com.sjw.file.io.all.oniondb.request.BatchSetRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author shijiawei
 * @version OnionDbApp.java -> v 1.0
 * @date 2020/2/28
 * 洋葱db的接口主类
 */
@Slf4j
@Component
public class OnionDbApp {

    private PositionManager positionManager;

    public OnionDbResult set(String key, String value) {
        try {
            int result = doSet(key, value);
            return OnionDbResult.successResult(result);
        } catch (OnionDbException e) {
            log.info("onion db set biz error -> key = {} , msg = {} , stack = {}", key, e.getMsg(), ExceptionUtils.getStackTrace(e));
            return OnionDbResult.failResult(e);
        } catch (Exception e) {
            log.error("onion db system error", e);
            return OnionDbResult.failSystemResult(e.getMessage());
        }
    }

    public OnionDbResult batchSet(List<BatchSetRequest> requests) {
        int resultCount = 0;
        if (CollectionUtils.isEmpty(requests)) {
            return OnionDbResult.successResult(resultCount);
        }
        try {
            for (BatchSetRequest batchSetRequest : requests) {
                int result = doSet(batchSetRequest.getKey(), batchSetRequest.getValue());
                resultCount += result;
            }
            return OnionDbResult.successResult(resultCount);
        } catch (OnionDbException e) {
            log.info("onion db batch set biz error -> msg = {} , stack = {}", e.getMsg(), ExceptionUtils.getStackTrace(e));
            return OnionDbResult.failResult(e);
        } catch (Exception e) {
            log.error("onion db system error", e);
            return OnionDbResult.failSystemResult(e.getMessage());
        }
    }


    private int doSet(String key, String value) throws IOException {
        //基础check -> null等
        checkParams(key, value);
        log.info("onion db set action -> key = {} | value = {}", key, value);
        //value 序列化选择，默认直接String，其他可以用protostuff等替换
        String vStr = value.toString();
        //检查字段，是否超过最大的key的size或者value的size
        checkMaxLimit(key, vStr);
        //获取桶值
        int position = HashHelper.hashPosition(key);
        //memoryTable put
        MemoryCachePutResult memoryCachePutResult = positionManager.getMemoryCacheTable(position).put(key, vStr);
        if (memoryCachePutResult.isFull()) {
            log.info("onion db memory table full -> size = {}", ParamConstans.MAX_NODE_SIZE);
            //执行数据入盘
            positionManager.getFileSystemTable(position).write(memoryCachePutResult);
        }
        return memoryCachePutResult.getSetNum();
    }

    public OnionDbResult get(String key) {
        try {
            checkParams(key);
            checkKeyMaxLimit(key);
            //获取桶值
            int position = HashHelper.hashPosition(key);
            //首先查memory table
            String memResult = positionManager.getMemoryCacheTable(position).get(key);
            if (null != memResult) {
                return OnionDbResult.successResult(memResult);
            }
            //查磁盘
            String dbResult = positionManager.getFileSystemTable(position).get(key);
            return OnionDbResult.successResult(dbResult);
        } catch (OnionDbException e) {
            log.info("onion db biz error -> key = {} , msg = {} , stack = {}", key, e.getMsg(), ExceptionUtils.getStackTrace(e));
            return OnionDbResult.failResult(e);
        } catch (Exception e) {
            log.error("onion db system error", e);
            return OnionDbResult.failSystemResult(e.getMessage());
        }

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

    @Autowired
    public void setPositionManager(PositionManager positionManager) {
        this.positionManager = positionManager;
    }

}
