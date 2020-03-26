package com.sjw.file.io.all.oniondb.file;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author shijiawei
 * @version FileStandardUtil.java -> v 1.0
 * @date 2019/5/28
 * 文件通用操作接口
 */
public interface FileStandardApi {

    /**
     * 顺序写
     */
    void sequenceWrite(File file, ByteBuffer byteBuffer) throws IOException;

    /**
     * 随机写
     */
    void randomWrite(File file, ByteBuffer byteBuffer) throws IOException;

    /**
     * 顺序读
     */
    long sequenceRead(File file) throws IOException;

    /**
     * 随机读
     */
    byte[] randomRead(File file, int offset, int readByteNum) throws IOException;
}
