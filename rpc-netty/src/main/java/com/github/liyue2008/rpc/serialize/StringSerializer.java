package com.github.liyue2008.rpc.serialize;

import com.github.liyue2008.rpc.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class StringSerializer implements Serializer<String> {
    @Override
    public byte[] serialize(String entry) {
        return entry.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String parse(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }
}
