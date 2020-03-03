package com.sjw.file.io.all.oniondb.helper;

import com.sjw.file.io.all.oniondb.common.ParamConstans;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author shijiawei
 * @version NodeSerializeHelper.java -> v 1.0
 * @date 2020/2/28
 * 节点序列化工具类
 * 文件kev节点协议的格式为  | key的长度(1字节) | key的字节数据包 | value的长度(4字节) | value的字节数据包 |
 */
public class NodeSerializeHelper {

    public static ByteBuffer serializeByteBuffer(String key, String value) {
        return buildByteBuffer(key, value);
    }

    public static byte[] serializeBytes(String key, String value) {
        ByteBuffer byteBuffer = buildByteBuffer(key, value);
        return byteBuffer.array();
    }

    private static ByteBuffer buildByteBuffer(String key, String value) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        byte keySize = (byte) keyBytes.length;
        int valueSize = valueBytes.length;
        int size = keyBytes.length + valueBytes.length + ParamConstans.KEY_FALG_BYTE_NUM + ParamConstans.VALUE_FALG_BYTE_NUM;
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        byteBuffer.put(keySize);
        byteBuffer.put(keyBytes);
        byteBuffer.putInt(valueSize);
        byteBuffer.put(valueBytes);
        byteBuffer.flip();
        return byteBuffer;
    }
}
