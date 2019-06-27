package com.sjw.file.io.all.helper;

import java.io.File;

/**
 * @author shijiawei
 * @version FileHelper.java -> v 1.0
 * @date 2019/6/11
 * 可怕之文件帮助者
 */
public class FileHelper {

    private static final String BASE_PATH = "/home/file-io/";
    private static final String STREAM_FALG = "stream";
    private static final String BUFFER_FALG = "buffer";
    private static final String FILE_CHANNEL_FALG = "fc";
    private static final String MMAP_FALG = "mmap";

    public static File getFile(int type, String model) {
        String path = BASE_PATH;
        if (type == 1) {
            path = path + model + "-" + STREAM_FALG;
        } else if (type == 2) {
            path = path + model + "-" + BUFFER_FALG;
        } else if (type == 3) {
            path = path + model + "-" + FILE_CHANNEL_FALG;
        } else if (type == 4) {
            path = path + model + "-" + MMAP_FALG;
        } else {
            throw new IllegalArgumentException("file io no this type!");
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.mkdir();
        return file;
    }

}
