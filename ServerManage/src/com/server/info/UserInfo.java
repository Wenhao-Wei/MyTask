package com.server.info;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class UserInfo {
    private Vector columnNames;
    private Vector userData;

    /**
     * initial the table column
     *
     * @return
     *
     */
    public Vector getcolumn() {
        columnNames = new Vector();
        //columnNames.add("Query");  //****Query
        columnNames.add("User_tel");
        columnNames.add("User_emal");
        columnNames.add("Days");
        columnNames.add("Exec_date");       
        columnNames.add("Shop_Times");
        columnNames.add("Cost");
        columnNames.add("Permission_flag");
        return columnNames;
    }

    /**
     * return the user data
     *
     * @param userid
     * @return
     */
    public Vector getdata(String userid) {
        Vector v = new Vector();
        userData = new Vector();
        Connection con = DBConnection();
        try {
            Statement st = con.createStatement();
            String sql = "select user_tel, user_email, days, exec_date, shop_times, cost, permission_flag from user_info where user_name = '" + userid + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userData.add(v);
        return userData;
    }

    /**
     * connect database
     *
     * @return
     */
    private Connection DBConnection() {
        Connection con = null;
        //String url = "jdbc:odbc:userconf";
        String url = "jdbc:mysql://localhost:3306/chemmapper";
        String username = "root";
        String password = "1133557799";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return con;
    }
}
