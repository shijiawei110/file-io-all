package com.sjw.file.io.all.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

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
     * @param kNum
     * @return
     */
    public static byte[] buildTxtByK(final int kNum) {
        log.info("create txt start -> size : {}K", kNum);
        int byteNum = kNum * 1024;
        byte[] bytes = new byte[byteNum];
        for (int i = 0; i < byteNum; i++) {
            bytes[i] = (byte) RandomUtils.nextInt(ASCII_START, ASCII_END);
        }
        log.info("create txt complete -> size : {}K", kNum);
        return bytes;
    }

}
