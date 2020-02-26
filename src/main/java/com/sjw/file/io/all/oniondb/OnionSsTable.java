package com.sjw.file.io.all.oniondb;

import lombok.Data;

/**
 * @author shijiawei
 * @version OnionSsTable.java -> v 1.0
 * @date 2020/2/26
 * onion存储引擎的基本组成单位，有序 顺序写文件
 */
@Data
public class OnionSsTable {
    /**
     * 最大文件大小
     **/
    public static final int MAX_FILE_SIZE = 100;


    private int currentSize = 0;
}
