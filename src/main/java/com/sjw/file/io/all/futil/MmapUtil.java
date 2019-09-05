package com.sjw.file.io.all.futil;

import com.sjw.file.io.all.helper.LogHelper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author shijiawei
 * @version MmapUtil.java -> v 1.0
 * @date 2019/6/5
 * mmap的slice()作用是创造一个指定区间的buffer窗口,但是引用的是同一个buffer : https://janla.iteye.com/blog/322638
 */
public class MmapUtil implements FileStandardUtil {

    public static MmapUtil instance = new MmapUtil();

    @Override
    public long sequenceWrite(File file, byte[] bytes) throws IOException {
        FileChannel fileChannel = getChannel(file);
        //范围 0-size 就是映射整个字节的文件 ,不能超过1.5G
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
        try {
            LogHelper.logTag("Mmap顺序写", "start", file, bytes);
            long start = System.currentTimeMillis();
            ByteBuffer subBuffer = mappedByteBuffer.slice();
            subBuffer.position((int) fileChannel.size());
            subBuffer.put(bytes);
            //手动同步pageData刷盘
//            mappedByteBuffer.force();
            long duration = System.currentTimeMillis() - start;
            LogHelper.calDuration(start);
            return duration;
        } finally {
            fileChannel.close();
            clean(mappedByteBuffer);
            LogHelper.logTag("Mmap顺序写", "end", file, bytes);
        }
    }

    @Override
    public long randomWrite(File file, byte[] bytes) throws IOException {
        FileChannel fileChannel = getChannel(file);
        long currentPosition = fileChannel.size() / 2;
        //范围 0-size 就是映射整个字节的文件 ,不能超过1.5G
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
        try {
            LogHelper.logTag("Mmap随机写", "start", file, bytes);
            long start = System.currentTimeMillis();
            ByteBuffer subBuffer = mappedByteBuffer.slice();
            subBuffer.position((int) currentPosition);
            subBuffer.put(bytes);
            long duration = System.currentTimeMillis() - start;
            LogHelper.calDuration(start);
            return duration;
        } finally {
            fileChannel.close();
            clean(mappedByteBuffer);
            LogHelper.logTag("Mmap随机写", "end", file, bytes);
        }
    }

    /**
     * 释放mmap
     */
    public static void clean(MappedByteBuffer mappedByteBuffer) {
        ByteBuffer buffer = mappedByteBuffer;
        if (buffer == null || !buffer.isDirect() || buffer.capacity() == 0) {
            return;
        }
        invoke(invoke(viewed(buffer), "cleaner"), "clean");
    }

    private static Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    private static Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    private static ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }
        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null)
            return buffer;
        else
            return viewed(viewedBuffer);
    }

    private FileChannel getChannel(File file) throws IOException {
        return new RandomAccessFile(file, "rw").getChannel();
    }
}
