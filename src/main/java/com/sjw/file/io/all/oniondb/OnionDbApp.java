package com.sjw.file.io.all.oniondb;

import com.sjw.file.io.all.oniondb.common.OnionDbResult;
import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shijiawei
 * @version OnionDbApp.java -> v 1.0
 * @date 2020/2/28
 * 洋葱db的接口主类
 */
@Slf4j
public class OnionDbApp {

    public OnionDbResult set(String key, Object value) {
        try {
            //基础check -> null等

            //首先要检查字段，是否超过最大的key的size或者value的size

            //value 序列化选择，默认直接String，其他可以用protostuff等替换
            String vStr = value.toString();
        } catch (OnionDbException e) {
            return OnionDbResult.failResult(e);
        } catch (Exception e) {
            log.error("onion db system error", e);
            return OnionDbResult.failSystemResult(e.getMessage());
        }
        return null;
    }

    private void checkRequestParams(String key, Object value) {
        if (StringUtils.isBlank(key) || null == value) {
            throw OnionDbException.REQUEST_PARAM_ERROR;
        }
    }

}
