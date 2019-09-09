package com.sjw.file.io.all.futil;

import java.io.File;
import java.io.IOException;

/**
 * @author shijiawei
 * @version FileStandardUtil.java -> v 1.0
 * @date 2019/5/28
 * 文件通用操作接口
 */
public interface FileStandardUtil {

    /**
     * 顺序写
     */
    long sequenceWrite(File file, byte[] bytes,int writeNum) throws IOException;

    /**
     * 随机写
     */
    long randomWrite(File file, byte[] bytes,int writeNum) throws IOException;
}
