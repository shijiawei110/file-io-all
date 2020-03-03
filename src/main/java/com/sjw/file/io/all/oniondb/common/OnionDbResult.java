package com.sjw.file.io.all.oniondb.common;

import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import lombok.Data;

/**
 * @author shijiawei
 * @version OnionDbResult.java -> v 1.0
 * @date 2020/3/3
 * 数据库返回参数
 */
@Data
public class OnionDbResult {

    private boolean success;
    private Object value;
    private Integer code;
    private String message;

    public static OnionDbResult successResult(Object value) {
        return new OnionDbResult(true, value, null, null);
    }

    public static OnionDbResult failResult(Integer code, String message) {
        return new OnionDbResult(false, null, code, message);
    }

    public static OnionDbResult failSystemResult(String message) {
        return new OnionDbResult(false, null, 99999, message);
    }

    public static OnionDbResult failResult(OnionDbException e) {
        return new OnionDbResult(false, null, e.getCode(), e.getMessage());
    }

    private OnionDbResult(boolean success, Object value, Integer code, String message) {
        this.success = success;
        this.value = value;
        this.code = code;
        this.message = message;
    }


}
