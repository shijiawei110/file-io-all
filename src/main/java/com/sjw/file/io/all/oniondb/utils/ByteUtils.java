package com.sjw.file.io.all.oniondb.utils;

/**
 * @author shijiawei
 * @version ByteUtils.java -> v 1.0
 * @date 2020/3/26
 */
public class ByteUtils {
    private static final int INT_BYTE_NUM = 4;

    public static int getBytesInt(byte[] byteArray) {
        if (byteArray.length != INT_BYTE_NUM) {
            return 0;
        }
        int value = ((byteArray[0] & 0xff) << 24) | ((byteArray[1] & 0xff) << 16)
                | ((byteArray[2] & 0xff) << 8) | (byteArray[3] & 0xff);
        return value;
    }

    public static int getByteInt(byte b) {
        int x = b & 0xff;
        return x;
    }

    public static byte intToByte(int x){
        byte b =(byte) (x & 0xff);
        return b;
    }
    /**
     * 把整数转换为字节数组：整数是32位，8位一个字节，依次读取8位，转化为字节数组
     * 整数与0xff,取得最后8位,生成整数,再强转为第3个byte
     * 整数右移8位,与0xff,取得倒数第二组8位,生成整数,再强转为第2个byte
     * 整数右移16位,与0xff,取得倒数第3组8位,生成整数,再强转为第1个byte
     * 整数右移24位,与0xff,取得倒数第4组8位,生成整数,再强转为第0个byte
     */
    public static byte[] intToByteArr(int x){
        byte[] arr = new byte[4];
        arr[3]= (byte)(x & 0xff);
        arr[2]= (byte)(x>>8 & 0xff);
        arr[1]= (byte)(x>>16 & 0xff);
        arr[0]= (byte)(x>>24 & 0xff);
        return arr;
    }

}
