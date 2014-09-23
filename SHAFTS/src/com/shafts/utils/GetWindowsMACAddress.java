package com.shafts.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetWindowsMACAddress{ 
	/**
	 * ��ȡMac��ַ
	 */
	String mac = null; 
	public String getAddress(){
         
    BufferedReader bufferedReader = null;      
    Process process = null;      
    try {      
          /**   
           * windows�µ������ʾ��Ϣ�а�����mac��ַ��Ϣ     
           */   
        process = Runtime.getRuntime().exec("ipconfig /all");    
        bufferedReader = new BufferedReader(new InputStreamReader(process      
                .getInputStream()));      
        String line = null;      
        int index = -1;      
        while ((line = bufferedReader.readLine()) != null) {      
               /**   
                *  Ѱ�ұ�ʾ�ַ���[physical address]    
                */   
            index = line.toLowerCase().indexOf("�����ַ");     
            if (index != -1) {    
                index = line.indexOf(":");    
                if (index != -1) {    
                       /**   
                        *   ȡ��mac��ַ��ȥ��2�߿ո�   
                        */   
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
