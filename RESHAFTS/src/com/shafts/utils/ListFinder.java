package com.shafts.utils;

import java.io.File;

public class ListFinder {

    /**
     * find the file of "result.list"
     *
     * @param path
     * @return
     */
    public String GetList(String path) {
        File file = new File(path);
        String filename;
        File tempfile;
        String name = null;
        if (file.exists()) {
            for (int i = 0; i < file.list().length; i++) {
                filename = file.list()[i];
                tempfile = new File(filename);
                if (tempfile.isDirectory()) {
                } else {
                    String fileType = filename.substring(filename.lastIndexOf(".") + 1);
                    if (fileType.equals("list")) {
                        name = filename;
                    }
                }
            }
        }
        return name;
    }

    public String getXML(String path){
        File file = new File(path);
        String filename;
        File tempfile;
        String name = null;
        if (file.exists()) {
            for (int i = 0; i < file.list().length; i++) {
                filename = file.list()[i];
                tempfile = new File(filename);
                if (tempfile.isDirectory()) {
                } else {
                    String fileType = filename.substring(filename.lastIndexOf(".") + 1);
                    if (fileType.equals("xml")) {
                        name = filename;
                    }
                }
            }
        }
        return name;
    }
    public static void main(String[] args) {
        String path = "E:\\Master\\Files\\SHAFTS\\靶标注释设计\\13396\\13396\\result";
        String name = new ListFinder().getXML(path);
        System.out.println(name);
    }

}
