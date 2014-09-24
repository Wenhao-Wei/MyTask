package com.shafts.utils;

import java.io.*;

public class FileSplit {

	private File MolFile;
	private String name; //check whether the string contains illegal chars
	private String fileName; // the name of file 

	public void StartSplit(String filePath) throws IOException {
		String FilePath = filePath + "\\Hits.mol2";
		//System.out.println("*****FilePath*****:" + FilePath);
		String s = null;
		int i = 1;
		LineNumberReader lnr = null;
		// byte []newLine="/n".getBytes();
		String newLine = System.getProperty("line.separator");
		File SrcFile = new File(FilePath);
		if (SrcFile.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(SrcFile);
				lnr = new LineNumberReader(fr);
				s = lnr.readLine();
				while (s != null) {
					if (s.startsWith("# Name:")) {
						String[] str = s.split("\t\t");
						name = str[1].replace(" ", "");
						if (name.contains("/")){
						fileName = i + "";
						i++;
						}
						else
							fileName = name;
						MolFile = new File(filePath + "\\" + fileName + ".mol2");
						//System.out.println(MolFile);
						if (!MolFile.exists()) {
							MolFile.createNewFile();
						}
						s = lnr.readLine();
					}
					if (s.equals("@<TRIPOS>MOLECULE")) {

						FileOutputStream out = new FileOutputStream(MolFile);
						out.write(s.getBytes());
						out.write(newLine.getBytes());
						out.write(name.getBytes());
						out.write(newLine.getBytes());
						s = lnr.readLine();
						s = lnr.readLine();
						do {
							out.write(s.getBytes());
							out.write(newLine.getBytes());
							s = lnr.readLine();							
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
		String filePath = "D:\\MyOffice\\Github\\MyTask\\SHAFTS\\workspace\\localwork\\Job1";
		try {
			new FileSplit().StartSplit(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
