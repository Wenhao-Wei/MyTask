/*
 * the shafts
 */
package com.shafts.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.shafts.utils.FileSplit;
import com.shafts.utils.FormatConv;
import com.shafts.utils.InitVector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author baoabo
 *
 */
public class Similarity {

    /**
     *
     * @param NewPath output result path
     * @param inFilePath input molecule
     * @param DataBase user upload database
     * @param outputNum
     * @param threshold molecule threshold
     * @param localModel
     */
    public void shaftinit(String NewPath, String inFilePath, String DataBase, String outputNum, String threshold, String localModel) {
        String FilePath = inFilePath;
        //boolean isOk = false;
        if (localModel.equals("conformer")) // decide the run model 
        {
            DataBase = generateConformer(DataBase, "Database");
        }
        if (new File(DataBase).exists()) {            
            boolean movefile = false;
            if ((inFilePath.substring(inFilePath.lastIndexOf(".") + 1)).equals("mol")) { // convert the input format to mol2
                movefile = true;
                FilePath = format_To_Mol2(inFilePath);
            }
            if (!movefile) {                // 
                String newpath = workPath + "\\Input.mol2";
                copyMol2(inFilePath, newpath);
            }
            similarityRun(FilePath, DataBase, outputNum, threshold);

        }
    }
    public String getworkid(String NewPath){
        int i = 1;
            String j = i + "";
            workPath = NewPath + j;
            File F1 = new File(workPath);
            while (F1.exists()) {
                i++;
                j = i + "";
                workPath = NewPath + j;
                F1 = new File(workPath);
            }
            F1.mkdir();           
            LocalworkID = "Job" + j;
            return LocalworkID;
    }

    /**
     * generate conformers
     *
     * @param inFile
     * @param model
     * @return
     */
    public String generateConformer(String inFile, String model) {
        File file1 = new File(inFile);
        String name = file1.getName();
        String outpath = System.getProperty("user.dir") + "\\conformers";
        File file2 = new File(outpath);
        InputStream ins = null;
        if (!file2.exists()) {
            file2.mkdir();
        }
        if (model.equals("Database")) {
            outpath = outpath + "\\Databases";
            outPath = outpath + "\\" + name;
            cmd = "Cyndi.exe -i " + inFile + " -o " + outPath + " -p" + " CyndiParam.in ";
        } else {
            outpath = outpath + "\\files";
            outPath = outpath + "\\" + name;
            cmd = "Cyndi.exe -i " + inFile + " -o " + outPath + " -p" + " CyndiParamPerMol2.in ";
        }
        if (!(new File(outpath).exists())) {
            new File(outpath).mkdir();
        }
        try {
            //Runtime.getRuntime().exec(cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            ins = process.getInputStream(); // cmd start
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitValue = process.waitFor();
            System.out.println("return value:" + exitValue);
            process.getOutputStream().close(); // close the stream
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Similarity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outPath;
    }

    /**
     * begin the process
     *
     * @param FilePath
     * @param DataBase
     * @param outputNum
     * @param threshold
     */
    private void similarityRun(String FilePath, String DataBase, String outputNum, String threshold) {
        if (!FilePath.equals("stop")) {
            String cmd = "Cynthia.exe -q " + FilePath + " -t " + DataBase + " -n " + outputNum + " -sCutoff " + threshold;
            InputStream ins = null;
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                ins = process.getInputStream(); // cmd start
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                int exitValue = process.waitFor();
                System.out.println("return value:" + exitValue);
                process.getOutputStream().close(); // close the stream

            } catch (IOException | InterruptedException e) {
                Logger.getLogger(Similarity.class.getName()).log(Level.SEVERE, null, e);
            }
            movefiles();
        }
    }

    /**
     * copy the query file to the result dir
     *
     * @param oldPath
     * @param newPath
     */
    private void copyMol2(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024 * 5];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;   //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            //System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * move result and hits
     */
    public void movefiles() {
        byte[] b = new byte[1024 * 5];
        try {
            File F2 = new File("Result.list");
            if (F2.exists()) {
                File F3 = new File("Hits.mol2");
                FileInputStream input = new FileInputStream(F2);
                FileInputStream input1 = new FileInputStream(F3);
                FileOutputStream output = new FileOutputStream(workPath + "/" + F2.getName());
                FileOutputStream output1 = new FileOutputStream(workPath + "/" + F3.getName());
                // byte[] b = new byte[1024 * 5];
                int len;
                int len1;
                while ((len = input.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                output.flush();
                output.close();
                input.close();
                F2.delete();
                while ((len1 = input1.read(b)) != -1) {
                    output1.write(b, 0, len1);
                }
                output1.flush();
                output1.close();
                input1.close();
                F3.delete();
                try {
                    new FileSplit().StartSplit(workPath);
                } catch (IOException e) {
                }
            }
        } catch (IOException ioe) {
        }
    }

    private String format_To_Mol2(String infilepath) {
        FormatConv fc = new FormatConv();
        String outformat = "mol2";
        String path = workPath + "\\input.mol2";
        boolean flag = fc.formatconv(infilepath, path, outformat);
        if (flag) {
            return path;
        } else {
            return "stop";
        }
    }

    /**
     * @return
     */
    public Vector getdata() {
        String path = workPath + "\\Result.list";
        IV = new InitVector();
        workdata = IV.getdata(path);
        return workdata;
    }

    public static void main(String args[]) {
        Similarity sm = new Similarity();
        //sm.shaftinit("D:\\TEST\\result\\job", "D:\\TEST\\DB00800.mol2", "D:\\TEST\\Hits.mol2", "100", "1.2", "conformer");
        sm.generateConformer("D:\\TEST\\Hits.mol2", "Database");
    }

    private String LocalworkID;
    private String workPath;
    private InitVector IV;
    private Vector workdata;
    private String cmd;
    private String outPath;
}
