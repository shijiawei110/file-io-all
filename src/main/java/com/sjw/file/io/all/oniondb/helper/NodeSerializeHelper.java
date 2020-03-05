package com.sjw.file.io.all.oniondb.helper;

import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import org.apache.commons.collections.MapUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author shijiawei
 * @version NodeSerializeHelper.java -> v 1.0
 * @date 2020/2/28
 * 节点序列化工具类
 * 文件kev节点协议的格式为  | key的长度(1字节) | key的字节数据包 | value的长度(4字节) | value的字节数据包 |
 */
public class NodeSerializeHelper {

    public static ByteBuffer serializeByteBuffer(Map<String, String> map, int size) {
        if (MapUtils.isEmpty(map)) {
            throw OnionDbException.DATA_NULL_ERROR;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        map.forEach((k, v) -> {
            buildByteBuffer(byteBuffer, k, v);
        });
        byteBuffer.flip();
        return byteBuffer;
    }

    /**
     * 计算单个节点的协议化字节数
     */
    public static int calNodeSize(String key, String value) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        return keyBytes.length + valueBytes.length + ParamConstans.KEY_FALG_BYTE_NUM + ParamConstans.VALUE_FALG_BYTE_NUM;
    }

    private static void buildByteBuffer(ByteBuffer byteBuffer, String key, String value) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        byte keySize = (byte) keyBytes.length;
        int valueSize = valueBytes.length;
        byteBuffer.put(keySize);
        byteBuffer.put(keyBytes);
        byteBuffer.putInt(valueSize);
        byteBuffer.put(valueBytes);
    }
}
