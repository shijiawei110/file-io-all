package com.sjw.file.io.all.test;

import org.junit.Test;

import java.nio.charset.CharacterCodingException;
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
        int sjw = 123;
        kkk(sjw);
    }

    private void kkk(Object jj) {
        System.out.println(jj.toString());
    }
}
