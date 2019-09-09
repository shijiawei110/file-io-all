package com.sjw.file.io.all.helper;

import java.io.File;
import java.io.IOException;

/**
 * @author shijiawei
 * @version FileHelper.java -> v 1.0
 * @date 2019/6/11
 * 可怕之文件帮助者
 */
public class FileHelper {

    private static final String BASE_PATH = System.getProperty("user.dir") + "/";
    private static final String STREAM_FALG = "stream.txt";
    private static final String BUFFER_FALG = "buffer.txt";
    private static final String FILE_CHANNEL_FALG = "fc.txt";
    private static final String MMAP_FALG = "mmap.txt";
    private static final String RANDOM_FALG = "raf.txt";

    public static File getFile(int type, String model) throws IOException {
        return getFile(type, model, true);
    }

    public static File getFile(int type, String model, boolean isNeedNew) throws IOException {
        String path = BASE_PATH;
        if (type == 1) {
            path = path + model + "-" + STREAM_FALG;
        } else if (type == 2) {
            path = path + model + "-" + BUFFER_FALG;
        } else if (type == 3) {
            path = path + model + "-" + FILE_CHANNEL_FALG;
        } else if (type == 4) {
            path = path + model + "-" + MMAP_FALG;
        } else if (type == 5) {
            path = path + model + "-" + RANDOM_FALG;
        } else {
            throw new IllegalArgumentException("file io no this type!");
        }
        File file = new File(path);
        if (file.exists()) {
            if (isNeedNew) {
                file.delete();
                file.createNewFile();
            }
        } else {
            file.createNewFile();
        }
        return file;
    }

}
