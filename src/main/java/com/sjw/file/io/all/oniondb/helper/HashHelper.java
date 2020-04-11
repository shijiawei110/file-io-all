package com.sjw.file.io.all.oniondb.helper;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.sjw.file.io.all.oniondb.common.ParamConstans;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * hash helper
 */
public class HashHelper {

    private static final Charset charset = StandardCharsets.UTF_8;


    public static String hashPosition(String key) {
        int hashValue = doHash(key);
        return String.valueOf(doPosition(hashValue));
    }


    /**
     * 默认采用miumiu hash
     */
    private static int doHash(String key) {
        return murmurHash(key);
    }

    private static int doPosition(int hashValue) {
        return (ParamConstans.HASH_SIZE - 1) & hashValue;
    }

    private static int murmurHash(String key) {
        HashFunction function = Hashing.murmur3_32();
        HashCode hascode = function.hashString(key, charset);
        return hascode.asInt();
    }

}
