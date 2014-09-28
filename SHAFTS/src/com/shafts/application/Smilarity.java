/*
 * the shafts
 */
package com.shafts.application;

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
import com.shafts.utils.InitVector;

/**
 *
 * @author baoabo
 *
 */
public class Smilarity {

	/**
	 *
	 * @param NewPath
	 *            output result path
	 * @param inFilePath
	 *            input molecule
	 * @param DataBase
	 *            user upload database
	 * @param outputNum
	 *
	 * @param threshold
	 *            molecule threshold
	 */
	public void shaftinit(String NewPath, String inFilePath, String DataBase, String outputNum, String threshold) {
		String FilePath = inFilePath;
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
		
		boolean movefile = false;
		if((inFilePath.substring(inFilePath.lastIndexOf(".") + 1)).equals("mol")){
			movefile = true;
			FilePath = format_To_Mol2(inFilePath);
		}
		if(!movefile){
			File f1 = new File(inFilePath);
			String newpath = workPath + "\\Input.mol2"; 			
			File f2 = new File(newpath);
			f1.renameTo(f2);
		}
		if(!FilePath.equals("stop")){
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
			e.printStackTrace();
		}
		movefiles();					
		LocalworkID = "Job" + j;
		}
	}

	/**
	 * move result and hits
	 */
	public void movefiles() {
		byte[] b = new byte[1024 * 5];
		try {
			File F2 = new File("Result.list");
			if (!F2.exists())
				JOptionPane.showMessageDialog(null, "Sorry! No result file to show. \nYour can try again.");
			else {
				File F3 = new File("Hits.mol2");
				FileInputStream input = new FileInputStream(F2);
				FileInputStream input1 = new FileInputStream(F3);
				FileOutputStream output = new FileOutputStream(workPath + "/" + F2.getName().toString());
				FileOutputStream output1 = new FileOutputStream(workPath + "/" + F3.getName().toString());
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private String format_To_Mol2(String infilepath){
		FormatConv fc = new FormatConv();
		String outformat = "mol2";
		String path = workPath + "//input.mol2";
		boolean flag = fc.formatconv(infilepath, path, outformat);
		if(flag)
			return path;
		else
			return "stop";
	}
	/**
	 * @return
	 */
	public String getworkid() {
		return LocalworkID;
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

	private String LocalworkID;
	private String workPath;
	private InitVector IV;
	private Vector workdata;
}
