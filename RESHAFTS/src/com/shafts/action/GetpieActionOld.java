/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.action;

import com.shafts.bridge.CheckUserStatusOld;
import com.shafts.bridge.DecName;
import com.shafts.ui.GetPieOld;
import com.shafts.utils.IllegalJudge;
import com.shafts.utils.PayforPieOld;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-12 10:18:43
 */
public final class GetpieActionOld extends GetPieOld {

    public GetpieActionOld() { 
        kitkat = new PayforPieOld();
        initAction();
    }

    public void initAction() {
        /**
         * cancel
         */
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        /**
         * next
         */
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String op = jButton2.getText();
                switch (op) {
                    case "Next":
                        
                        if (jRadioButton1.isSelected()) {// renew operation
                            jButton3.setEnabled(true);
                            jLabel1.setText("Renew");
                            jLabel9.setText("Hello, " + new DecName().getAccount());
                            jButton2.setText("Renew");
                            centerPanel.removeAll();
                            centerPanel.add(centerChild2);
                            centerPanel.updateUI();
                        } else if (jRadioButton2.isSelected()) {// purchase
                            jButton3.setEnabled(true);
                            jLabel1.setText("Purchase");
                            jButton2.setText("Purchase");
                            centerPanel.removeAll();
                            centerPanel.add(centerChild1);
                            centerPanel.updateUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select one operation!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Renew":
                        kitkat.renew(chooseday, cost);
                        //dispose();
                        break;
                    case "Purchase":
                        username = jTextField3.getText();
                        usertel = jTextField2.getText();
                        useremail = jTextField1.getText();
                        kitkat.purchase(chooseday, cost, username, usertel, useremail, checkflag);
                        //dispose();
                        break;
                }
            }
        });
        /**
         * pre
         */
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                jButton3.setEnabled(false);
                jButton2.setText("Next");
                jComboBox1.setSelectedIndex(0);
                jComboBox2.setSelectedIndex(0);
                jTextField1.removeAll();
                jTextField2.removeAll();
                jTextField3.removeAll();
                jTextField4.removeAll();
                jTextField5.setText("");
                centerPanel.removeAll();
                centerPanel.add(jScrollPane1);
                centerPanel.updateUI();
            }
        });
        /**
         * check username if has been renrolled
         */
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                CheckUserStatusOld CUS = new CheckUserStatusOld();
                checkflag = 1;
                username = jTextField3.getText();
                IllegalJudge il = new IllegalJudge();
                boolean b = il.isillegal(username);
                if (username.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please input the user name!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                } else if (!b) {
                    JOptionPane.showMessageDialog(null, "The user name contains illegal characters!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    try {
                        isRegister = CUS.getcheck(username);
                    } catch (IOException ex) {
                        Logger.getLogger(GetpieActionOld.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    switch (isRegister) {
                        case "yes":
                            JOptionPane.showMessageDialog(null, "User name efficient!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                            iscan = 1;
                            break;
                        case "no":
                            JOptionPane.showMessageDialog(null, "User name has been enrolled!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
    
    public static void main(String args[]){
        new GetpieActionOld().setVisible(true);
    }
    private int checkflag = 0; //
    private int iscan = 0;
    private String username;
    private String usertel;
    private String useremail;
    private String isRegister;
    private PayforPieOld kitkat;
}
