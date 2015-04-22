/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.ui;

import com.shafts.bridge.ServerGate;
import com.shafts.render.TipPanel;
import com.shafts.render.TipsRender;
import com.socket.bean.InforBean;
import com.shafts.utils.PatchDoor;
import java.awt.Dimension;
import java.awt.Toolkit;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.File;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Little-Kitty
 */
public class EnterKeyUI extends javax.swing.JDialog {

    /**
     * Creates new form EnterKeyUI
     *
     * @param isOrder
     * @param userName
     */
    public EnterKeyUI(boolean isOrder, String userName) {
        setModal(true);
        this.isLock = !isOrder;
        this.userName = userName;
        initAccount();        
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * get company infor
     */
    private void initAccount() {
         serGate = new ServerGate();
         InforBean inforBean = (InforBean) serGate.getProInfor();
         if (inforBean != null) {
         this.accountVec = inforBean.getAccountVec();
         this.phoneVec = inforBean.getPhoneVec();
         this.mailVec = inforBean.getMailVec();
         }
        accountVec = new Vector();
        phoneVec = new Vector();
        mailVec = new Vector();
        for (int i = 0; i < 5; i++) {
            Vector a = new Vector();
            a.add("ABC");
            a.add("321897348274321423874");
            a.add("ecust");
            accountVec.add(a);
        }
        for (int i = 0; i < 5; i++) {
            Vector a = new Vector();
            a.add("魏先生");
            a.add("18317067106");
            phoneVec.add(a);
        }
        for (int i = 0; i < 5; i++) {
            Vector a = new Vector();
            a.add("魏先生");
            a.add("784887302@qq.com");
            mailVec.add(a);
        }
        //get account ifor
        String accInfor = "";
        if (accountVec != null) {
            for (int i = 0; i < accountVec.size(); i++) {
                Vector account = (Vector) accountVec.get(i);
                accInfor += "<tr>\n"
                        + "<th>" + account.get(0) + "</th>\n"
                        + "<th>" + account.get(1) + "</th>\n"
                        + "<th>" + account.get(2) + "</th>\n"
                        + "</tr>\n";
            }
        }
        //get contacts infor
        String phoInfor = "";
        if (phoneVec != null) {
            for (int i = 0; i < phoneVec.size(); i++) {
                Vector phone = (Vector) phoneVec.get(i);
                phoInfor += "&nbsp;&nbsp;&nbsp;&nbsp;" + phone.get(0) + " :  " + phone.get(1);
                if ((i + 1) % 2 == 0) {
                    phoInfor += "<br/>\n";
                }
            }
        }
        //get email
        String mailInfor = "";
        if (mailVec != null) {
            for (int i = 0; i < mailVec.size(); i++) {
                Vector mail = (Vector)mailVec.get(i);
                mailInfor += "&nbsp;&nbsp;&nbsp;&nbsp;" + mail.get(0) + " : " + mail.get(1);
                if ((i + 1) % 2 == 0) {
                    mailInfor += "<br/>\n";
                }
            }
        }

        infor = "<html>\n"
                + "  <head>\n"
                + "  </head>\n"
                + "  <body>\n"
                + "<center><h2>Order Notice</h2></center>\n"
                + "<hr>\n"
                + "<p>\n"
                + "<font face=\"微软雅黑\" size=\"4\"><strong>Illustration</strong><font>\n"
                + "<hr width=\"150\" color=\"#996600\" align=\"left\">\n"
                + "<font face=\"微软雅黑\" size=\"4\">\n"
                + "&nbsp;&nbsp;&nbsp;&nbsp;Partial function of the product need to pay to use.For better \n"
                + "product experience you can get, we sincerely suggest you get authorization \n"
                + "by purchase.</font><br/></p>\n"
                + "<p>\n"
                + "<font face=\"微软雅黑\" size=\"4\">\n"
                + "&nbsp;&nbsp;&nbsp;&nbsp;Once you have gotten the license of the product and installed it \n"
                + "successfully, you can enjoy all the function of the product unless have expired the \n"
                + "authorizations.If your license has expired, you can get the right of use of the product \n"
                + "by renew, and thankfully, you need not install any other license again.</font><br/> </p>\n"
                + "<p>\n"
                + "<font face=\"微软雅黑\" size=\"4\"><strong>Order Procedure</strong> <font>\n"
                + "<hr width=\"150\" color=\"#996600\" align=\"left\">\n"
                + "<font face=\"微软雅黑\" size=\"4\">\n"
                + "&nbsp;&nbsp;&nbsp;&nbsp;For acquiring the license or the right of use of the product. You need to go to a bank to make remittance for completing the transaction, we will provide you with some available accounts.We will send the <strong>key file</strong> to your mailbox which you had been registered as fast as we can when we get the remittance<br/>\n"
                + "<font face=\"微软雅黑\" size=\"4\" color=\"#FF0000\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Attention: You must give clear indication of your user name which you registered when you make remittance.</strong></font>\n"
                + "</p><br/>\n"
                + "<p>\n"
                + "<font face=\"微软雅黑\" size=\"4\">\n"
                + "&nbsp;&nbsp;&nbsp;&nbsp;We will accept your request within 24 hours after we received the remittance. The available account as follows:<br/>\n"
                + "<table align=\"center\" border=\"1\">\n"
                + "<caption><font size=\"5\"><strong>Company account</strong></font></caption>\n"
                + "<tr>\n"
                + "<th>Bank name</th>\n"
                + "<th>Card ID</th>\n"
                + "<th>Account holder</th>\n"
                + "</tr>\n" + accInfor
                + "</table>\n"
                + "<p>\n"
                + "<font face=\"微软雅黑\" size=\"4\">\n"
                + "<strong>&nbsp;&nbsp;&nbsp;&nbsp;If you have any questions at any time, please contacts us:</strong><br/>\n"
                + "<strong>Contacts:</strong><br/>\n" + phoInfor
                + "<br/><strong>E-mail:</strong><br/>\n" + mailInfor
                + "<br/>"
                + "\n"
                + "\n"
                + "  </body>\n"
                + "</html>\n";
    }

    /**
     * Creates new form EnterKeyUI
     *
     * @param parent
     * @param modal
     */
    public EnterKeyUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAccount();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tipPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Install key"));

        jLabel1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel1.setText("Key File:");

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N

        jButton1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButton1.setText("Browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButton2.setText("Install");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButton4.setText("Cancel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 255));
        jLabel2.setText("Lost the key?");
        jLabel2.setToolTipText("We will send the key file to your mailbox if you have purchase the product.");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });

        tipPanel.setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tipPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addComponent(jButton4))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tipPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Install key"));

        jTextPane2.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        jTextPane2.setForeground(new java.awt.Color(0, 0, 204));
        jTextPane2.setText("  You have installed the key file, and needn't install it anymore. If you want to renew for your function, just read the \"Order notice\" above to know more about how renew it.");
        jScrollPane2.setViewportView(jTextPane2);

        jButton3.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButton3.setText("OK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(433, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        if(isLock)
        jPanel2.add(jPanel3);
        else

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ACTIVE");
        setResizable(false);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Order Notice"));

        jTextPane1.setEditable(false);
        jTextPane1.setContentType("text/html"); // NOI18N
        jTextPane1.setText(infor);
        jTextPane1.setCaretPosition(0);
        jScrollPane1.setViewportView(jTextPane1);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));
        if(isLock)
        jPanel2.add(jPanel3);
        else
        jPanel2.add(jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        // TODO add your handling code here:
        jLabel2.setForeground(new java.awt.Color(0, 102, 255));
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        // TODO add your handling code here:
        jLabel2.setForeground(new java.awt.Color(255, 102, 0));
    }//GEN-LAST:event_jLabel2MouseEntered

    //reget the keyfile
    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        new ReGetKeyUI(userName).setVisible(true);

    }//GEN-LAST:event_jLabel2MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    //install the key
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        TipsRender tipRender = new TipsRender();
        String keyPath = jTextField1.getText();
        if (!keyPath.equals("")) {
            new PatchDoor().insall(keyPath);
        } else {
            tipPanel.removeAll();
            tipPanel.add(new TipPanel("Choose a key file!", "Error"));
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    //CHOOSE the key file
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith("pfx");
            }

            @Override
            public String getDescription() {
                // TODO Auto-generated method stub
                return "Files(*.pfx)";
            }
        });
        jfc.showDialog(new JLabel(), "choose");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EnterKeyUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EnterKeyUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EnterKeyUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EnterKeyUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               // EnterKeyUI dialog = new EnterKeyUI(new javax.swing.JFrame(), true);
                EnterKeyUI dialog = new EnterKeyUI(false, "xiaocainiao");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private boolean isLock;
    private String userName;
    private String infor;
    private Vector accountVec;
    private Vector phoneVec;
    private Vector mailVec;
    private ServerGate serGate;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JPanel tipPanel;
    // End of variables declaration//GEN-END:variables
}
