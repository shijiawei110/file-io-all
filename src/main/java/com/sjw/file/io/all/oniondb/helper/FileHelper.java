package com.sjw.file.io.all.oniondb.helper;

import com.sjw.file.io.all.oniondb.utils.NumberUtil;

import java.io.File;

/**
 * @author shijiawei
 * @version FileHelper.java -> v 1.0
 * @date 2020/3/5
 */
public class FileHelper {

    private static final String FILE_FORM = ".odb";

    private static final String FILE_FILE_NAME = "/onionDbTables";

    private static final String FILE_NAME_PRE = "/onion_db_table";

    private static final String HASH_KEY_PATH_NAME_PRE = "hz-";

    private static final String CUT_KEY = "_";

    private static final String FILE_PATH_KEY = "/";

    public static File getWriteFile(Integer hashkey, Integer index) {
        if (NumberUtil.invalidNumber(hashkey)) {
            hashkey = 0;
        }
        String path = getUserPath() + FILE_FILE_NAME + FILE_PATH_KEY + HASH_KEY_PATH_NAME_PRE + hashkey;
        checkPath(path);
        String fileName = path + "onion_db_table_test" + FILE_FORM;
        return new File(fileName);
    }

    public static File getWriteFile() {
        String path = getUserPath() + FILE_FILE_NAME;
        checkPath(path);
        String fileName = path + "/onion_db_table_test" + FILE_FORM;
        return new File(fileName);
    }

    public static File getReadFile() {
        String path = getUserPath() + FILE_FILE_NAME;
        String fileName = path + "/onion_db_table_test" + FILE_FORM;
        return new File(fileName);
    }

    private static String getUserPath() {
        return System.getProperty("user.dir");
    }

    private static String getFileName(Integer index) {
        if (NumberUtil.invalidNumber(index)) {
            index = 1;
        }
        return FILE_NAME_PRE + CUT_KEY + index + FILE_FORM;
    }

    private static void checkPath(String path) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
    }
}
