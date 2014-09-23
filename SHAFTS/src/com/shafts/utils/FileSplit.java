package com.shafts.utils;

import java.io.*;

public class FileSplit {

    public void StartSplit(String filePath) throws IOException {
        String FilePath = filePath + "\\Hits.mol2";
        String s = null;
        LineNumberReader lnr = null;
        //byte []newLine="/n".getBytes();
        String newLine = System.getProperty("line.separator");
        File SrcFile = new File(FilePath);
        if (SrcFile.exists()) {
            FileReader fr;
            try {
                fr = new FileReader(SrcFile);
                lnr = new LineNumberReader(fr);
                s = lnr.readLine();
                while (s != null) {
                    if (s.equals("@<TRIPOS>MOLECULE")) {
                        String name = lnr.readLine();
                        File MolFile = new File(filePath + "\\" + name + ".mol2");
                        System.out.println(MolFile.getName());
                        if (!MolFile.exists()) {
                            MolFile.createNewFile();
                        }
                        FileOutputStream out = new FileOutputStream(MolFile);
                        out.write(s.getBytes());
                        out.write(newLine.getBytes());
                        out.write(name.getBytes());
                        out.write(newLine.getBytes());
                        s = lnr.readLine();
                        do {
                            out.write(s.getBytes());
                            out.write(newLine.getBytes());
                            s = lnr.readLine();
                            System.out.println("********************" + s);
                        } while (!s.equals("@<TRIPOS>Similarity"));
                        out.flush();
                        out.close();
                    } else {
                        s = lnr.readLine();
                    }
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] srgs) {
        String filePath = "D:\\TEST";
        try {
            new FileSplit().StartSplit(filePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
