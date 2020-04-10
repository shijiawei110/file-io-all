package com.sjw.file.io.all.oniondb.manager;


import com.google.common.collect.Maps;
import com.sjw.file.io.all.oniondb.helper.FileHelper;
import com.sjw.file.io.all.oniondb.index.DenseIndex;
import com.sjw.file.io.all.oniondb.index.OnionDbTableIndex;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件个数以及目前序号的管理者
 */
public class FilePositionManager {

    //todo 合并文件后 先不删除原来的文件保证服务正常运行，当file manager的索引修改到新文件后，再删除老的合并前的文件

    private int position;
    //目前游标位置
    private volatile int currentIndex = 1;

    //各个index的索引cache
    private Map<String, OnionDbTableIndex> indexCaches = Maps.newHashMap();


    public void init(Integer position) {
        this.position = position;
        //获取文件夹路径
        String path = FileHelper.getCurrentFilePath(position);
        //读取所在position文件夹下的所有文件名
        List<String> fileNames = FileHelper.getAllFileName(path);
        //根据所有文件名 重新定位目前游标位置
        if (CollectionUtils.isEmpty(fileNames)) {
            return;
        }
        int indexMax = 0;
        for (String fileName : fileNames) {
            int fileIndex = FileHelper.getIndexFromFileName(fileName);
            indexMax = Math.max(fileIndex, indexMax);
        }
        currentIndex = indexMax + 1;
        //生成索引0位置文件的cache
        createNewIndexCache();
        //todo 测试用 -> 每次启动数据库清空主目录下的所有文件
        clearFilesForTest(FileHelper.getMainFile());
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

    public File getFileByFileIndex(int fileIndex) {
        return FileHelper.getCurrentFile(position, fileIndex);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public synchronized void forwardIndex() {
        currentIndex++;
    }

    public synchronized void createNewIndexCache() {
        if (null == indexCaches.get(String.valueOf(currentIndex))) {
            indexCaches.put(String.valueOf(currentIndex), new DenseIndex());
        }
    }

    private void clearFilesForTest(File file) {
        File[] files = file.listFiles();
        if (null == files || files.length <= 0) {
            return;
        }
        //遍历该目录下的文件对象
        for (File f : files) {
            //打印文件名
            String name = file.getName();
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                clearFilesForTest(f);
            } else {
                f.delete();
            }
        }
    }
}
