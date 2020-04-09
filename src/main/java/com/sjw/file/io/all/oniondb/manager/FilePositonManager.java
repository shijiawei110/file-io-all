package com.sjw.file.io.all.oniondb.manager;


import com.sjw.file.io.all.oniondb.helper.FileHelper;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 文件个数以及目前序号的管理者
 */
public class FilePositonManager {

    //目前游标位置
    private int currentIndex = 1;

    public void init(Integer position) {
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
    }


    public int getCurrentIndex() {
        return currentIndex;
    }
}
