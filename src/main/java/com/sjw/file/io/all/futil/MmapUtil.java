package com.sjw.file.io.all.futil;

import com.sjw.file.io.all.helper.ByteBufHelper;
import com.sjw.file.io.all.helper.LogHelper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shijiawei
 * @version MmapUtil.java -> v 1.0
 * @date 2019/6/5
 * mmap的slice()作用是创造一个指定区间的buffer窗口,但是引用的是同一个buffer : https://janla.iteye.com/blog/322638
 */
public class MmapUtil implements FileStandardUtil {

    public static MmapUtil instance = new MmapUtil();

    @Override
    public long sequenceWrite(File file, byte[] bytes, int writeNum) throws IOException {
        FileChannel fileChannel = getChannel(file);
        int length = bytes.length;
        //范围 0-size 就是映射整个字节的文件 ,不能超过1.5G
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length * writeNum);
        LogHelper.logTag("Mmap顺序写", "start", file, bytes);
        ByteBuffer subBuffer = mappedByteBuffer.slice();
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < writeNum; i++) {
                subBuffer.position(length * i);
                subBuffer.put(bytes);
            }
            //手动同步pageData刷盘
//            mappedByteBuffer.force();
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            return duration;
        } finally {
            fileChannel.close();
            ByteBufHelper.mmpClean(mappedByteBuffer);
            LogHelper.logTag("Mmap顺序写", "end", file, bytes);
        }
    }


    @Override
    public long randomWrite(File file, byte[] bytes, int writeNum) throws IOException {
        FileChannel fileChannel = getChannel(file);
        int length = bytes.length;
        //范围 0-size 就是映射整个字节的文件 ,不能超过1.5G
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length * writeNum);
        LogHelper.logTag("Mmap随机写", "start", file, bytes);
        ByteBuffer subBuffer = mappedByteBuffer.slice();
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < writeNum; i++) {
                long currentPosition = (length * i) / 2;
                subBuffer.position((int) currentPosition);
                subBuffer.put(bytes);
            }
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            return duration;
        } finally {
            fileChannel.close();
            ByteBufHelper.mmpClean(mappedByteBuffer);
            LogHelper.logTag("Mmap随机写", "end", file, bytes);
        }
    }


    private FileChannel getChannel(File file) throws IOException {
        return new RandomAccessFile(file, "rw").getChannel();
    }
}
