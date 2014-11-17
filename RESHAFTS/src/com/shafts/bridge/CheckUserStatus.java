package com.shafts.bridge;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class CheckUserStatus {

    private Socket socket = null;
    private final String ip = "localhost";// server IP
    private String userphone = null;
    private final int port = 8821;  //8821
    private int flag;
    private int leftdays;
    private boolean isconnect;
    private DataOutputStream out = null;
    private DataInputStream getMessageStream = null;
    private final String userid = new SetorGet().getstr();

    /**
     * connect the server
     *
     * @throws java.io.IOException
     */
    public void connection() throws IOException {
        try {
            socket = new Socket(ip, port);
            isconnect = true;
        } catch (Exception e) {
            if (socket != null) {
                socket.close();
            }
            isconnect = false;
        }
    }

    /**
     * chek the authorized status overdue or continue
     *
     * @return
     * @throws java.io.IOException
     */
    public int checkauthorization() throws IOException {
        connection();
        if (isconnect) {
            if (userid != null) {
                out = new DataOutputStream(socket.getOutputStream());
                out.writeInt(1);
                out.writeUTF(userid);
                out.flush();

                getMessageStream = new DataInputStream(new BufferedInputStream(
                        socket.getInputStream()));
                flag = getMessageStream.readInt();
            }
        }
        return flag;
    }

    /**
     * check user left days
     *
     * @return
     * @throws java.io.IOException
     */
    public int getleftdays() throws IOException {
        connection();
        if (isconnect) {
            if (userid != null) {
                out = new DataOutputStream(socket.getOutputStream());
                GetWindowsMACAddress getmac = new GetWindowsMACAddress();
                String mac = getmac.getAddress();
                out.writeInt(2);
                out.writeUTF(userid);
                out.flush();

                getMessageStream = new DataInputStream(new BufferedInputStream(
                        socket.getInputStream()));
                leftdays = getMessageStream.readInt();
            }

        }
        return leftdays;
    }

    /**
     * pay for use the system
     *
     * @param days
     * @param money
     * @param username
     * @param phonenumber
     * @param email
     * @return
     * @throws java.io.IOException
     */
    public String buypro(int days, int money, String username, String phonenumber, String email) throws IOException {
        String message = null;
        String serial = new ClientInfo().getdst();
        connection();
        if (isconnect) {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(3);
            out.writeUTF(username);
            out.writeInt(days);
            out.writeInt(money);
            out.writeUTF(phonenumber);
            out.writeUTF(email);
            out.writeUTF(serial);
            out.flush();
            getMessageStream = new DataInputStream(new BufferedInputStream(
                    socket.getInputStream()));
            message = getMessageStream.readUTF();
        }
        return message;
    }

    /**
     * verify the user
     *
     * @return
     */
    public boolean verify() {
        boolean reach = false;
        String part1 = new ClientInfo().getdst();
        String part2 = new SetorGet().getstr();
        StringBuilder sb = new StringBuilder(part1);
        sb.insert(8, part2);
        String fin = sb.toString();
        String key = new Opendoor().getBar();
        if (fin.equals(key)) {
            reach = true;
        }
        return reach;
    }

    /**
     * renew for extend the term
     *
     * @param money
     * @param days
     * @return
     * @throws java.io.IOException
     */
    public String renew(int money, int days) throws IOException {
        String renewsuc = null;
        connection();
        if (isconnect) {
            if (userid == null) {
                JOptionPane.showMessageDialog(null, "New user! Please purchase the product first!");
            } else {
                out = new DataOutputStream(socket.getOutputStream());
                out.writeInt(4);
                out.writeUTF(userid);
                out.writeInt(days);
                out.writeInt(money);
                out.flush();

                getMessageStream = new DataInputStream(new BufferedInputStream(
                        socket.getInputStream()));
                renewsuc = getMessageStream.readUTF();
            }
        }
        return renewsuc;
    }

    /**
     * get user phone
     *
     * @return
     * @throws java.io.IOException
     */
    public String getusertel() throws IOException {
        connection();
        if (isconnect) {
            if (userid != null) {
                out = new DataOutputStream(socket.getOutputStream());
                out.writeInt(5);
                out.writeUTF(userid);
                out.flush();

                getMessageStream = new DataInputStream(new BufferedInputStream(
                        socket.getInputStream()));
                userphone = getMessageStream.readUTF();
            }
        }
        return userphone;
    }

    /**
     * check equals error or null if hava network problem
     *
     * @param username
     * @return
     * @throws java.io.IOException
     */
    public String getcheck(String username) throws IOException {
        String check = null;
        connection();
        if (isconnect) {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(6);
            out.writeUTF(username);
            out.flush();

            getMessageStream = new DataInputStream(new BufferedInputStream(
                    socket.getInputStream()));
            check = getMessageStream.readUTF();
        }
        return check;
    }

    /**
     * send the information of off line
     *
     * @throws java.io.IOException
     */
    public void offline() throws IOException {
        connection();
        if (isconnect && userid != null) {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(7);
            out.writeUTF(userid);
            out.flush();
        }
    }

    public static void main(String args[]) throws IOException {
        //GetWindowsMACAddress getmac = new GetWindowsMACAddress();
        //String mac = getmac.getAddress();
        //System.out.println(mac);
        //boolean f = new CheckUserStatus().verify();
       int f = new CheckUserStatus().checkauthorization();
        System.out.println(f);
    }
}
