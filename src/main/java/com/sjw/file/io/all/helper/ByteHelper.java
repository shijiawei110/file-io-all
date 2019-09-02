package com.sjw.file.io.all.helper;

/**
 * @author shijiawei
 * @version ByteHelper.java -> v 1.0
 * @date 2019/8/30
 */
public class ByteHelper {

    private static final int S = 1024;

    public static int bytesByKb(int kb) {
        return kb * S;
    }

    public static int bytesByMb(int mb) {
        return bytesByKb(mb * S);
    }

    public static int bytesByGb(int gb) {
        return bytesByMb(gb * S);
    }
}
