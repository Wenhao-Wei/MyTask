/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.render;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Little-Kitty
 * @date 2014-11-7 14:09:02
 */
public class TipsRender {

    public void render(final JLabel label, String str, String tipModel) {
        if (!(label.getText().equals(""))) {
            label.removeAll();
        }
        switch (tipModel) {
            case "Error":
                label.setForeground(new java.awt.Color(255, 0, 0));
                break;
            case "Tip":
                label.setForeground(new java.awt.Color(0, 255, 255));
                break;
        }
        label.setText(str);
        label.setToolTipText(str);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);;
                    label.setText("");
                } catch (InterruptedException ex) {
                    Logger.getLogger(TipsRender.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }.start();

    }
}
