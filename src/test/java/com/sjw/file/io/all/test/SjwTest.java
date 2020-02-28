package com.sjw.file.io.all.test;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

/**
 * @author shijiawei
 * @version SjwTest.java -> v 1.0
 * @date 2020/2/26
 */
public class SjwTest {
    public static void main(String[] args) {
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("aba", 10008);
        treeMap.put("bbb", 10001);
        treeMap.put("aaa", 10000);
        treeMap.put("ccc", 10002);
        System.out.println(treeMap.size());
        treeMap.forEach((k, v) -> {
            System.out.println("key = " + k + "  v = " + v);
        });
    }


    @Test
    public void IoBufferTest() throws CharacterCodingException {
        //不使用堆外内存
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer buffer = IoBuffer.allocate(1024);
        //设置为自动拓展
        buffer.setAutoExpand(true);
        byte k = 5;
        String kl = "abcde";
        int v = 10;
        String vl = "1234567890";

        Charset charset = StandardCharsets.UTF_8;
        CharsetEncoder charsetEncoder = charset.newEncoder();
        CharsetDecoder charsetDecoder = charset.newDecoder();

        buffer.put(k);
        buffer.putString(kl, charsetEncoder);
        buffer.putInt(v);
        buffer.putString(vl, charsetEncoder);

        buffer.flip();
        System.out.println(buffer.get());
//        System.out.println(buffer.getString(5, charsetDecoder));
//        System.out.println(buffer.getInt());
//        System.out.println(buffer.getString(10, charsetDecoder));

    }
}
