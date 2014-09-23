package com.server.utils;

import java.io.File;

/**
 *
 * @author Little-Kitty company CHINA ECUST.
 * @version V1.0 date: 2014年9月5日 下午6:26:47
 */
public class SendMail {

    /**
     * send email
     *
     * @param reciever
     * @param key
     */
    public void send(String reciever, File file) {
        String smtp = "smtp.163.com";// smtp服务器
        String from = "shaftsmail@163.com";// 邮件显示名称
        String to = reciever;
        String copyto = "";// 抄送人邮件地址
        String subject = "shafts crack file";// 邮件标题
        String content = "该邮件为系统邮件，请勿回复。";
         String path = "E:\\Master\\MyOffice\\NetBeansWorkspace\\NewSHAFTS\\workspace\\download\\19895\\DB00861.mol2";
       // File attachment = new File(path);  //附件
        String username = "shaftsmail";// 发件人真实的账户名
        //username = encode(username.getBytes());
        String password = "ecust200237";// 发件人密码
        Mail.sendAndCc(smtp, from, to, copyto, subject, content, file, username, password);
    }

    public static void main(String[] args) {
        String smtp = "smtp.163.com";// smtp服务器
        String to = "shaftsmial@126.com";// 收件人的邮件地址，必须是真实地址
        String from = "shaftsmail@163.com";// 邮件显示名称
        String copyto = "";// 抄送人邮件地址
        String subject = "测试邮件";// 邮件标题
        String content = "<tr>该邮件为系统邮件，请勿回复</tr> 谢谢使用";// 邮件内容
        String path = "E:\\Master\\MyOffice\\NetBeansWorkspace\\NewSHAFTS\\workspace\\download\\19895\\DB00861.mol2";
        File attachment = new File(path);  //附件
        String username = "shaftsmail";// 发件人真实的账户名
        //username = encode(username.getBytes());
        String password = "ecust200237";// 发件人密码
        //password = encode(password.getBytes());
        Mail.sendAndCc(smtp, from, to, copyto, subject, content, attachment, username, password);
    }
}
