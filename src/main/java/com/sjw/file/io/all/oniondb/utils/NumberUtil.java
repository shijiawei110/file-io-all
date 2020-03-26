package com.sjw.file.io.all.oniondb.utils;

/**
 * @author shijiawei
 * @version NumberUtil.java -> v 1.0
 * @date 2020/3/18
 */
public class NumberUtil {

    public static boolean validNumber(Integer v) {
        return null != v && v >= 0;
    }

    public static boolean invalidNumber(Integer v) {
        return null == v || v < 0;
    }

    public static boolean validNumber(Long v) {
        return null != v && v >= 0;
    }
}
