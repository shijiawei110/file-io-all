package com.sjw.file.io.all.oniondb.file;

import com.sjw.file.io.all.futil.FileChannelUtil;
import com.sjw.file.io.all.futil.FileStandardUtil;

/**
 * @author shijiawei
 * @version FileSystemUtil.java -> v 1.0
 * @date 2020/2/28
 * 文件系统工具
 */
public class FileSystemUtil {

    public static FileSystemUtil instance = new FileSystemUtil();


    private final FileChannelImpl FILE_STANDARD_UTIL = FileChannelImpl.instance;


    //todo  写需要加锁
    public synchronized void write(byte[] bytes) {
        if (bytes.length <= 0) {
            return;
        }
    }


}
