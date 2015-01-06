/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.action;

import com.shafts.ui.MainUI;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-15 13:07:31
 */
public class LaunchAction {

    public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
            */
            try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
            javax.swing.UIManager.setLookAndFeel(info.getClassName());
            break;
            }
            }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            
            //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainAction f = new MainAction();
                
                f.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        String content = "Are you sure to close the application?";
                        int status = 0; // no job running
                        if(f.getThreadCount() > 0){
                            content = "There are " + f.getThreadCount() + " jobs are running! Do you want keep it running in the background even \n close the SHAFTS?";
                            status = 1;
                        }
                        int result = JOptionPane.showConfirmDialog(f, content , "Tips",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // new Thread(){
                            // public void run(){
                            //CheckUserStatus CUS = new CheckUserStatus();
                           // try {
                               // CUS.offline();
                          //  } catch (IOException ex) {
                             //   Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                           // }
                            // }
                            // }.start();
                            //System.exit(0);
                            if(status == 1){
                            new Thread(){
                                @Override
                                public void run(){
                                    while(true){
                                        if(f.getThreadCount() == 0)
                                            break;
                                        try {
                                            Thread.sleep(60000);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(LaunchAction.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    System.exit(0);
                                }
                            }.start();
                            f.dispose();
                            }
                            else 
                                System.exit(0);
                        }
                        else{
                            if(status == 1)
                                System.exit(0);
                        }

                    }

                });
            }
        });
    }
}
