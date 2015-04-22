/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Little-Kitty
 * @date 2015-4-15 19:51:09
 */

public class TipPanel extends JLabel{//extends JPanel {

    private int cordiY ;
    private String tip;
    private final Color tipColor;

    public TipPanel(String tip, String type) {
        this.tip = tip;
         //TipPanel.this.removeAll();
         //TipPanel.this.updateUI();
        if(type.equals("Error"))
            tipColor = new Color(255, 0, 0);
        else
            tipColor = new Color(0, 255, 0);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    cordiY += 1;
                    if (cordiY  > 12) {
                        //s = null;
                        close();
                        break;//
                        //y = 1;
                    }
                    TipPanel.this.repaint();
                    TipPanel.this.updateUI();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TipPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        });
        t.start();
    }
   // public void setTip(String tip, String type){
         
    //}
    
    public void close(){
        try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TipPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    cordiY -= 1;
                    if (cordiY  < -10) {
                        //s = null;
                        //close();
                        break;//
                        //y = 1;
                    }
                   TipPanel.this.repaint();
                    try {
                        Thread.sleep(40);
                    } catch (Exception e) {
                    }
                }
                
            }
        });
        t.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(tipColor);
        g.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        g.drawString(tip, 15, cordiY );
    }

}
