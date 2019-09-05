package com.sjw.file.io.all.futil;

import com.sjw.file.io.all.helper.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author shijiawei
 * @version StreamIoUtil.java -> v 1.0
 * @date 2019/6/5
 */
public class StreamIoUtil implements FileStandardUtil {

    public static StreamIoUtil instance = new StreamIoUtil();

    @Override
    public long sequenceWrite(File file, byte[] bytes) throws IOException {
        OutputStream output = new FileOutputStream(file, true);
        try {
            LogHelper.logTag("StreamIo顺序写", "start", file, bytes);
            long start = System.currentTimeMillis();
            output.write(bytes);
            long duration = System.currentTimeMillis() - start;
            LogHelper.calDuration(start);
            return duration;
        } finally {
            output.close();
            LogHelper.logTag("StreamIo顺序写", "end", file, bytes);
        }
    }

    @Override
    public long randomWrite(File file, byte[] bytes) throws IOException {
        return 0;
    }
}
