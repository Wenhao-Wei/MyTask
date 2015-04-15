/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.bridge;

import com.shafts.utils.InforBean;
import com.shafts.utils.RegisterBean;
import com.shafts.utils.StatusBean;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

/**
 *
 * @author Little-Kitty
 * @date 2015-4-4 16:27:29
 */
public class ServerGate {

    private GetConnection getConnection;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    //private InforBean inforBean;

    public ServerGate() {

    }

    /**
     * submit and get the register infor
     *
     * @param regBean
     * @return
     */
    public String submitInfor(RegisterBean regBean) {
        getConnection = new GetConnection();
        String regResult = null;
        socket = getConnection.connection();
        if (socket != null) {
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("REGISTER");
                os.writeObject(regBean);
                os.flush();
                //get the infor form the server
                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                regResult = is.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return regResult;
    }
    
    /**
     * submit and get the register infor
     *
     * @param userName
     * @return
     */
    public String isExpired(String userName) {
        getConnection = new GetConnection();
        String isExpired = null;
        socket = getConnection.connection();
        if (socket != null) {
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("ISEXPIRED");
                os.writeUTF(userName);
                os.flush();

                //get the infor form the server
                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                isExpired = is.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExpired;
    }

    /**
     * verify the user account
     *
     * @param name
     * @param password
     * @return
     */
    public String verifyAccount(String name, String password) {
        getConnection = new GetConnection();
        socket = getConnection.connection();
        String verResult = null;
        if (socket != null) {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("username", name);
            jsonOb.put("password", password);
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("LOGINVERIFY");
                os.writeObject(jsonOb);
                os.flush();

                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                verResult = is.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return verResult;
    }

    /**
     * forget password and find it
     *
     * @param mailAddress
     * @param userName
     * @return
     */
    public String getPassword(String mailAddress, String userName) {
        getConnection = new GetConnection();
        socket = getConnection.connection();
        String isSend = null;
        if (socket != null) {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("username", userName);
            jsonOb.put("mailbox", mailAddress);
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("GETPASSWORD");
                os.writeObject(jsonOb);
                os.flush();

                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                isSend = is.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isSend;
    }

    /**
     * check the user name if existed
     * @param name
     * @return 
     */
    public String isNameExist(String name) {
        getConnection = new GetConnection();
        socket = getConnection.connection();
        String isExisted = null;
        if (socket != null) {
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("ISNAMEEXIST");
                os.writeUTF(name);
                os.flush();

                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                isExisted = is.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExisted;
    }
 
    /**
     * get key file
     * @param mailAddress
     * @param userName
     * @return 
     */
    public String getKeyFile(String mailAddress, String userName){
        getConnection = new GetConnection();
        socket = getConnection.connection();
        String isSend = null;
        if (socket != null) {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("username", userName);
            jsonOb.put("mailbox", mailAddress);
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("GETKEYFILE");
                os.writeObject(jsonOb);
                os.flush();

                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                isSend = is.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isSend;       
    }
    /**
     * get the company infor
     * @return 
     */
    public InforBean getProInfor(){
        getConnection = new GetConnection();
        socket = getConnection.connection();
        InforBean inforBean = null;
        if (socket != null) {
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("GETCOMPANYINFOR");
                os.flush();

                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                inforBean = (InforBean) is.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return inforBean;
    }
   /**
    * get order infor
     * @param userName
    * @return 
    */ 
    public StatusBean getStatusInfor(String userName){
        getConnection = new GetConnection();
        socket = getConnection.connection();
        StatusBean statusBean = null;
        if (socket != null) {
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.writeUTF("GETSTATUS");
                os.writeUTF(userName);
                os.flush();

                is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                statusBean = (StatusBean) is.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerGate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return statusBean;
    }
    
     /**
     * verify the user
     * @return
     */
    public boolean verify() {
        boolean reach = false;
        String part1 = new ClientInfo().getDst();
        String part2 = new DecName().getAccount();
        StringBuilder sb = new StringBuilder(part1);
        sb.insert(8, part2);
        String fin = sb.toString();
        String key = new OpenDoor().getBar();
        if (fin.equals(key)) {
            reach = true;
        }
        return reach;
    }
}


