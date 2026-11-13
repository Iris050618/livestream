package com.winter.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class LoginUtil {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = formPass + salt;
        return md5(str);
    }

    public static boolean equals(String inputPassword, String dbPassword, String salt){
        String password = formPassToDBPass(inputPassword, salt);
        return password.equals(dbPassword);
    }

    public static void main(String[] args){
        String result = formPassToDBPass("123456", "ad815f2");
        System.out.print(result);
    }
}
