package com.sjw.file.io.all.oniondb.pojo;

import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author shijiawei
 * @version DbNodePojo.java -> v 1.0
 * @date 2020/3/18
 * 序列化协议的pojo类
 * 文件kev节点协议的格式为  | key的长度(1字节) | key的字节数据包 | value的长度(4字节) | value的字节数据包 |
 */
@Data
@Slf4j
public class DbNodePojo {
    private int keySize;
    private byte[] keyData;
    private int valueSize;
    private byte[] valueData;

    public DbNodePojo(int keySize, byte[] keyData, int valueSize, byte[] valueData) {
        this.keySize = keySize;
        this.keyData = keyData;
        this.valueSize = valueSize;
        this.valueData = valueData;
    }

    /**
     * 检查key是否和请求的key一致
     */
    public void checkKey(String key) {
        String dbKey = new String(keyData, StandardCharsets.UTF_8);
        if (StringUtils.isBlank(dbKey)) {
            throw OnionDbException.DB_KEY_BLANK;
        }
        if (!key.equals(dbKey)) {
            log.info("check key not equals to db -> key = {} , dbkey = {}", key, dbKey);
            throw OnionDbException.DB_KEY_DIFF_TO_REQUEST_KEY;
        }
    }

    public String getValue() {
        return new String(valueData, StandardCharsets.UTF_8);
    }

}
