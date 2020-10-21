package com.seven.web;

import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * 习题集
 */
public class Test {


    /**
     * 字符串最后一个单词的长度
     *
     * @return
     */
    public void getLastWordLength(String scannerIn) {
        String s = StringUtils.EMPTY;
        s = scannerIn;
        System.out.println(s);
        String[] sArr = s.split(" ");
        System.out.println(sArr[sArr.length - 1].length());
    }

}
