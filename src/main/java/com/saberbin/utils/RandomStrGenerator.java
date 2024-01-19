package com.saberbin.utils;

import java.util.Random;

/**
 * @projectName: Kafka2FileSystem
 * @package: utils
 * @className: RandomStrGenerator
 * @author: NelsonWu
 * @description: 任意长度随机字符串生成方法
 * @date: 2024/1/10 12:13
 * @version: 1.0
 */
public class RandomStrGenerator {
    /**
     * 生成随机字符串
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generatorRandomStr(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }
}
