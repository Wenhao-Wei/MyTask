/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.ui;

import com.shafts.render.TipsRender;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.apache.poi.hssf.usermodel.*;

/**
 *
 * @author Little-Kitty
 */
public class ExportXlsUI extends javax.swing.JDialog {

    /**
     * Creates new form ExprotXlsUI
     *
     * @param table
     * @param filepath
     */
    public ExportXlsUI(JTable table, String filepath) {
        this.table = table;
        this.filepath = filepath;
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screensize = kit.getScreenSize();
        int width = screensize.width;
        int height = screensize.height;
        int x = (width - WIDTH) / 2;
        int y = (height - HEIGHT) / 2;
        tip = new TipsRender();
        setLocation(x - 50, y - 100);
        initComponents();
    }

    class ImagePanel extends JPanel {

        public void paint(Graphics g) {
            super.paint(g);
            //String path = System.getProperty("user.dir") + "\\Pictures\\004.gif";
            String path = System.getProperty("user.dir") + "\\Pictures\\004.gif";
            ImageIcon icon = new ImageIcon(path);
            g.drawImage(icon.getImage(), 0, 0, 350, 10, this);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        TipLabel = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();

        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);
        buttonGroup1.add(jRadioButton3);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 210));

        jRadioButton1.setBackground(new java.awt.Color(51, 51, 51));
        jRadioButton1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setText("Completely Export");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setBackground(new java.awt.Color(51, 51, 51));
        jRadioButton2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setText("Custom Exprot 2 (Based on the range of row)");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Start Point:");
        jLabel1.setEnabled(false);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(51, 51, 51));
        jTextField1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("0");
        jTextField1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTextField1.setCaretColor(new java.awt.Color(255, 255, 255));
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        // 限制只能输入数字
        jTextField1.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
                } else {
                    e.consume(); // 屏蔽掉非法输入
                }
            }
        });

        jLabel2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("(Point Here!)");
        jLabel2.setToolTipText("The number should be greater than 0!");

        jLabel3.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("End Point:");
        jLabel3.setEnabled(false);

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(51, 51, 51));
        jTextField2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setText("0");
        jTextField2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTextField2.setCaretColor(new java.awt.Color(255, 255, 255));
        // 限制只能输入数字
        jTextField2.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
                } else {
                    e.consume(); // 屏蔽掉非法输入
                }
            }
        });

        jLabel4.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("(Point Here!)");
        jLabel4.setToolTipText("The number should be greater than the starting point!");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jSeparator1.setBackground(new java.awt.Color(51, 51, 51));
        jSeparator1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.add(jSeparator1);

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        TipLabel.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        TipLabel.setForeground(new java.awt.Color(0, 255, 255));

        jRadioButton3.setBackground(new java.awt.Color(51, 51, 51));
        jRadioButton3.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jRadioButton3.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton3.setText("Custom Exprot 1 (Based on selected item)");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jRadioButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jRadioButton1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jRadioButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TipLabel, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TipLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jLabel1.setEnabled(false);
        jLabel3.setEnabled(false);
        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
        jPanel1.updateUI();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jLabel1.setEnabled(true);
        jLabel3.setEnabled(true);
        jTextField1.setEditable(true);
        jTextField2.setEditable(true);
        jPanel1.updateUI();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (jRadioButton1.isSelected()) {
            exportResult(table, 0, 1000,false);           
        } else if (jRadioButton2.isSelected()) {
            int i = Integer.parseInt(jTextField1.getText());
            int j = Integer.parseInt(jTextField2.getText());
            if (i < 0 || i > j) {
                if (i < 0) 
                    tip.render(TipLabel, "The start number can't smaller than 0!", "Error");
else 
                    tip.render(TipLabel, "Parameter selection error!", "Error");
            } else {
                exportResult(table, i, j, false);
            }
        }
        else if(jRadioButton3.isSelected()){
            exportResult(table, 0, 1000,true);
        }
        else 
            tip.render(TipLabel, "Choose the export model!", "Error");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
       jLabel1.setEnabled(false);
        jLabel3.setEnabled(false);
        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
        jPanel1.updateUI();
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    protected void exportResult(JTable table, int i, int j, boolean exportflag) {
        String outFilePath;
        int RowCount = table.getRowCount();
            if (j >= RowCount) {
                j = RowCount;
            }
        //String temp = (String) table.getValueAt(0, 1);
        if (table.getRowCount() == 1 || !table.isValid()) //temp.equals("")
             tip.render(TipLabel, "No any data could export!", "Error");
        else {
            boolean selectFlag = false;
            for(int num = 0; num < j; num ++){
                selectFlag = (boolean)table.getValueAt(num, 5);
                if(selectFlag)
                    break;
            }
            if(exportflag && !selectFlag)
                tip.render(TipLabel, "No any data selected!", "Error");
            else{
            JFileChooser fc = new JFileChooser(
                    System.getProperty("user.dir"));
            fc.setMultiSelectionEnabled(false);
            fc.setName("Choose directory...");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fc.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                 tip.render(TipLabel, "Exporting...", "Tip");
                 jPanel2.removeAll();
                JPanel panel = new ImagePanel();
                jPanel2.add(panel);
                jPanel2.updateUI();
                File file = fc.getSelectedFile();
                if(!file.exists())
                    file.mkdir();
                outFilePath = file.getAbsolutePath() + "//"; // + "//"+ (int)(89999999*Math.random()+10000000) + "" + "//";
                //File file1 = new File(outFilePath);
                //file1.mkdir();
                exportXls(table, outFilePath, i, j, exportflag);
                exportFile(table, outFilePath, i, j, exportflag);
                new Thread(){
                    public void run(){
                        int k = 5;
                        while(k > 0){
                            TipLabel.setText("Export complete! Close window: " + k + "s.");
                     try {
                         Thread.sleep(1000);
                     } catch (InterruptedException ex) {
                         Logger.getLogger(ExportXlsUI.class.getName()).log(Level.SEVERE, null, ex);
                     }
                     k--;
                        }
                 dispose();
            }
                }.start();
                        }

        }
        }
    }

    protected void exportFile(JTable table, String outFilePath, int i, int j, boolean flag){
        while(i < j){
           // if(i != 0){
                boolean selected = (boolean)table.getValueAt(i, 5);
                if((flag && selected) || !flag){
                String MolName = table.getModel().getValueAt(i, 1).toString();
                File mol = new File(filepath + MolName + ".mol2");
                if(!mol.exists())
                    MolName = table.getModel().getValueAt(i, 0).toString();
                String newPath = filepath + MolName + ".mol2";
                copyfile(newPath, outFilePath + MolName + ".mol2");
           // }
            }
            i ++;
        }
    }
    protected void copyfile(String oldPath, String newPath){
       try {
           int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = null;            
                //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024 * 5];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;   //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExportXlsUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExportXlsUI.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    /**
     * Realize export the result from the table
     *
     * @param table
     * @param outFilePath
     * @param i Start position
     * @param j End position
     */
    protected boolean exportXls(JTable table, String outFilePath, int i, int j, boolean exportModel) {

        boolean ExportFlag = false;
        String excelFileName = null;
        excelFileName = outFilePath + "result.xls";
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0, "分子相似性对比结果");
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell1 = row.createCell(0); // 第一列
            HSSFCell cell2 = row.createCell(1);
            HSSFCell cell3 = row.createCell(2);
            HSSFCell cell4 = row.createCell(3);
            HSSFCell cell5 = row.createCell(4);
                            //HSSFCell cell6 = row.createCell(5);
            // 定义单元格为字符串类型
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell3.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell4.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell5.setCellType(HSSFCell.CELL_TYPE_STRING);
            //cell6.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue("Rank");
            cell2.setCellValue("Name");
            cell3.setCellValue("HybridScore");
            cell4.setCellValue("ShapeScore");
            cell5.setCellValue("FeatureScore");
            //cell6.setCellValue("Query");
            int Row = 1;
           // int RowCount = table.getRowCount();
            //if (j >= RowCount) {
              //  j = RowCount;
           // }
            int k; // k为列
            while (i < j) { // i为表的行数 第一行为表头
                if(exportModel && (boolean)table.getValueAt(i, 5)){
                row = sheet.createRow(Row);
                for (k = 0; k < 5; k++) {
                    HSSFCell cell = row.createCell(k);
                    String s = (String) table.getValueAt(i, k);
                    // 设置单元格格式
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(s);
                }
                Row++;
                }
                else if(!exportModel){
                    row = sheet.createRow(Row);
                for (k = 0; k < 5; k++) {
                    HSSFCell cell = row.createCell(k);
                    String s = (String) table.getValueAt(i, k);
                    // 设置单元格格式
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(s);
                }
                Row++;
                }
                i++;
            }
            // 建立一个文件输出流
            FileOutputStream fOut = new FileOutputStream(excelFileName);
            // 把相应的Excel工作薄存盘
            workbook.write(fOut);
            // 操作结束，关闭文件
            fOut.flush();
            fOut.close();
            ExportFlag = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportXlsUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportXlsUI.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return ExportFlag;
    }

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
            java.util.logging.Logger.getLogger(ExportXlsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExportXlsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExportXlsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExportXlsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JTable table = new JTable();
                ExportXlsUI dialog = new ExportXlsUI(table, System.getProperty("user.dir"));
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

    private JTable table;
    private String filepath;
    private final int HEIGHT = 210;
    private final int WIDTH = 350;
    private TipsRender tip;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TipLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
