/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.utils;

import com.shafts.bridge.CheckNetWork;
import com.shafts.bridge.CheckUserStatus;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-13 10:03:13
 */
public class GetButtonName {

    public String getname(boolean iscan){
        String name = null;
        if(!iscan)
            name = "Not Actived";
        else{
             CheckUserStatus cus = new CheckUserStatus();
            try {
                int status = cus.checkauthorization();
                 switch(status){
                    case 0: name = "Arrearage";
                    break;
                    case 1: name = "Welcome";
                    break;
                    case 2: name = "Server Problem";
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(GetButtonName.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return name;
    }
    
}
