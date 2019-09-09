package com.sjw.file.io.all.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shijiawei
 * @version TxtHelper.java -> v 1.0
 * @date 2019/6/27
 * 文本生成器
 */
@Slf4j
public class TxtHelper {

    private static final int ASCII_START = 0;
    private static final int ASCII_END = 127;

    /**
     * 生成nk的 bytes
     *
     * @param size
     * @return
     */
    public static byte[] buildTxtByK(final String size) {
        log.info("create txt start -> size : {}", size);
        int byteNum = 1024;
        if (StringUtils.isNotBlank(size)) {
            String lastWei = size.substring(size.length() - 1, size.length());
            int v = Integer.parseInt(size.substring(0, size.length() - 1));
            if ("b".equalsIgnoreCase(lastWei)) {
                byteNum = v;
            } else if ("k".equalsIgnoreCase(lastWei)) {
                byteNum = ByteHelper.bytesByKb(v);
            } else if ("m".equalsIgnoreCase(lastWei)) {
                byteNum = ByteHelper.bytesByMb(v);
            } else if ("g".equalsIgnoreCase(lastWei)) {
                byteNum = ByteHelper.bytesByGb(v);
            }
        }
        byte[] bytes = new byte[byteNum];
        for (int i = 0; i < byteNum; i++) {
            bytes[i] = (byte) RandomUtils.nextInt(ASCII_START, ASCII_END);
        }
        log.info("create txt complete -> size : {} 字节", byteNum);
        return bytes;
    }

}
