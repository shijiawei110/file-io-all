package com.sjw.file.io.all.oniondb.file;

import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shijiawei
 * @version FileChannelImpl.java -> v 1.0
 * @date 2019/6/5
 * filechannel util工具
 */
@Slf4j
public class FileChannelImpl implements FileStandardApi {

    private FileChannelImpl() {
    }

    public static final FileChannelImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final FileChannelImpl INSTANCE = new FileChannelImpl();
    }

    @Override
    public void sequenceWrite(File file, ByteBuffer byteBuffer) throws IOException {
        FileChannel fileChannel = getChannel(file);
        try {
            if (file.exists()) {
                //追加写
//                fileChannel.write(byteBuffer, file.length());
                fileChannel.write(byteBuffer);
            } else {
                fileChannel.write(byteBuffer);
            }
            //clear
            byteBuffer.clear();
            //回收堆外内存
            if (byteBuffer.isDirect()) {
                ((DirectBuffer) byteBuffer).cleaner().clean();
            }
        } finally {
            fileChannel.close();
        }
    }

    @Override
    public void randomWrite(File file, ByteBuffer byteBuffer) throws IOException {
        return;
    }

    @Override
    public long sequenceRead(File file) throws IOException {
        return 0;
    }

    @Override
    public byte[] randomRead(File file, int offset, int readByteNum) throws IOException {
        FileChannel fileChannel = getChannel(file);
        byte[] result = new byte[readByteNum];
        try {
            ByteBuffer buffer = ByteBuffer.allocate(readByteNum);
            fileChannel.read(buffer, offset);
            //切换到读模式
            buffer.flip();
            buffer.get(result);
        } finally {
            fileChannel.close();
        }
        return result;
    }

    private FileChannel getChannel(File file) throws IOException {
        return new RandomAccessFile(file, "rw").getChannel();
    }

}
