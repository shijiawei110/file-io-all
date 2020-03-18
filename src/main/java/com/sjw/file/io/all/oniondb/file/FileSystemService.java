package com.sjw.file.io.all.oniondb.file;

import com.sjw.file.io.all.oniondb.helper.FileHelper;
import com.sjw.file.io.all.oniondb.helper.NodeSerializeHelper;
import com.sjw.file.io.all.oniondb.index.DenseIndex;
import com.sjw.file.io.all.oniondb.memory.MemoryCachePutResult;
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

    @Resource
    private DenseIndex denseIndex;


    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void write(MemoryCachePutResult memoryCachePutResult) throws IOException {
        try {
            //协议序列化
            ByteBuffer byteBuffer = NodeSerializeHelper.serializeByteBuffer(memoryCachePutResult.getFullData(), memoryCachePutResult.getFullDataSize());
            lock.writeLock().lock();
            //写入磁盘
            fileChannel.sequenceWrite(FileHelper.getWriteFile(), byteBuffer);
            //写入索引
            denseIndex.setIndex(memoryCachePutResult.getIndexData());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String get(String key) {
        try {
            //协议序列化
            ByteBuffer byteBuffer = NodeSerializeHelper.serializeByteBuffer(memoryCachePutResult.getFullData(), memoryCachePutResult.getFullDataSize());
            lock.writeLock().lock();
            //写入磁盘
            fileChannel.sequenceWrite(FileHelper.getWriteFile(), byteBuffer);
            //写入索引
            denseIndex.setIndex(memoryCachePutResult.getIndexData());
        } finally {
            lock.writeLock().unlock();
        }
    }


}
