package com.sjw.file.io.all.futil;

import com.sjw.file.io.all.helper.LogHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author shijiawei
 * @version BufferIoUtil.java -> v 1.0
 * @date 2019/6/5
 */
public class BufferIoUtil implements FileStandardUtil {

    @Override
    public void sequenceWrite(File file, byte[] bytes) throws IOException {
        LogHelper.logTag("BufferStreamIo顺序写", "start", file, bytes);
        long start = System.currentTimeMillis();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        try {
            bos.write(bytes, 0, bytes.length);
            bos.flush();
            LogHelper.calDuration(start);
        } finally {
            bos.close();
            LogHelper.logTag("BufferStreamIo顺序写", "end", file, bytes);
        }
    }
}
