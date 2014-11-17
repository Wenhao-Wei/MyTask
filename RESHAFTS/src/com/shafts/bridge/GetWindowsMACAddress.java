package com.shafts.bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetWindowsMACAddress{ 
	String mac = null; 
	public String getAddress(){
         
    BufferedReader bufferedReader = null;      
    Process process = null;      
    try {      
          /**   
           * windows   
           */   
        process = Runtime.getRuntime().exec("ipconfig /all");    
        bufferedReader = new BufferedReader(new InputStreamReader(process      
                .getInputStream()));      
        String line = null;      
        int index = -1;      
        while ((line = bufferedReader.readLine()) != null) {      
            index = line.toLowerCase().indexOf("物理地址");     
            if (index != -1) {    
                index = line.indexOf(":");    
                if (index != -1) {    
                   mac = line.substring(index + 1).trim();     
               }    
                break;      
            }    
        }    
    } catch (IOException e) {      
        e.printStackTrace();      
    }finally {      
        try {      
            if (bufferedReader != null) {      
                bufferedReader.close();      
              }      
        }catch (IOException e1) {      
            e1.printStackTrace();      
          }      
        bufferedReader = null;      
        process = null;      
    }      
  
    return mac;      
	}
	public static void main(String args[]){
		String a = new GetWindowsMACAddress().getAddress();
		a = a.replace("-", "");
		System.out.println(a);
	}
} 
