package com.sjw.file.io.all.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shijiawei
 * @version StringPools.java -> v 1.0
 * @date 2020/2/26
 */
public class StringPool {

    public static final String EMPTY = "";
    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String LINUX_LINE_SEPARATOR = "\n";

    /**
     * 获取换行符
     * windows下的文本文件换行符:\r\n
     * linux/unix下的文本文件换行符:\r
     * Mac下的文本文件换行符:\n
     */
    public static String getLineSeparator(){
        String lineSeparator = System.getProperty("line.separator");
        if(StringUtils.isBlank(lineSeparator)){
            return LINUX_LINE_SEPARATOR;
        }
        return lineSeparator;
    }
}
