package com.sjw.file.io.all.oniondb;

import com.sjw.file.io.all.oniondb.common.MemoryCachePutResult;
import com.sjw.file.io.all.oniondb.common.OnionDbResult;
import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import com.sjw.file.io.all.oniondb.file.FileSystemService;
import com.sjw.file.io.all.oniondb.helper.NodeSerializeHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

/**
 * @author shijiawei
 * @version OnionDbApp.java -> v 1.0
 * @date 2020/2/28
 * 洋葱db的接口主类
 */
@Slf4j
@Component
public class OnionDbApp {

    private MemoryCacheTable memoryCacheTable;

    private FileSystemService fileSystemService;

    public OnionDbResult set(String key, Object value) {
        try {
            //基础check -> null等
            checkRequestParams(key, value);
            //value 序列化选择，默认直接String，其他可以用protostuff等替换
            String vStr = value.toString();
            //检查字段，是否超过最大的key的size或者value的size
            checkMaxLimit(key, vStr);
            //memoryTable put
            MemoryCachePutResult memoryCachePutResult = memoryCacheTable.put(key, vStr);
            if (memoryCachePutResult.isFull()) {
                //协议序列化
                ByteBuffer byteBuffer = NodeSerializeHelper.serializeByteBuffer(memoryCachePutResult.getFullData(), memoryCachePutResult.getFullDataSize());
                //执行数据入磁盘
                fileSystemService.write(byteBuffer);
            }
            return OnionDbResult.successResult(memoryCachePutResult.getSetNum());
        } catch (OnionDbException e) {
            log.info("onion db biz error -> key = {} , msg = {} , stack = {}", key, e.getMsg(), ExceptionUtils.getStackTrace(e));
            return OnionDbResult.failResult(e);
        } catch (Exception e) {
            log.error("onion db system error", e);
            return OnionDbResult.failSystemResult(e.getMessage());
        }
    }


    private void checkRequestParams(String key, Object value) {
        if (StringUtils.isBlank(key) || null == value) {
            throw OnionDbException.REQUEST_PARAM_ERROR;
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

    @Autowired
    private void setMemoryCacheTable(MemoryCacheTable memoryCacheTable) {
        this.memoryCacheTable = memoryCacheTable;
    }

    @Autowired
    private FileSystemService getFileSystemService() {
        return fileSystemService;
    }
}
