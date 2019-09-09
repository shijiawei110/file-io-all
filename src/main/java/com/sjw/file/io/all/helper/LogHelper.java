package com.sjw.file.io.all.helper;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author shijiawei
 * @version LogHelper.java -> v 1.0
 * @date 2019/6/5
 */
@Slf4j
public class LogHelper {


    public static void calDuration(long start) {
        long end = System.currentTimeMillis();
        long duration = end - start;
        log.info("总计耗时 duration = " + duration + " ms");
    }

    public static void printDuration(long duration) {
        log.info("总计耗时 duration = " + duration + " ms");
    }

    public static void logTag(String type, String desc, File file, byte[] bytes) {
        long byteLength = bytes.length;
        String size = getHumanBytes(byteLength);
        log.info("执行{}文件 【{}】 -> filePath = {} , txtSize = {}", type, desc, file.getPath(), size);
    }

    public static String getHumanBytes(long size) {
        long rest = 0;
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size /= 1024;
        }

        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            rest = size % 1024;
            size /= 1024;
        }

        if (size < 1024) {
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((rest * 100 / 1024 % 100)) + "MB";
        } else {
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }
}
