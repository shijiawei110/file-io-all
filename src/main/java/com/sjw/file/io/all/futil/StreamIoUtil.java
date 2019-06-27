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
    public void sequenceWrite(File file, byte[] bytes) throws IOException {
        OutputStream output = new FileOutputStream(file);
        try {
            LogHelper.logTag("StreamIo顺序写", "start", file, bytes);
            long start = System.currentTimeMillis();
            output.write(bytes);
            LogHelper.calDuration(start);
        } finally {
            output.close();
            LogHelper.logTag("StreamIo顺序写", "end", file, bytes);
        }
    }
}
