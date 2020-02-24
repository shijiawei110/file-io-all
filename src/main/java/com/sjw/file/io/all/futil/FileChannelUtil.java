package com.sjw.file.io.all.futil;

import com.sjw.file.io.all.helper.ByteHelper;
import com.sjw.file.io.all.helper.LogHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
//        int size = bytes.length;
//        size += size >> 1;
        //分配直接堆外内存 n MB，用完需要回收
//        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        int length = bytes.length;
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < writeNum; i++) {
//                buffer.clear();
//                buffer.put(bytes);
//                //切换到读模式(从buffer读到channel中,对于我们来说是写文件)
//                buffer.flip();
                fileChannel.write(ByteBuffer.wrap(bytes), length * i);
            }
            //通知操作系统刷盘（参数为false是指不刷盘文件的自带权限等信息）
//            fileChannel.force(false);
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
//            buffer.clear();
//            //回收堆外内存
//            if (buffer.isDirect()) {
//                ((DirectBuffer) buffer).cleaner().clean();
//            }
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
        int length = bytes.length;
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < writeNum; i++) {
                long currentPosition = (length * i) / 2;
                fileChannel.write(ByteBuffer.wrap(bytes), currentPosition);
            }
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            return duration;
        } finally {
            fileChannel.close();
            LogHelper.logTag("FileChannel随机写", "end", file, bytes);
        }
    }

    @Override
    public long sequenceRead(File file, int onceKb) throws IOException {
        LogHelper.logTag("FileChannel顺序读", "start", file, null);
        FileChannel fileChannel = getChannel(file);
        long start = System.currentTimeMillis();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(ByteHelper.bytesByKb(onceKb));
            StringBuilder sb = new StringBuilder();
            while (true) {
                int count = fileChannel.read(buffer);
                if (count <= -1) {
                    break;
                }
                //切换到读模式
                buffer.flip();
                Charset charset = StandardCharsets.UTF_8;
                String str = charset.decode(buffer).toString();
                sb.append(str);
                //切换到写模式
                buffer.compact();
            }
            LogHelper.printReadInfo(sb.toString());
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            return duration;
        } finally {
            fileChannel.close();
            LogHelper.logTag("FileChannel顺序读", "end", file, null);
        }
    }

    private FileChannel getChannel(File file) throws IOException {
        return new RandomAccessFile(file, "rw").getChannel();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/shijiawei/Desktop/io-test.txt");
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        StringBuilder sb = new StringBuilder();
        while (true) {
            int count = fileChannel.read(buffer);
            if (count <= -1) {
                break;
            }
            //切换到读模式
            buffer.flip();
            Charset charset = StandardCharsets.UTF_8;
            String str = charset.decode(buffer).toString();
            System.out.println(str);
            sb.append(str);
            //切换到写模式
            buffer.compact();
        }
        System.out.println(sb.toString());
        fileChannel.close();
    }

}
