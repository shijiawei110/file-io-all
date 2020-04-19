package com.sjw.file.io.all.oniondb.manager;


import com.google.common.collect.Maps;
import com.sjw.file.io.all.oniondb.helper.FileHelper;
import com.sjw.file.io.all.oniondb.index.DenseIndex;
import com.sjw.file.io.all.oniondb.index.OnionDbTableIndex;
import com.sjw.file.io.all.oniondb.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件个数以及目前序号的管理者
 */
@Slf4j
public class FilePositionManager {

    //todo 合并文件后 先不删除原来的文件保证服务正常运行，当file manager的索引修改到新文件后，再删除老的合并前的文件

    //桶位置
    private int position;
    //目前游标位置
    private volatile int currentIndex = 1;

    //索引记录的偏移量
    private volatile int cacheIndexOffset = 0;

    //各个index的索引cache
    private Map<String, OnionDbTableIndex> indexCaches = Maps.newHashMap();


    public void init(Integer position) {
        //todo 为了测试先删除所有磁盘数据
        FileHelper.clearFilesForTest();

        this.position = position;
        //获取文件夹路径
        String path = FileHelper.getCurrentFilePath(position);
        //读取所在position文件夹下的所有文件名
        List<String> fileNames = FileHelper.getAllFileName(path);
        //根据所有文件名 重新定位目前游标位置
        int indexMax = 0;
        if (CollectionUtils.isNotEmpty(fileNames)) {
            for (String fileName : fileNames) {
                int fileIndex = FileHelper.getIndexFromFileName(fileName);
                indexMax = Math.max(fileIndex, indexMax);
            }
        }
        currentIndex = indexMax + 1;
        //生成索引0位置文件的cache
        createNewIndexCache();
    }

    public File getCurrentFile() {
        return FileHelper.getCurrentFile(position, currentIndex);
    }

    public int getCurrentFileIndex() {
        return FileHelper.getCurrentFileIndex(position, currentIndex);
    }


    public OnionDbTableIndex getIndexCache() {
        return indexCaches.get(String.valueOf(currentIndex));
    }

    public OnionDbTableIndex getIndexCacheByKey(int fileIndex) {
        return indexCaches.get(String.valueOf(fileIndex));
    }

    public synchronized void batchSetIndex(Map<String, Integer> indexData, int indexByteSize) {
        //设置索引
        getIndexCache().batchSetIndex(indexData, cacheIndexOffset);
        //增加偏移量
        cacheIndexOffset += indexByteSize;
    }

    public File getFileByFileIndex(int fileIndex) {
        return FileHelper.getCurrentFile(position, fileIndex);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public synchronized void forwardIndex() {
        currentIndex++;
    }

    /**
     * 创建新的文件内存索引
     */
    public synchronized void createNewIndexCache() {
        if (null == indexCaches.get(String.valueOf(currentIndex))) {
            indexCaches.put(String.valueOf(currentIndex), new DenseIndex());
            //索引偏移量重置
            cacheIndexOffset = 0;
        }
    }

    /**
     * 执行文件合并
     */
    public synchronized void mergeFileAsync() {
        ThreadPoolUtil.instance().runTask(this::doMergeFileAsync);
    }

    private void doMergeFileAsync() {
        try {
            //执行合并过程
            //合并文件 -> 多个file 第一个node比较归并排序，比较用compareto
            //合并索引
        } catch (Exception e) {
            log.error("do merge file error", e);
        }
    }
}
