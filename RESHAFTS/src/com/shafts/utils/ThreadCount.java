/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.utils;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-21 16:22:59
 */
public class ThreadCount {
    private int i = 0;
    public void addThreadNum(){
        this.i = i + 1;
    }
    public void deThreadNum(){
        this.i = i - 1;
    }
    public int getThreadNum(){
        return i;
    }
}
