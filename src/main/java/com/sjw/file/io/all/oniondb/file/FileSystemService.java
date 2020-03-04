package com.sjw.file.io.all.oniondb.file;

import com.sjw.file.io.all.futil.FileChannelUtil;
import com.sjw.file.io.all.futil.FileStandardUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shijiawei
 * @version FileSystemUtil.java -> v 1.0
 * @date 2020/2/28
 * 文件系统工具
 */
@Service
public class FileSystemService {


    @Resource
    private FileChannelImpl fileChannel;


    //todo  写需要加锁
    public synchronized void write(byte[] bytes) {
        if (bytes.length <= 0) {
            return;
        }
    }


}
