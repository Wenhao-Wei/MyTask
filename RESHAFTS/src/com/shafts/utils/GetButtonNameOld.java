/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.utils;

import com.shafts.bridge.CheckNetWork;
import com.shafts.bridge.CheckUserStatusOld;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-13 10:03:13
 */
public class GetButtonNameOld {

    public String getname(boolean iscan){
        String name = null;
        if(!iscan)
            name = "Not Actived";
        else{
             CheckUserStatusOld cus = new CheckUserStatusOld();
             int status = 0;
            try {
                status = cus.checkauthorization();
            } catch (IOException ex) {
                Logger.getLogger(GetButtonNameOld.class.getName()).log(Level.SEVERE, null, ex);
            }
             switch(status){
                 case 0: name = "Arrearage";
                 break;
                 case 1: name = "Welcome";
                 break;
                 case 2: name = "Server Problem";
                 break;
             }
        }
        return name;
    }
    
}
