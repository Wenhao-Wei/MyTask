/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Little-Kitty
 * @date 2014-11-7 15:13:57
 */
public class CheckNetWork {

    public boolean netstatus() {
        
            Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
            Process process = null; // 声明处理类对象
            String line = null; // 返回行信息
            InputStream is = null; // 输入流
            InputStreamReader isr = null; // 字节流
            BufferedReader br = null;
            String ip = "www.baidu.com";
            boolean res = false;// 结果
            try {
            process = runtime.exec("ping " + ip);         // PING
            is = process.getInputStream(); // 实例化输入流
            isr = new InputStreamReader(is);// 把输入流转换成字节流
            br = new BufferedReader(isr);// 从字节中读取文本
            while ((line = br.readLine()) != null) { 
                if (line.contains("TTL")) {
                    res = true;
                     break;
                     }
            }
            is.close();
            isr.close();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(CheckNetWork.class.getName()).log(Level.SEVERE, null, ex);
        }
            return res;
    }
    public static void main(String args[]){
        CheckNetWork cnk = new CheckNetWork();
                boolean re = cnk.netstatus();
                System.out.println(re);
    }
}
