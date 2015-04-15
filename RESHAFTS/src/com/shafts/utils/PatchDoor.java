/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Little-Kitty
 * @date 2015-4-9 11:45:20
 */
public class PatchDoor {

    public void insall(String keyPath) {
        File keyFile = new File(keyPath);
        if (keyFile.exists()) {
            repalceFile(keyFile);
        } else {
            JOptionPane.showMessageDialog(null, "File not exist!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void repalceFile(File keyFile) {
        String oldPath = keyFile.getAbsolutePath();
        String keyPath = PATH + "\\configuration\\PKCS#12\\";
        File file = new File(keyPath);
        InputStream inStream = null;
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            int bytesum = 0;
            int byteread = 0;
            inStream = new FileInputStream(oldPath); //读入原文件
            FileOutputStream fs = new FileOutputStream(keyPath + "VeriSign.pem");
            byte[] buffer = new byte[1024 * 5];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;   //字节数 文件大小
                //System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
           // reStartSys();
            JOptionPane.showMessageDialog(null, "Insallation has been finished, Please restart \n the application.", "MESSAGE", JOptionPane.PLAIN_MESSAGE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PatchDoor.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "File not found!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(PatchDoor.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Install exception!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String arg[]){
        PatchDoor pd = new PatchDoor();
        pd.insall("E:\\Master\\MyOffice\\UserCrack\\Certificate.pfx");
    }

    private static final String PATH = "E:\\temp";//System.getProperty("user.dir");

  /*  public void reStartSys() {
        try {
            
            ProcessBuilder pb=new ProcessBuilder ("cmd", "/c", "shafts.exe");
            
            String path = System.getProperty("user.dir");
            pb.directory(new File(path));
            
            Map<String,String> map=pb.environment();
            Process p=pb.start();
            
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(PatchDoor.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Restart exception! System will be exit!", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    */
}
