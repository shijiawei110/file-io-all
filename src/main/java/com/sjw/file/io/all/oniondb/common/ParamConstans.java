package com.sjw.file.io.all.oniondb.common;

/**
 * @author shijiawei
 * @version ParamConstans.java -> v 1.0
 * @date 2020/2/27
 * 可变的参数配置
 */
public class ParamConstans {
    /**
     * 内存表最大的节点个数
     **/
    public static final int MAX_NODE_SIZE = 1000;

    /**
     * key长度的占位符的字节数
     */
    public static final int KEY_FALG_BYTE_NUM = 1;

    /**
     * key最大长度
     */
    public static final int KEY_MAX_SIZE = Byte.MAX_VALUE;

    /**
     * value长度的占位符的字节数 -> 4字节int
     */
    public static final int VALUE_FALG_BYTE_NUM = 4;

    /**
     * value最大长度
     */
    public static final int VALUE_MAX_SIZE = Integer.MAX_VALUE;

    /**
     * 最大批set数量
     */
    public static final int MAX_BATCH_SET_NUM = 100;

    /**
     * 最大批get数量
     */
    public static final int MAX_BATCH_GET_NUM = 100;

    /**
     * hash桶数量
     */
    public static final int HASH_SIZE = 4;

    /**
     * 单个文件的最大字节数
     */
    public static final int DB_TABLE_FILE_MAX_BYTE_SIZE = 500;

}
