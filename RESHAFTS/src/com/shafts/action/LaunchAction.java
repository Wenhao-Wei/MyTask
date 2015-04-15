/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.action;

import com.shafts.ui.MainUI;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

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
           /* for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
            javax.swing.UIManager.setLookAndFeel(info.getClassName());
            break;
            }
            }*/
                BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            //BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            Border bd = new org.jb2011.lnf.beautyeye.ch8_toolbar.BEToolBarUI.ToolBarBorder(UIManager.getColor(Color.GRAY)//Floatable 时触点的颜色 
                                   ,UIManager.getColor(Color.BLUE)//Floatable 时触点的阴影颜色 
                    ,new Insets(2, 2, 2, 2)); //border 的默认insets
            UIManager.put("ToolBar.border",new BorderUIResource(bd)); 

            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
           //UIManager.put("ToolBar.isPaintPlainBackground",Boolean.TRUE);
            UIManager.put("RootPane.setupButtonVisible", false);
            UIManager.put("TabbedPane.tabAreaInsets",new javax.swing.plaf.InsetsUIResource(2, 5, 2, 5));
            //new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (Exception ex) {
            Logger.getLogger(LaunchAction.class.getName()).log(Level.SEVERE, null, ex);
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
                            content = "There are " + f.getThreadCount() + " jobs are running! Do you want keep it running in the \n background even close the SHAFTS?";
                            status = 1;
                        }
                        int result = JOptionPane.showConfirmDialog(f, content , "Tips",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
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
    public final MainAction f = new MainAction();
}
