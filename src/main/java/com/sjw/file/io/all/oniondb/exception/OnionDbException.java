package com.sjw.file.io.all.oniondb.exception;

import java.text.MessageFormat;

/**
 * @author shijw
 * @version OnionDbException.java, v 0.1 2018-10-07 20:05 shijw
 */
public class OnionDbException extends RuntimeException {

    public static final OnionDbException REQUEST_PARAM_ERROR= new OnionDbException(10001, "入参信息错误");
    public static final OnionDbException OUT_OF_MAX_LENGTH= new OnionDbException(10002, "超过最大存储长度");
    public static final OnionDbException DATA_NULL_ERROR= new OnionDbException(10003, "数据为空");


    /**
     * 异常信息
     */
    private String msg;

    /**
     * 具体异常码
     */
    private int code;

    /**
     * 异常构造器
     *
     * @param code      代码
     * @param msgFormat 消息模板,内部会用MessageFormat拼接，模板类似：userid={0},message={1},date{2}
     * @param args      具体参数的值
     */
    private OnionDbException(int code, String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.code = code;
        this.msg = MessageFormat.format(msgFormat, args);
    }

    /**
     * 默认构造器
     */
    private OnionDbException() {
        super();
    }

    /**
     * 异常构造器
     *
     * @param message
     * @param cause
     */
    private OnionDbException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 异常构造器
     *
     * @param cause
     */
    private OnionDbException(Throwable cause) {
        super(cause);
    }

    /**
     * 异常构造器
     *
     * @param message
     */
    private OnionDbException(String message) {
        super(message);
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
