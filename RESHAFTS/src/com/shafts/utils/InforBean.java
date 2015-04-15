/*
 * create a object 
 */

package com.shafts.utils;

import java.io.Serializable;
import java.util.Vector;
/**
 * @author Little-Kitty
 * @date 2015-4-4 16:04:58
 */
public class InforBean implements Serializable{
    
    private static final long serialVersionUID=1L;
    
    private Vector accountVec;
    private Vector phoneVec;
    private Vector mailVec;

    public Vector getAccountVec() {
        return accountVec;
    }

    public void setAccountVec(Vector accountVec) {
        this.accountVec = accountVec;
    }

    public Vector getPhoneVec() {
        return phoneVec;
    }

    public void setPhoneVec(Vector phoneVec) {
        this.phoneVec = phoneVec;
    }

    public Vector getMailVec() {
        return mailVec;
    }

    public void setMailVec(Vector mailVec) {
        this.mailVec = mailVec;
    } 

}
