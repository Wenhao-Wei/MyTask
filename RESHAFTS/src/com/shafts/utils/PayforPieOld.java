/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.utils;

import com.shafts.action.GetpieActionOld;
import com.shafts.bridge.CheckUserStatusOld;
import com.shafts.bridge.DecName;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-13 9:39:03
 */
public class PayforPieOld {

    /**
     * renew the product
     * @param chooseday
     * @param cost 
     */
    public void renew(int chooseday, int cost) {
        if (chooseday == 0) {
                            JOptionPane.showMessageDialog(null, "Choose the days you want to extend first!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            CheckUserStatusOld cus = new CheckUserStatusOld();
                            try {
                                String result = cus.renew(cost, chooseday);
                                switch (result) {
                                    case "success":
                                        JOptionPane.showMessageDialog(null, "Success! This cost you " + cost + " yuan", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                        break;
                                    case "error":
                                        JOptionPane.showMessageDialog(null, "Server error! Try it again later", "Tips", JOptionPane.WARNING_MESSAGE);
                                        break;
                                    default:
                                        break;
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(GetpieActionOld.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(null, "Internal Error!", "Tips", JOptionPane.WARNING_MESSAGE);
                            }
                        }
    }
    
    /**
     * pay for the product
     * @param chooseday
     * @param cost
     * @param username
     * @param usertel
     * @param useremail
     * @param checkflag 
     */
    public void purchase(int chooseday, int cost, String username, String usertel, String useremail, int checkflag){
        int iscan = 0;
    IllegalJudge il = new IllegalJudge();
                        
                        if (username.trim().equals("")) {
                            JOptionPane.showMessageDialog(null, "Please input the user name!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            if (checkflag == 0) {//check the username if has registered
                                CheckUserStatusOld CUS = new CheckUserStatusOld();
                                boolean b = il.isillegal(username);
                                if (!b) {
                                    JOptionPane.showMessageDialog(null, "The user name contains illegal characters!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    try {
                                       String isRegister = CUS.getcheck(username);
                                        switch (isRegister) {
                                            case "yes":
                                                iscan = 1;
                                                break;
                                            case "no":
                                                JOptionPane.showMessageDialog(null, "User name has been existed!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                                break;
                                            default:
                                                break;
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(GetpieActionOld.class.getName()).log(Level.SEVERE, null, ex);
                                        JOptionPane.showMessageDialog(null, "Internal error!", "Tips", JOptionPane.WARNING_MESSAGE);
                                    }
                                }
                                checkflag = 1;
                            }
                            if (checkflag == 1) {
                                
                                if (usertel.trim().equals("") || useremail.trim().equals("") || chooseday == 0) {
                                    JOptionPane.showMessageDialog(null, "Incomplete information!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                } else if (!il.isEmail(useremail) || !il.isMobilenum(usertel)) {
                                    if (!il.isEmail(useremail)) {
                                        JOptionPane.showMessageDialog(null, "The user mail is invalid!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "The user tel is invalid!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                } else if (iscan == 1) {
                                    CheckUserStatusOld cus = new CheckUserStatusOld();
                                    try {
                                        String message = cus.buypro(chooseday, cost, username, usertel, useremail);
                                        switch (message) {
                                            case "hasbought":
                                                JOptionPane.showMessageDialog(null, "You have bought the product! You can extend you using days by the renew way", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                                break;
                                            case "Success":
                                                try {
                                                    new DecName().accountRecord(username);
                                                    int i = JOptionPane.showConfirmDialog(null, "Success!This cost you " + cost + " yuan. The  patch file has send to your mailbox, Do you want to check it now?", "Tips", JOptionPane.YES_NO_OPTION);
                                                    if (i == JOptionPane.OK_OPTION) {
                                                        String domain = il.getMailformat(useremail);
                                                        Desktop desktop = Desktop.getDesktop();
                                                        try {
                                                            desktop.browse(new URI("http://mail." + domain + ".com"));
                                                        } catch (URISyntaxException e1) {
                                                        }
                                                    }
                                                } catch (Exception ex) {
                                                    Logger.getLogger(GetpieActionOld.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                break;
                                            case "error":
                                                JOptionPane.showMessageDialog(null, "Server error! Try it again later!", "Tips", JOptionPane.ERROR_MESSAGE);
                                                break;
                                            default:
                                                break;
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(GetpieActionOld.class.getName()).log(Level.SEVERE, null, ex);
                                        JOptionPane.showMessageDialog(null, "Internal error!", "Tips", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
    } 
}
