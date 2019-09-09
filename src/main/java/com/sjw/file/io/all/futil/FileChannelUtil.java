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
    public long sequenceWrite(File file, byte[] bytes, int writeNum) throws IOException {
        LogHelper.logTag("FileChannel顺序写", "start", file, bytes);
        FileChannel fileChannel = getChannel(file);
        //计算需要分配的内存1.5倍
        int size = bytes.length;
        size += size >> 1;
        //分配直接内存 n MB
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < writeNum; i++) {
                buffer.clear();
                buffer.put(bytes);
                //切换到读模式(从buffer读到channel中,对于我们来说是写文件)
                buffer.flip();
                fileChannel.write(buffer, file.length());
            }
            //通知操作系统刷盘（参数为false是指不刷盘文件的自带权限等信息）
//            fileChannel.force(false);
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            buffer.clear();
            //回收堆外内存
            if (buffer.isDirect()) {
                ((DirectBuffer) buffer).cleaner().clean();
            }
            return duration;
        } finally {
            fileChannel.close();
            LogHelper.logTag("FileChannel顺序写", "end", file, bytes);
        }
    }

    /**
     * 随机写,多线程并发就可以实现
     */
    @Override
    public long randomWrite(File file, byte[] bytes, int writeNum) throws IOException {
        LogHelper.logTag("FileChannel随机写", "start", file, bytes);
        FileChannel fileChannel = getChannel(file);
        //计算需要分配的内存1.5倍
        int size = bytes.length;
        size += size >> 1;
        //分配直接内存 n MB
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < writeNum; i++) {
                long fileLength = file.length();
                long currentPosition = fileLength > 10 ? fileLength - 10 : 1;
                buffer.clear();
                buffer.put(bytes);
                //切换到读模式(从buffer读到channel中,对于我们来说是写文件)
                buffer.flip();
                fileChannel.write(buffer, currentPosition);
            }
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            buffer.clear();
            //回收堆外内存
            if (buffer.isDirect()) {
                ((DirectBuffer) buffer).cleaner().clean();
            }
            return duration;
        } finally {
            fileChannel.close();
            LogHelper.logTag("FileChannel随机写", "end", file, bytes);
        }
    }

    private FileChannel getChannel(File file) throws IOException {
        return new RandomAccessFile(file, "rw").getChannel();
    }

}
