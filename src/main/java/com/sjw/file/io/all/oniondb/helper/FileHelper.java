package com.sjw.file.io.all.oniondb.helper;

import java.io.File;

/**
 * @author shijiawei
 * @version FileHelper.java -> v 1.0
 * @date 2020/3/5
 */
public class FileHelper {

    public static File getWriteFile() {
        String path = getUserPath() + "/onionDbTables";
        checkPath(path);
        String fileName = path + "/onion_db_table_test.odb";
        return new File(fileName);
    }

    private static String getUserPath() {
        return System.getProperty("user.dir");
    }

    private static void checkPath(String path){
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
    }
}
