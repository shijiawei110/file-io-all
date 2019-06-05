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
    void sequenceWrite(File file, byte[] bytes) throws IOException;
}
