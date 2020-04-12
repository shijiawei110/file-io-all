package com.sjw.file.io.all.oniondb;

import com.sjw.file.io.all.oniondb.common.OnionDbResult;
import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import com.sjw.file.io.all.oniondb.helper.FileHelper;
import com.sjw.file.io.all.oniondb.manager.MainControllerManager;
import com.sjw.file.io.all.oniondb.request.BatchSetRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

    private MainControllerManager mainControllerManager;

    @PostConstruct
    public void init(){
        //todo 测试用 -> 每次启动数据库清空主目录下的所有文件
        log.info("---------------------- onion db app init ----------------------");
        FileHelper.clearFilesForTest();
    }

    @PreDestroy
    public void stop() {
        log.info("---------------------- onion db app stop ----------------------");
    }

    public OnionDbResult set(String key, String value) {
        try {
            int result = mainControllerManager.doSet(key, value);
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
        //最大批处理数目
        if (requests.size() >= ParamConstans.MAX_BATCH_SET_NUM) {
            return OnionDbResult.failResult(OnionDbException.OUT_OF_MAX_BATCH_SET_NUM);
        }
        try {
            for (BatchSetRequest batchSetRequest : requests) {
                int result = mainControllerManager.doSet(batchSetRequest.getKey(), batchSetRequest.getValue());
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

    public OnionDbResult get(String key) {
        try {
            return mainControllerManager.doGet(key);
        } catch (OnionDbException e) {
            log.info("onion db biz error -> key = {} , msg = {} , stack = {}", key, e.getMsg(), ExceptionUtils.getStackTrace(e));
            return OnionDbResult.failResult(e);
        } catch (Exception e) {
            log.error("onion db system error", e);
            return OnionDbResult.failSystemResult(e.getMessage());
        }

    }

    @Autowired
    public void setMainControllerManager(MainControllerManager mainControllerManager) {
        this.mainControllerManager = mainControllerManager;
    }

}
