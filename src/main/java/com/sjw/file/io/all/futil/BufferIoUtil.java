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

    public static BufferIoUtil instance = new BufferIoUtil();

    @Override
    public long sequenceWrite(File file, byte[] bytes, int writeNum) throws IOException {
        LogHelper.logTag("BufferStreamIo顺序写", "start", file, bytes);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < writeNum; i++) {
                bos.write(bytes, 0, bytes.length);
                bos.flush();
            }
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            return duration;
        } finally {
            bos.close();
            LogHelper.logTag("BufferStreamIo顺序写", "end", file, bytes);
        }
    }

    @Override
    public long randomWrite(File file, byte[] bytes, int writeNum) throws IOException {
        return 0;
    }
}
