/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.bridge;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Little-Kitty
 * @date 2015-4-5 13:53:14
 */
public class GetConnection {
    private Socket socket = null;
    private final String ip = "localhost";// server IP
    private final int port = 8821;  //8821
    
    /**
     * connect the server
     * @return 
     */
    public Socket connection() {
        try {
            socket = new Socket(ip, port);
            return socket;
        } catch (IOException ex) {
            if(socket != null)
                try {
                    socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(GetConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(GetConnection.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Connection Exception!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }        
    }

}
