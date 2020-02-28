package com.sjw.file.io.all.oniondb;

/**
 * @author shijiawei
 * @version OnionDbApp.java -> v 1.0
 * @date 2020/2/28
 * 洋葱db的接口主类
 */
public class OnionDbApp {

    public String set(String key, Object value) {
        //基础check -> null等

        //首先要检查字段，是否超过最大的key的size或者value的size

        //value 序列化选择，默认直接String，其他可以用protostuff等替换
        String vStr = value.toString();
        return null;
    }
}
