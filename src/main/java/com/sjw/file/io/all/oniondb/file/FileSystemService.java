package com.sjw.file.io.all.oniondb.file;

import com.sjw.file.io.all.oniondb.helper.FileHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void write(ByteBuffer byteBuffer) throws IOException {
        try {
            lock.writeLock().lock();
            fileChannel.sequenceWrite(FileHelper.getWriteFile(), byteBuffer);
        } finally {
            lock.writeLock().unlock();
        }
    }


}
