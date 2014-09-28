package com.shafts.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.openbabel.OBConversion;
import org.openbabel.OBMol;


/**
 * Format conversion
 * @author Wenhao Wei
 * 2014-07-06
 */
public class FormatConv{
	/**
	 * 
	 */
	private boolean convFlag = false;
	private File inMol;
	private String inMolName;
	private String inFormat;
	/**
	 * convert
	 * @param inFilePath
	 * 			the file path of the input molecule
	 * @param outFilePath
	 * 			the file path of the output molecule
	 * @param outFormat
	 * 			the format of the output molecule
	 * @return
	 * 		  	return the flag if the conversion has succeeded
	 */
	public boolean formatconv(final String inFilePath,final String outFilePath,final String outFormat){
			convFlag = false;
			inMol = new File(inFilePath);
			inMolName = inMol.getName();
			inFormat = inMolName.substring(inMolName.lastIndexOf(".")+1);	
			// Read molecule from informat
			System.loadLibrary("openbabel_java");
			OBConversion conv = new OBConversion();
			OBMol mol = new OBMol(); 
			try{	       
				 conv.SetInFormat(inFormat);
				 conv.ReadFile(mol, inFilePath);	  
				 conv.SetOutFormat(outFormat);
				 conv.WriteFile(mol, outFilePath);
				 convFlag = true;
				 //JOptionPane.showMessageDialog( null,"Convert Success��");
				 }catch(Exception e){
				   	e.printStackTrace();
				   	int i = JOptionPane.showConfirmDialog( null,"Please install the Openbabel first!\n Install now?","Tips",JOptionPane.YES_NO_OPTION);
				   	if(i == JOptionPane.OK_OPTION){
				   		Desktop desktop = Desktop.getDesktop();  
				        try {
							desktop.browse(new URI("http://openbabel.org/wiki/Main_Page"));
								} catch (IOException e1) {
										e1.printStackTrace();
									} catch (URISyntaxException e1) {
										e1.printStackTrace();
									} 
				   			}				   				
				   		}	       	       
			        // conv.WriteString(mol);									   	
				   	return convFlag;
			}
	public static void main(String args[]){
		
	}
}
