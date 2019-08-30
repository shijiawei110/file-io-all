package com.sjw.file.io.all.futil;

import com.sjw.file.io.all.helper.LogHelper;
import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shijiawei
 * @version FileChannelUtil.java -> v 1.0
 * @date 2019/6/5
 * filechannel util工具
 */
@Slf4j
public class FileChannelUtil implements FileStandardUtil {

    public static FileChannelUtil instance = new FileChannelUtil();

    @Override
    public void sequenceWrite(File file, byte[] bytes) throws IOException {
        LogHelper.logTag("FileChannel顺序写", "start", file, bytes);
        FileChannel fileChannel = getChannel(file);
        try {
            long start = System.currentTimeMillis();
            //分配直接内存 n MB
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 50);
            buffer.clear();
            buffer.put(bytes);
            //切换到读模式(从buffer读到channel中,对于我们来说是写文件)
            buffer.flip();
            fileChannel.write(buffer);
            //通知操作系统刷盘
            fileChannel.force(false);
            LogHelper.calDuration(start);
            buffer.clear();
            //回收堆外内存
            ((DirectBuffer) buffer).cleaner().clean();
        } finally {
            fileChannel.close();
            LogHelper.logTag("FileChannel顺序写", "end", file, bytes);
        }
    }

    private FileChannel getChannel(File file) throws IOException {
        return new RandomAccessFile(file, "rw").getChannel();
    }
}
