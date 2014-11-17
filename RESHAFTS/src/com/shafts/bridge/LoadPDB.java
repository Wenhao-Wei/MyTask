/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.bridge;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-28 15:54:27
 */
public class LoadPDB {

    public final static boolean DEBUG = true; // 调试用
    private static int BUFFER_SIZE = 8096; // 缓冲区大小
    private Vector vDownLoad = new Vector(); // URL列表
    private Vector vFileList = new Vector(); // 下载后的保存文件名列表

    /**
     * 　* 清除下载列表
     */
    public void resetList() {
        vDownLoad.clear();
        vFileList.clear();
    }

    /**
     * 　* 增加下载列表项 * * @param url String * @param filename String
     */
    public void addItem(String url, String filename) {
        vDownLoad.add(url);
        vFileList.add(filename);
    }

    /**
     * 　* 根据列表下载资源
     */
    public void downLoadByList() {
        String url = null;
        String filename = null;
        // 按列表顺序保存资源
        for (int i = 0; i < vDownLoad.size(); i++) {
            url = (String) vDownLoad.get(i);
            filename = (String) vFileList.get(i);
            try {
                saveToFile(url, filename);
            } catch (IOException err) {
                if (DEBUG) {
                    System.out.println("资源[" + url + "]下载失败!!!");
                }
            }
        }
        if (DEBUG) {
            System.out.println("下载完成!!!");
        }
    }

    /**
     * 将HTTP资源另存为文件
     *
     * @param destUrl String
     * @param fileName String
     * @throws Exception
     */
    public void saveToFile(String destUrl, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;

        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        // 建立链接
        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
        int lenth = httpUrl.getContentLength();
        // 连接指定的资源
        httpUrl.connect();
        // 获取网络输入流

        bis = new BufferedInputStream(httpUrl.getInputStream());
        // 建立文件
        fos = new FileOutputStream(fileName);
        if (this.DEBUG) {
            System.out.println("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件["
                    + fileName + "] \n 文件大小为: " + lenth);
        }
        // 保存文件
        while ((size = bis.read(buf)) != -1) {
            fos.write(buf, 0, size);
        }
        fos.close();
        bis.close();
        httpUrl.disconnect();
    }

    /**
     * 将HTTP资源另存为文件
     *
     * @param destUrl String
     * @param fileName String
     * @throws java.io.IOException
     */
    public boolean saveToFile2(String destUrl, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        // 建立链接
        url = new URL(destUrl);
        HttpURLConnection.setFollowRedirects(false);
        httpUrl = (HttpURLConnection) url.openConnection();
        // String authString = "username" + ":" + "password";
        String authString = "50301" + ":" + "88888888";
        String auth = "Basic "
                + new sun.misc.BASE64Encoder().encode(authString.getBytes());
        httpUrl.setRequestProperty("Proxy-Authorization", auth);
        int state = httpUrl.getResponseCode();
        if (state != 200) 
            return false;     
        else {
             // 连接指定的资源
            httpUrl.connect();
            // 获取网络输入流
            InputStream in = httpUrl.getInputStream();
            bis = new BufferedInputStream(in);
            // 建立文件
            fos = new FileOutputStream(fileName);
            if (this.DEBUG) {
                System.out.println("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件["
                        + fileName + "] \n文件大小为: " + httpUrl.getHeaderField("Content-Length"));
            }
            // 保存文件
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();
            httpUrl.disconnect();
            return true;
        }
    }

    /**
     * 设置代理服务器
     *
     * @param proxy String
     * @param proxyPort String
     */
    public void setProxyServer(String proxy, String proxyPort) {
        // 设置代理服务器
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", proxy);
        System.getProperties().put("proxyPort", proxyPort);
    }

//	public void setAuthenticator(String uid, String pwd) {
//		Authenticator.setDefault(new MyAuthenticator());
//	}
    /**
     * 主方法(用于测试)
     *
     * @param argv String[]
     */
    public static void main(String argv[]) throws IOException {
        //HttpGet oInstance = new HttpGet();
        LoadPDB save = new LoadPDB();

        boolean flag = save.saveToFile2("http://www.rcsb.org/pdb/files/1fv.pdb.gz",
                "3ika.pdb.gz");
         System.out.println(flag);
    }
}
