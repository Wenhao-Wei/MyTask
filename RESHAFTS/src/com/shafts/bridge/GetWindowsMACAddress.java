package com.shafts.bridge;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class GetWindowsMACAddress {

    private String localMac = null;
    private InetAddress ia;

    public GetWindowsMACAddress() {

    }

    public String getAddress() {

        byte[] mac;
        try {
            ia = InetAddress.getLocalHost();
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }			
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
            localMac = sb.toString().toUpperCase();
        } catch (SocketException | UnknownHostException ex) {
            JOptionPane.showMessageDialog(null, "get system data exception!", "message", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(GetWindowsMACAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        return localMac;
    }

    /*    public String getAddress() {

     BufferedReader bufferedReader = null;
     Process process = null;
     try {
     /**
     * windows
     */
    /* process = Runtime.getRuntime().exec("ipconfig /all");
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
     System.out.println(mac);
     }
     break;
     }
     }
     } catch (IOException e) {
     e.printStackTrace();
     } finally {
     try {
     if (bufferedReader != null) {
     bufferedReader.close();
     }
     } catch (IOException e1) {
     e1.printStackTrace();
     }
     bufferedReader = null;
     process = null;
     }

     return mac;
     }*/
    public static void main(String args[]) {
        String a = new GetWindowsMACAddress().getAddress();
        //a = a.replace("-", "");
        System.out.println(a);
    }
}
