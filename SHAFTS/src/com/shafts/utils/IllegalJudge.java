/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Little-Kitty
 * @date 2014-9-18 14:51:31
 */
public class IllegalJudge {

    private static final String source = "0123456789abcdefghijklmnopqrstuvwxyz_";

    public boolean isillegal(String str) {
        char ch;
        int temp;
        if(str.length() < 6 || str.length() >16)
            return false;
        for (int i = 0; i < (str.length() - 1); i++) {
            ch = str.charAt(i);
            temp = source.indexOf(ch);
            if (temp == -1) {
                return false;
            }
        }
                return true;
    }
    /** 
     * 验证输入的邮箱格式是否符合 
     * @param email 
     * @return 是否合法 
     */ 
public boolean emailFormat(String email) 
    { 
        final String pattern2 = "^([a-z0-9A-Z]+[-|//.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?//.)+[a-zA-Z]{2,}$";
        final String pattern1 = "^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,4}$";
        final Pattern pattern = Pattern.compile(pattern1); 
        final Matcher mat = pattern.matcher(email); 
        if (!mat.find()) { 
            return false; 
        }
        else
            return true; 
    }
public boolean isMobilenum(String mobiles) {
Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
Matcher m = p.matcher(mobiles);
return m.matches();
}
public String getMailformat(String email){
    String result;
    String[] tmp = email.split("@");
    String mid = tmp[1];
    String[] tmp1 = mid.split("\\.");
    result = tmp1[0];
    return result;
}
    public static void main(String args[]){
            String s = "12323151554";
            IllegalJudge il = new IllegalJudge();
            boolean b = il.isMobilenum(s);
            System.out.println(b);
            
    }
}
