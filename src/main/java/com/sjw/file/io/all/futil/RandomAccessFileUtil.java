//package com.sjw.file.io.all.futil;
//
//import com.sjw.file.io.all.helper.LogHelper;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//
///**
// * @author shijiawei
// * @version RandomAccessFileUtil.java -> v 1.0
// * @date 2019/6/5
// */
//public class RandomAccessFileUtil {
//
//    public static long randomWrite(File file, byte[] bytes) throws IOException {
//        RandomAccessFile raf = new RandomAccessFile(file, "rw");
//        try {
//            LogHelper.logTag("RandomAccess随机写", "start", file, bytes);
//            long start = System.currentTimeMillis();
//
//
//            long duration = System.currentTimeMillis() - start;
//            LogHelper.calDuration(start);
//            return duration;
//        }finally {
//            LogHelper.logTag("RandomAccess随机写", "end", file, bytes);
//            raf.close();
//        }
//    }
//}
