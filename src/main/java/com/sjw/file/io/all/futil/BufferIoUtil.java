package com.sjw.file.io.all.futil;

import com.sjw.file.io.all.helper.ByteHelper;
import com.sjw.file.io.all.helper.LogHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

    @Override
    public long sequenceRead(File file, int onceKb) throws IOException {
        LogHelper.logTag("BufferStreamIo顺序读", "start", file, null);
        //设置缓冲大小，默认就为8 * 1024，必须设置为1024的倍数性能最佳
        int bufferSize = 8 * 1024;
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file), bufferSize);
        long start = System.currentTimeMillis();
        try {
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[ByteHelper.bytesByKb(onceKb)];
            int flag = 0;
            while ((flag = bis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, flag));
            }
            LogHelper.printReadInfo(sb.toString());
            long duration = System.currentTimeMillis() - start;
            LogHelper.printDuration(duration);
            return duration;
        } finally {
            bis.close();
            LogHelper.logTag("BufferStreamIo顺序读", "end", file, null);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/Users/shijiawei/Desktop/io-test.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[16];
            int flag = 0;
            while ((flag = bis.read(buffer)) != -1) {
                String once = new String(buffer, 0, flag);
                System.out.println(once);
                sb.append(once);
            }
            System.out.println(sb.toString());
        } finally {
            bis.close();
        }
    }
}
