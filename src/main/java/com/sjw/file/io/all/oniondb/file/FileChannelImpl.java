package com.sjw.file.io.all.oniondb.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Component
public class FileChannelImpl implements FileStandardApi {

    @Override
    public void sequenceWrite(File file, ByteBuffer byteBuffer) throws IOException {
        FileChannel fileChannel = getChannel(file);

        try {
            fileChannel.write(byteBuffer);
            clearByteBuffer(byteBuffer);
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
            clearByteBuffer(buffer);
        } finally {
            fileChannel.close();
        }
        return result;
    }

    private void clearByteBuffer(ByteBuffer byteBuffer) {
        //clear
        if (null != byteBuffer) {
            byteBuffer.clear();
            //回收堆外内存
            if (byteBuffer.isDirect()) {
                ((DirectBuffer) byteBuffer).cleaner().clean();
            }
        }
    }

    private FileChannel getChannel(File file) throws IOException {
        return new RandomAccessFile(file, "rw").getChannel();
    }

}
