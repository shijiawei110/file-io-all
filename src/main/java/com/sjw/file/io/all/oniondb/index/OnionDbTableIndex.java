package com.sjw.file.io.all.oniondb.index;

/**
 * @author shijiawei
 * @version OnionDbTableIndex.java -> v 1.0
 * @date 2020/3/6
 * 磁盘log文件的索引api ，目前想到的索引有 1：稀疏索引  2：密集索引 3：磁盘索引
 * 磁盘索引详见 sstable的 index_block设计 ：https://blog.csdn.net/ws1296931325/article/details/86635751
 */
public interface OnionDbTableIndex {
    /**
     * 创建索引
     * @param key -> 键值
     * @param offset -> 偏移量
     */
    void createIndex(String key, int offset);
}
