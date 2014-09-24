package com.server.info;

import com.server.rsaencrypt.RSAUtil;
import com.server.utils.SendMail;
import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IsAuthorized {

    private int flag = 0;
    private int days = 0;

    /**
     * check the user information
     *
     * @param user_id
     * @return 0: 已到期 1：可使用 2：数据库问题
     */
    public int isauthorized(String user_id) {
        Connection con = DBConnection();
        try {
            flag = 1;
            Statement st = con.createStatement();
            days = getleftdays(user_id);
            if (days < 1) {      //已经到期
                flag = 0;
                String sql1 = "update user_info set days = 0,exec_date = null,cost = 0,reset_flag = 0 where user_name = '" + user_id + "'";
                st.executeUpdate(sql1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = 2;
        }
        return flag;
    }

    /**
     * buy product 获取用户序列号、电话、邮箱等信息，并根据用户缴费情况将记录插入数据库 插入之前先检查用户是否已经购买过此产品
     * 防止重复购买，已购买的用户只可通过续费来继续使用产品
     *
     * @param money
     * @param days
     * @param user_id
     * @param user_tel
     * @param user_email
     * @param serial
     * @return
     */
    public String buypro(int money, int days, String user_id, String user_tel, String user_email, String serial) {
        String tips;
        int shop_times = 0;
        int buy_flag = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String exec_date = df.format(date);
        Connection con = DBConnection();
        try {
            Statement st = con.createStatement();
            String sql = "select user_name from user_info";
            String sql1 = "select permission_flag from user_info where user_name = '" + user_id + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString(1).equals(user_id)) {
                    ResultSet rs1 = st.executeQuery(sql1);
                    while (rs1.next()) {
                        buy_flag = rs1.getInt(1);
                    }
                    break;

                }
            }
            if (buy_flag != 0) {
                tips = "hasbought";
            } else {//生成文件，插入新纪录
                RSAUtil rsa = new RSAUtil();
                rsa.encryptFile(user_id, serial);
                shop_times = shop_times + 1;
                String sql2 = "insert into user_info(user_name, user_tel, user_email, days, exec_date, shop_times, cost, permission_flag) values('" + user_id + "','" + user_tel + "','" + user_email + "','" + days + "','" + exec_date + "','" + shop_times + "','" + money + "',1) ";
                st.executeUpdate(sql2);
                tips = "Success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            tips = "error";
        }
        return tips;
    }

    /**
     * renew the products
     *
     * @param money
     * @param days
     * @param user_id
     * @return
     */
    public String renew(int money, int days, String user_id) {
        int shoptimes = 0;
        String renewsuc;
        Connection con = DBConnection();
        try {
            Statement st = con.createStatement();
            String sql = "select shop_times from user_info where user_name = '" + user_id + "'";
            //续费
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                shoptimes = rs.getInt(1);
            }
            shoptimes = shoptimes + 1;
            String sql1 = "update user_info set days = days + '" + days + "',cost = '" + money + "',shop_times = '" + shoptimes + "' where user_name = '" + user_id + "'";
            st.executeUpdate(sql1);
            renewsuc = "success";
        } catch (Exception e) {
            renewsuc = "error";
        }
        return renewsuc;
    }

    public void sendmail(String user_id) throws SQLException {
        Connection con = DBConnection();
        String email = null;
        String path = "E:\\Master\\MyOffice\\UserCrack\\" + user_id + "\\shafts.pem";
        File file = new File(path);
        Statement st = con.createStatement();
        String sql = "select user_email from user_info where user_name = '" + user_id + "'";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            email = rs.getString(1);
        }
        new SendMail().send(email, file);
    }

    /**
     * get left days
     *
     * @param user_id
     * @return
     */
    public int getleftdays(String user_id) {
        Connection con = DBConnection();
        try {
            Date date2;
            int authorizeday = 0;
            String date1 = null;
            String time;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Statement st = con.createStatement();
            String sql = "select exec_date,days from user_info where user_name = '" + user_id + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                date1 = rs.getString(1);
                authorizeday = rs.getInt(2);
            }
            date2 = new Date();
            time = df.format(date2);
            days = authorizeday - nDaysBetweenTwoDate(date1, time);
        } catch (SQLException | ParseException e) {
            days = -1;
        }
        return days;
    }

    /**
     * get the user phone number
     *
     * @param user_id
     * @return
     * @throws java.sql.SQLException
     */
    public String getuserphone(String user_id) throws SQLException {
        Connection con = DBConnection();
        String phone = null;
        Statement st = con.createStatement();
        String sql = "select tel from user_info where user_name = '" + user_id + "'";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            phone = rs.getString(1);
        }
        return phone;
    }

    /**
     * check Duplicated username
     *
     * @param user_id
     * @return
     */
    public String getcheck(String user_id) {
        Connection con = DBConnection();
        String Dup = "yes";
        try {
            Statement st = con.createStatement();
            String sql = "select user_name from user_info";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString(1).equals(user_id)) {
                    Dup = "no";
                }
            }
        } catch (Exception e) {
            Dup = "no";
        }
        return Dup;
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
        } catch (SQLException | ClassNotFoundException e) {
        }
        return con;
    }

    /**
     * Calculate the left days
     *
     * @param first_Date
     * @param second_Date
     * @return
     * @throws java.text.ParseException
     */
    public int nDaysBetweenTwoDate(String first_Date, String second_Date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate;
        Date secondDate;
        firstDate = df.parse(first_Date);
        secondDate = df.parse(second_Date);
        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    public static void main(String args[]) {
       // String mac = "78-AC-C0-A0-6A-93";
       //String  a = new IsAuthorized().getcheck(mac);
        //for(int i = 0;i<a.size();i++)
        //{
        //System.out.println(a);
        String name = "xiaocainiao";
        String tel = "18317067106";
        String email = "784887302@qq.com";
        int day = 180;
        int money = 45;
        String serial = "wqhje28y78932sd192";
      //  String result = new IsAuthorized().buypro(money, day, name, tel, email, serial);
        int result = new IsAuthorized().isauthorized(name);
        System.out.println(result);
        
    }
}
