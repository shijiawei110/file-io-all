package com.sjw.file.io.all.oniondb.helper;

import com.google.common.collect.Lists;
import com.sjw.file.io.all.oniondb.utils.NumberUtil;
import com.sjw.file.io.all.utils.StringPool;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

/**
 * @author shijiawei
 * @version FileHelper.java -> v 1.0
 * @date 2020/3/5
 */
public class FileHelper {

    private static final String FILE_FORM = ".odb";

    private static final String FILE_FILE_NAME = "/onionDbTables";

    private static final String FILE_NAME_PRE = "onion_db_table";

    private static final String HASH_KEY_PATH_NAME_PRE = "position-";

    private static final String INDEX_CUT_KEY = "_";

    private static final String FILE_PATH_KEY = "/";


    public static File getCurrentFile(Integer position, Integer index) {
        if (NumberUtil.invalidNumber(position)) {
            position = 0;
        }
        String path = getFilePath(position);
        String fileName = path + getFileName(index);
        return new File(fileName);
    }

    public static int getCurrentFileIndex(Integer position, Integer index) {
        File file = getCurrentFile(position, index);
        return getIndexFromFileName(file.getName());
    }

    /**
     * 获取当前文件夹路径
     */
    public static String getCurrentFilePath(Integer position) {
        if (NumberUtil.invalidNumber(position)) {
            position = 0;
        }
        return getFilePath(position);
    }

    /**
     * 获取路径下的所有文件名
     */
    public static List<String> getAllFileName(final String path) {
        List<String> fileNames = Lists.newArrayList();
        if (StringUtils.isBlank(path)) {
            return fileNames;
        }
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (null == tempList || tempList.length <= 0) {
            return fileNames;
        }
        for (File value : tempList) {
            if (value.isFile()) {
                fileNames.add(value.getName());
            }
            //如果还是文件夹 直接先忽略
//            if (tempList[i].isDirectory()) {
//                getAllFileName(tempList[i].getAbsolutePath(),fileNameList);
//            }
        }
        return fileNames;
    }

    /**
     * 根据文件名获取文件的index编号
     */
    public static int getIndexFromFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return 0;
        }
        fileName = fileName.replace(FILE_NAME_PRE + INDEX_CUT_KEY, StringPool.EMPTY);
        fileName = fileName.replace(FILE_FORM, StringPool.EMPTY);
        return Integer.parseInt(fileName);
    }

    public static File getMainFile() {
        String path = getUserPath() + FILE_FILE_NAME;
        return new File(path);
    }

    private static String getUserPath() {
        return System.getProperty("user.dir");
    }

    private static String getFilePath(Integer position) {
        String path = getUserPath() + FILE_FILE_NAME + FILE_PATH_KEY + HASH_KEY_PATH_NAME_PRE + position;
        checkPath(path);
        return path;
    }

    private static String getFileName(Integer index) {
        if (NumberUtil.invalidNumber(index)) {
            index = 1;
        }
        return FILE_PATH_KEY + FILE_NAME_PRE + INDEX_CUT_KEY + index + FILE_FORM;
    }

    private static void checkPath(String path) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
    }
}
