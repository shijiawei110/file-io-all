package com.sjw.file.io.all.oniondb.helper;

import java.nio.charset.StandardCharsets;

/**
 * @author shijiawei
 * @version NodeSerializeHelper.java -> v 1.0
 * @date 2020/2/28
 * 节点序列化工具类
 * 文件kev节点协议的格式为  | key的长度(1字节) | key的字节数据包 | value的长度(4字节) | value的字节数据包 |
 */
public class NodeSerializeHelper {

    public static byte[] serialize(String key, String value) {
        byte[] keyBytes = key.getBytes();
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        byte keySize = (byte) keyBytes.length;
        int valueSize = valueBytes.length;
        return null;
    }
}
