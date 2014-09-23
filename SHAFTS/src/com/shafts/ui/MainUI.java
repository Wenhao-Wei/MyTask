/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.ui;

import com.shafts.application.CreateMolecule;
import com.shafts.application.JobInfor_XML;
import com.shafts.application.Smilarity;
import com.shafts.utils.CheckHeaderCellRenderer;
import com.shafts.utils.CheckTableModle;
import com.shafts.utils.CheckUserStatus;
import com.shafts.utils.HttpInvokerClient;
import com.shafts.utils.InitVector;
import com.shafts.utils.JmolPanel1;
import com.shafts.utils.ListFinder;
import com.shafts.utils.MyCellRenderer;
import com.shafts.utils.PropertyConfig;
import com.shafts.utils.SFTPConnection;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Little-Kitty
 */
public class MainUI extends javax.swing.JFrame {

    /**
     * Creates new form MainUI
     */
    public MainUI() {
        Color color = new Color(0, 51, 51);
        getContentPane().setBackground(color);
        PropertyConfig pc = new PropertyConfig();
        ArrayList<String> a = pc.readProperties();
        if (a == null) {
            new LaunchpathSetUI().setVisible(true);
        } else if ((pc.getProperty("showagain")).equals("NO")) {
            new LaunchpathSetUI().setVisible(true);

        }
        ArrayList<String> a1 = pc.readProperties();
        workPath = a1.get(1);
        File file = new File(workPath);
        if (!file.exists()) {
            file.mkdir();
        }
        localworkPath = workPath + "\\localwork\\";
        File file4 = new File(localworkPath);
        if (!file4.exists()) {
            file4.mkdir();
        }
        String networkPath = workPath + "\\network\\";
        File file1 = new File(networkPath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        targetworkPath = networkPath + "\\Target Navigator\\";
        File file2 = new File(targetworkPath);
        if (!file2.exists()) {
            file2.mkdir();
        }
        hitworkPath = networkPath + "\\Hit Explorer\\";
        File file3 = new File(hitworkPath);
        if (!file3.exists()) {
            file3.mkdir();
        }
        defaultPath = System.getProperty("user.dir");
        createfilePath = defaultPath + "\\files\\";
        String keypath = defaultPath + "\\configuration\\shafts.pem";
        File file5 = new File(keypath);
        if (!file5.exists()) {
            iscan = false;
            new Thread() {
                public void run() {
                    new GetenjoyUI().setVisible(true);
                }
            }.start();
        } else {
            try {
                iscan = new CheckUserStatus().verify();
            } catch (Exception ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                iscan = false;
            }
        }
        setLocation(10, 10);
        initComponents();
        initwork();
        IV = new InitVector();
    }

    /**
     * 2D molecule edit
     *
     * @author baobao
     *
     */
    class TwoDThread extends Thread {

        private long lastmodified1 = 0;
        private final int SLEEP_TIME = 500;
        File tempfile;

        @SuppressWarnings("static-access")
        @Override
        public void run() {
            tempfile = new File(CreateMolecule.RENDER_FILE_NAME);
            String tempfilePath = tempfile.getAbsolutePath();

            jTextField1.setText(tempfile.getName());
            fileName = tempfile.getName();

            while (true) {
                if (tempfile.exists() && tempfile.lastModified() > lastmodified1) {
                    jmolPanel.viewer.openFile(tempfilePath);// Open CreateMolecule.RENDER_FILE_NAME
                    filePath = createfilePath + tempfile.getName();
                    File file = new File(filePath);
                    System.out.println("??????????" + filePath);
                    if (file.exists()) {
                        file.delete();
                    }
                    tempfile.renameTo(file);
                    jmolPanel.viewer.openFile(filePath);
                    jmolPanel.viewer.evalString("dots on");
                    lastmodified1 = tempfile.lastModified();
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    // ignore the exception caused by the application's
                    // close

                }
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            if (tempfile.exists()) {
                tempfile.delete();
            }
        }
    }

    /**
     * show molecule
     *
     * @author Little-Kitty
     *
     */
    class ShowMol extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {// left click� 
                String stringName[] = new String[20];
                int colummCount = jTable.getModel().getColumnCount();// get table column count					 
                for (int i = 0; i < colummCount - 1; i++) {
                    stringName[i] = jTable.getModel().getValueAt(jTable.getSelectedRow(), i).toString();
                }
                show3Dname = stringName[1];
                String path1 = showMolPath + show3Dname + ".mol2";
                File mol2file = new File(path1);
                if (!mol2file.exists()) {
                    show3Dname = stringName[0];
                }
                boolean bl = (boolean) jTable.getModel().getValueAt(jTable.getSelectedRow(), 5);
                String path2 = showMolPath + show3Dname + ".mol2";
                if (jTable.getSelectedColumn() == 5) {
                    if (bl) {
                        //jTable.getModel().setValueAt(true, jTable.getSelectedRow(), 5);
                        if (jTable.getSelectedRow() == 0) {
                            queryfilePath = showMolPath + "Input.mol2";
                            String controller1 = "load APPEND " + "\"" + queryfilePath //first row is the query mol
                                    + "\"" + " ;frame*" + " ;dots on";
                            jmolPanel.viewer.evalString(controller1);
                            ifshow.entrySet().stream().map((entry) -> (int) entry.getValue()).filter((value) -> (flag < value)).forEach((value) -> {
                                flag = value;
                            });
                            flag++;
                            ifshow.put("Input", flag);
                        } else {
                            ifshow.entrySet().stream().map((entry) -> (int) entry.getValue()).filter((value) -> (flag < value)).forEach((value) -> {
                                flag = value;
                            });
                            flag++;
                            ifshow.put(show3Dname, flag);
                            String controller1 = "load APPEND " + "\"" + path2 + "\"" + " ;frame*" + " ;dots on";
                            //String controller2 = ""; 
                            jmolPanel.viewer.evalString(controller1);
                            //jmolPanel2.viewer.evalString(controller2); 
                        }
                    } else {
                        if (jTable.getSelectedRow() == 0) {
                            int a = (int) ifshow.get("Input");
                            String b = a + ".1";
                            String controller = "zap " + b;
                            jmolPanel.viewer.evalString(controller);
                            ifshow.remove("Input");
                            if (ifshow.isEmpty()) {
                                flag = 0;
                            }
                        } //jTable.getModel().setValueAt(false, jTable.getSelectedRow(), 5);
                        else {
                            int a = (int) ifshow.get(show3Dname);
                            String b = a + ".1";
                            String controller = "zap " + b;
                            jmolPanel.viewer.evalString(controller);
                            ifshow.remove(show3Dname);
                            if (ifshow.isEmpty()) {
                                flag = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * job tree mouse event
     *
     * @author baoabo
     *
     */
    class TreeMouseHandle extends MouseAdapter {

        boolean F = false;

        @Override
        public void mousePressed(final MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                F = true;
                JTree jTree = (JTree) e.getSource();
                int rowLocation = jTree.getRowForLocation(e.getX(),
                        e.getY());
                TreePath treepath = jTree.getPathForRow(rowLocation);
                if (treepath == null) {
                    if (IsRename == 1) {
                        String newName = nowNode.toString();
                        boolean renameflag = jobInfor.updateXML(oldName, newName, nowNode.getParent().toString(), tmpPath);
                        if (renameflag) {
                            jTree.stopEditing();
                            File file = new File(localworkPath + newName);
                            renameFile(oldName, file, ResultPath); //rename directory
                            IsRename = 0;
                        } else {
                            IsRename = 0;
                            jTree1.cancelEditing();

                        }
                    } else {
                        jTree1.clearSelection();
                    }
                } else {
                    treeNode = (TreeNode) treepath.getLastPathComponent();
                    if (!treeNode.isLeaf() || (treeNode.getParent()).toString().equals("My Work") || (treeNode.getParent()).toString().equals("Network")) {
                        F = false;
                    }
                    // System.out.println(nodeName);
                }
            }
            if (e.isMetaDown()) {  //right click             
                if (F) {
                    nodeName = treeNode.toString();
                    setmenupop();
                    pm.show(jTree1, e.getX(), e.getY());
                }
            }
            if (e.getClickCount() == 2 && F) {
                southPanel.removeAll();
                jLabel6.setText("Loading ...");
                jPanel1.updateUI();
                southPanel.add(jPanel1);
                southPanel.updateUI();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            nodeName = treeNode.toString();
                            switch ((treeNode.getParent()).toString()) {
                                case "Local":
                                    ResultPath = localworkPath;
                                    break;
                                case "Target Navigator":
                                    ResultPath = targetworkPath;
                                    break;
                                case "Hit Explorer":
                                    ResultPath = hitworkPath;
                                    break;
                            }

                        } catch (NullPointerException ne) {
                        }
                        showMolPath = ResultPath + nodeName + "\\";
                        String name = new ListFinder(showMolPath)
                                .GetList(showMolPath);
                        if (name == null) {
                            resultcomplete = false;
                            SFTPConnection sftp = new SFTPConnection();
                            sftp.connect();
                            resultcomplete = sftp.batchDownLoadFile(nodeName, showMolPath);
                            sftp.disconnect();
                        }
                        if (resultcomplete) {
                            String path = showMolPath + name;
                            IV = new InitVector();
                            data = IV.getdata(path);
                            if (data != null) {
                                Vector columnNames = IV.getcolumn();
                                southPanel.removeAll();
                                jmolPanel.viewer.evalString("zap all");
                                jTable = new JTable();
                                jTable.setBackground(new Color(0, 51, 51));
                                jTable.setForeground(new Color(0, 255, 255));
                                jTable.setDefaultRenderer(String.class,
                                        new MyCellRenderer());
                                CheckTableModle tableModel = new CheckTableModle(
                                        data, columnNames);
                                jTable.setModel(tableModel);
                                jTable.getTableHeader().setDefaultRenderer(
                                        new CheckHeaderCellRenderer(jTable));
                                jTable.addMouseListener(new ShowMol());
                                jScrollPane3 = new JScrollPane(
                                        jTable);
                                southPanel.add(jScrollPane3);
                                southPanel.updateUI();
                                flag = 0;
                                // ifshow.clear();
                                ifshow = new Hashtable<>();
                                flag++;
                                ifshow.put("Input", flag);
                                String InputFilepath = showMolPath + "Input.mol2";
                                String controller1 = "load APPEND " + "\"" + InputFilepath
                                        + "\"" + " ;frame*" + " ;dots on";
                                jmolPanel.viewer.evalString(controller1);
                            } else {
                                southPanel.removeAll();
                                jLabel6.setText("No Result to Show!");
                                southPanel.add(jPanel1);
                                southPanel.updateUI();
                            }
                        }
                    }
                }.start();
            }
        }
    };

    /**
     * rename the file
     *
     * @param oldname
     * @param newname
     * @param Path file path
     */
    public void renameFile(String oldname, File newname, String Path) {
        File file = new File(Path + oldname);
        file.renameTo(newname);
    }

    /**
     * right mouse click event
     */
    private void setmenupop() {
        pm = new JPopupMenu();
        JMenuItem menuitem1 = new JMenuItem();
        JMenuItem menuitem2 = new JMenuItem();
        JMenuItem menuitem3 = new JMenuItem();
        menuitem1.setText("Rename");
        menuitem1.addActionListener(new ActionListener() { // rename the file
            @Override
            public void actionPerformed(ActionEvent e) {
                jTree1.setEditable(true);
                nowNode = (DefaultMutableTreeNode) jTree1
                        .getLastSelectedPathComponent();
                if (nowNode.isLeaf()) {
                    oldName = nowNode.toString();
                    switch ((nowNode.getParent()).toString()) {
                        case "Local":
                            ResultPath = localworkPath;
                            break;
                        case "Target Navigator":
                            ResultPath = targetworkPath;
                            break;
                        case "Hit Explorer":
                            ResultPath = hitworkPath;
                            break;
                    }
                    jTree1.startEditingAtPath(jTree1.getSelectionPath());
                    IsRename = 1;
                }
            }
        });
        menuitem2.setText("Open In Explorer");
        menuitem2.addActionListener(new ActionListener() { // open the job directory
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((treeNode.getParent()).toString()) {
                    case "Local":
                        ResultPath = localworkPath;
                        break;
                    case "Target Navigator":
                        ResultPath = targetworkPath;
                        break;
                    case "Hit Explorer":
                        ResultPath = hitworkPath;
                        break;
                }
                try {
                    String filepath = ResultPath + nodeName;
                    // System.out.println(filePath);
                    Runtime.getRuntime().exec(
                            "explorer.exe /select," + filepath);
                } catch (IOException e1) {
                }
            }
        });
        menuitem3.setText("Delete");
        menuitem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((treeNode.getParent()).toString().equals("Local")) {
                    ResultPath = localworkPath;
                } else if ((treeNode.getParent()).toString().equals("Target Navigator")) {
                    ResultPath = targetworkPath;
                } else if ((treeNode.getParent()).toString().equals("Hit Explorer")) {
                    ResultPath = hitworkPath;
                }
                String filepath = ResultPath + nodeName;
                // System.out.println(filepath);
                int i = JOptionPane
                        .showConfirmDialog(
                                null,
                                "Do you want to delete the source file at the same time?",
                                "Delete", JOptionPane.YES_NO_CANCEL_OPTION);
                if (i == JOptionPane.OK_OPTION) {
                    File file = new File(filepath);
                    try {
                        if (file.isFile()) {
                            file.delete();
                        }
                        if (file.isDirectory()) {
                            File files[] = file.listFiles();
                            for (File file1 : files) {
                                file1.delete();
                            }
                            file.delete();
                        }
                        jobInfor.deleteXML(nodeName, treeNode.getParent().toString(), tmpPath);
                        nodeName = null;
                        dellocalnode((DefaultMutableTreeNode) treeNode);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Failed!");
                    }
                } else if (i == JOptionPane.NO_OPTION) {
                    jobInfor.deleteXML(nodeName, treeNode.getParent().toString(), tmpPath);
                    nodeName = null;
                    dellocalnode((DefaultMutableTreeNode) treeNode);
                }
            }
        });
        pm.add(menuitem1);
        pm.add(menuitem2);
        pm.add(menuitem3);
    }

    /**
     * init the job tree
     */
    private void initwork() {
        localworkNode.removeAllChildren();
        targetNode.removeAllChildren();
        hitNode.removeAllChildren();
        String path = workPath + "\\TMP";
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        tmpPath = path + "\\jobinfor.xml";
        jobInfor = new JobInfor_XML();
        jobInfor.initdataXML(tmpPath);
        Vector local = jobInfor.getLocal();
        Vector target = jobInfor.getTarget();
        Vector hit = jobInfor.getHit();
        if (local != null) {
            local.stream().forEach((local1) -> {
                localworkNode.add(new DefaultMutableTreeNode(local1));
            });
        }
        if (target != null) {
            for (Iterator it = target.iterator(); it.hasNext();) {
                Object target1 = it.next();
                targetNode.add(new DefaultMutableTreeNode(target1));
            }
        }
        if (hit != null) {
            hit.stream().forEach((hit1) -> {
                hitNode.add(new DefaultMutableTreeNode(hit1));
            });
        }
        /* File f1 = new File(localtmpPath);
         File f2 = new File(targettmpPath);
         File f3 = new File(hittmpPath);
         if (!f1.exists()) {
         f1.mkdir();
         }
         else{
         BufferedReader reader = null;   
         try {
         reader = new BufferedReader(new FileReader(f1));
         String line = null;
         while ((line = reader.readLine()) != null) {
         addnode(line,1);
         }
         } catch (FileNotFoundException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
         try {
         reader.close();
         } catch (IOException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }
         if (!f2.exists()) {
         f2.mkdir();
         }
         else{
         BufferedReader reader = null;   
         try {
         reader = new BufferedReader(new FileReader(f2));
         String line = null;
         while ((line = reader.readLine()) != null) {
         addnode(line,2);
         }
         } catch (FileNotFoundException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
         try {
         reader.close();
         } catch (IOException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }
         if (!f3.exists()) {
         f3.mkdir();
         }
         else{
         BufferedReader reader = null;   
         try {
         reader = new BufferedReader(new FileReader(f3));
         String line = null;
         while ((line = reader.readLine()) != null) {
         addnode(line,3);
         }
         } catch (FileNotFoundException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
         try {
         reader.close();
         } catch (IOException ex) {
         Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }*/
        jTree1.setEditable(false);
        jTree1.updateUI();
    }

    /**
     * add tree node
     *
     * @param node
     * @param model add where
     */
    public void addnode(String node, int model) {
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(node);
        switch (model) {
            case 1:
                localworkNode.add(node1);
                jTree1.updateUI();
                break;
            case 2:
                targetNode.add(node1);
                break;
            case 3:
                hitNode.add(node1);
                break;
        }
        jTree1.updateUI();

    }

    /**
     * delete local work tree node
     *
     * @param node
     */
    public void dellocalnode(DefaultMutableTreeNode node) {
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.removeNodeFromParent(node);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane3 = new javax.swing.JScrollPane();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        westPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeRoot = new DefaultMutableTreeNode("My Work");
        jTree1 = new javax.swing.JTree(treeRoot);
        jmolPanel = new JmolPanel1();
        centerPanel = new javax.swing.JPanel();
        eastPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jButton11 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        southPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jCheckBoxMenuItem14 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem15 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem16 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem17 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem18 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem19 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem20 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem21 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem22 = new javax.swing.JCheckBoxMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jCheckBoxMenuItem4 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem5 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem6 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem7 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem8 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem9 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem10 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem11 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem12 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem13 = new javax.swing.JCheckBoxMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();

        jPanel6.setBackground(new java.awt.Color(0, 102, 102));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(290, 131));

        jLabel5.setBackground(new java.awt.Color(0, 51, 51));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Similarity Method:");

        jComboBox2.setBackground(new java.awt.Color(0, 102, 102));
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        programModel = "FeatureAlign";
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SHAFTS...", "USR..." }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jRadioButton3.setBackground(new java.awt.Color(0, 102, 102));
        jRadioButton3.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton3.setText("Bioactivity Database:");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jRadioButton4.setBackground(new java.awt.Color(0, 102, 102));
        jRadioButton4.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton4.setText("Compound Database:");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jComboBox3.setBackground(new java.awt.Color(0, 102, 102));
        jComboBox3.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose...", "DrugBank", "ChEMBL", "BindingDB", "KEGG", "PDB" }));
        jComboBox3.setEnabled(false);
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jComboBox4.setBackground(new java.awt.Color(0, 102, 102));
        jComboBox4.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose...", "MayBridge", "Specs", "ZINC", "NCI" }));
        jComboBox4.setEnabled(false);
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jRadioButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);

        buttonGroup2.add(jRadioButton3);
        buttonGroup2.add(jRadioButton4);

        jScrollPane3.getViewport().setBackground(new java.awt.Color(0,51,51));
        jScrollPane3.getViewport().setForeground(new java.awt.Color(0,51,51));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(0, 51, 51));

        jToolBar1.setBackground(new java.awt.Color(0, 51, 51));
        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton2.setBackground(new java.awt.Color(0, 51, 51));
        jButton2.setForeground(new java.awt.Color(0, 153, 153));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/openf.png"))); // NOI18N
        jButton2.setToolTipText("Open File");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2MouseExited(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setBackground(new java.awt.Color(0, 51, 51));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/format.png"))); // NOI18N
        jButton3.setToolTipText("Format Conversion");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton3.setContentAreaFilled(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3MouseExited(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setBackground(new java.awt.Color(0, 51, 51));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/showH.png"))); // NOI18N
        jButton4.setToolTipText("Show H");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(37, 33));
        jButton4.setMinimumSize(new java.awt.Dimension(37, 33));
        jButton4.setPreferredSize(new java.awt.Dimension(37, 33));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setBackground(new java.awt.Color(0, 51, 51));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/doSpin.png"))); // NOI18N
        jButton5.setToolTipText("Rotation");
        jButton5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton5.setContentAreaFilled(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton5MouseExited(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setBackground(new java.awt.Color(0, 51, 51));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/Reset.png"))); // NOI18N
        jButton6.setToolTipText("Reset");
        jButton6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton6.setContentAreaFilled(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton6MouseExited(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton7.setBackground(new java.awt.Color(0, 51, 51));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/Bar.png"))); // NOI18N
        jButton7.setToolTipText("Bar");
        jButton7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton7.setContentAreaFilled(false);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton7MouseExited(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton8.setBackground(new java.awt.Color(0, 51, 51));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/Optimize.png"))); // NOI18N
        jButton8.setToolTipText("Optimization");
        jButton8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton8.setContentAreaFilled(false);
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton8MouseExited(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton9.setBackground(new java.awt.Color(0, 51, 51));
        jButton9.setForeground(new java.awt.Color(0, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/modelKit.png"))); // NOI18N
        jButton9.setToolTipText("Model Kit");
        jButton9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jButton9.setContentAreaFilled(false);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setMaximumSize(new java.awt.Dimension(37, 33));
        jButton9.setMinimumSize(new java.awt.Dimension(37, 33));
        jButton9.setPreferredSize(new java.awt.Dimension(37, 33));
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton9MouseExited(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jToolBar2.setBackground(new java.awt.Color(0, 51, 51));
        jToolBar2.setBorder(null);
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setAutoscrolls(true);
        jToolBar2.setPreferredSize(new java.awt.Dimension(130, 37));

        jButton1.setBackground(new java.awt.Color(0, 51, 51));
        jButton1.setFont(new java.awt.Font("微软雅黑", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 255, 204));
        jButton1.setText("   ");
        jButton1.setAlignmentX(1.0F);
        jButton1.setAutoscrolls(true);
        jButton1.setBorder(null);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusable(false);
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jButton1.setMaximumSize(new java.awt.Dimension(200, 35));
        jButton1.setMinimumSize(new java.awt.Dimension(63, 35));
        jButton1.setOpaque(true);
        jButton1.setPreferredSize(new java.awt.Dimension(130, 35));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton1);
        if(!iscan)
        jButton1.setText("Not Actived");
        else{
            try{
                CheckUserStatus cus = new CheckUserStatus();
                status = cus.checkauthorization();
                switch(status){
                    case 0: jButton1.setText("Arrearage");
                    break;
                    case 1: jButton1.setText("Welcome");
                    break;
                    case 2: jButton1.setText("Server Problem");
                    break;
                    case 3: jButton1.setText("Off Line");
                    break;
                }
            }catch (Exception ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Unknown Error!", "Tips", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        westPanel.setBackground(new java.awt.Color(0, 51, 51));

        jScrollPane1.setBackground(new java.awt.Color(0, 51, 51));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jTree1.setBackground(new java.awt.Color(0, 102, 102));
        jTree1.setForeground(new java.awt.Color(255, 255, 255));
        localworkNode = new DefaultMutableTreeNode("Local");
        networkNode = new DefaultMutableTreeNode("Network");
        targetNode = new DefaultMutableTreeNode("Target Navigator");
        hitNode = new DefaultMutableTreeNode("Hit Explorer");
        treeRoot.add(localworkNode);
        treeRoot.add(networkNode);
        networkNode.add(targetNode);
        networkNode.add(hitNode);
        jTree1.addMouseListener(new TreeMouseHandle());
        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout westPanelLayout = new javax.swing.GroupLayout(westPanel);
        westPanel.setLayout(westPanelLayout);
        westPanelLayout.setHorizontalGroup(
            westPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        );
        westPanelLayout.setVerticalGroup(
            westPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        centerPanel.setBackground(new java.awt.Color(0, 51, 51));
        centerPanel.setFocusable(false);
        centerPanel.setLayout(new java.awt.GridLayout(1, 0));

        jmolPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(jmolPanel);
        centerPanel.updateUI();

        eastPanel.setBackground(new java.awt.Color(0, 102, 102));
        eastPanel.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Input Molecule:");

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 102, 102));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTextField1.setCaretColor(new java.awt.Color(255, 255, 255));
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Similarity Threshold:");

        jComboBox1.setBackground(new java.awt.Color(0, 51, 51));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.8", "1.0", "1.2", "1.5" , "1.8"}));
        jComboBox1.setSelectedItem("1.2");
        Threshold = "1.2";
        jComboBox1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Max Hits:");

        jTextField2.setBackground(new java.awt.Color(0, 102, 102));
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setText("1000");
        jTextField2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTextField2.setCaretColor(new java.awt.Color(255, 255, 255));

        jRadioButton1.setBackground(new java.awt.Color(0, 102, 102));
        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Upload Database");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setBackground(new java.awt.Color(0, 102, 102));
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setVisible(iscan);
        jRadioButton2.setText("Chemmapper Database");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(0, 51, 51));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jScrollPane2.getViewport().setBackground(new java.awt.Color(0,51,51));
        jScrollPane2.setBorder(null);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton11.setBackground(new java.awt.Color(0, 102, 102));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Start");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(0, 102, 102));
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Export");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));

        jCheckBox1.setBackground(new java.awt.Color(0, 102, 102));
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Generated Conformers");

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(0, 102, 102));
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTextField3.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Note: File size must less than 10 MB!");

        jButton12.setBackground(new java.awt.Color(0, 102, 102));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Upload");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(0, 53, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12))
                .addGap(18, 18, 18)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 131, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout eastPanelLayout = new javax.swing.GroupLayout(eastPanel);
        eastPanel.setLayout(eastPanelLayout);
        eastPanelLayout.setHorizontalGroup(
            eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eastPanelLayout.createSequentialGroup()
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(eastPanelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton1)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jRadioButton2)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(eastPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addGap(1, 1, 1))
            .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        eastPanelLayout.setVerticalGroup(
            eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eastPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
            .addGroup(eastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(eastPanelLayout.createSequentialGroup()
                    .addGap(146, 146, 146)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(173, Short.MAX_VALUE)))
        );

        southPanel.setBackground(new java.awt.Color(0, 51, 51));
        southPanel.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel6.setFont(new java.awt.Font("微软雅黑", 2, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 255, 204));
        jLabel6.setText("No Result to Show!");
        jLabel6.setAutoscrolls(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(451, 451, 451)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(519, 519, 519))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        southPanel.add(jPanel1);

        jMenuBar1.setBackground(new java.awt.Color(0, 102, 102));
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));

        jMenu1.setText("  File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Open");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Create New");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("  Tools ");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Format Conversion");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Save As Picture...");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("  Molecule ");

        jMenu7.setText("Surface");

        jCheckBoxMenuItem14.setSelected(true);
        jCheckBoxMenuItem14.setText("Dot Surface");
        jCheckBoxMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem14ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem14);

        jCheckBoxMenuItem15.setText("van der Waals Surface");
        jCheckBoxMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem15ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem15);

        jCheckBoxMenuItem16.setText("Molecular Surface");
        jCheckBoxMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem16ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem16);

        jCheckBoxMenuItem17.setText("Solvent Surface(1.4-Angstrom probe)");
        jCheckBoxMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem17ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem17);

        jCheckBoxMenuItem18.setText("Soklvent-Accessible Surface(VDW+1.4 Angstrom)");
        jCheckBoxMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem18ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem18);

        jCheckBoxMenuItem19.setText("Molecular Electrostatic Potential");
        jCheckBoxMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem19ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem19);

        jCheckBoxMenuItem20.setText("Make Opaque");
        jCheckBoxMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem20ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem20);

        jCheckBoxMenuItem21.setText("MakeTranslucent");
        jCheckBoxMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem21ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem21);

        jCheckBoxMenuItem22.setText("Off");
        jCheckBoxMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem22ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem22);

        jMenu3.add(jMenu7);

        jMenu8.setText("Background");

        jCheckBoxMenuItem4.setText("White");
        jCheckBoxMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem4ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem4);

        jCheckBoxMenuItem5.setText("Black");
        jCheckBoxMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem5ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem5);

        jCheckBoxMenuItem6.setText("Red");
        jCheckBoxMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem6ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem6);

        jCheckBoxMenuItem7.setText("Orange");
        jCheckBoxMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem7ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem7);

        jCheckBoxMenuItem8.setText("Yellow");
        jCheckBoxMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem8ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem8);

        jCheckBoxMenuItem9.setText("Green");
        jCheckBoxMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem9ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem9);

        jCheckBoxMenuItem10.setText("Cyan");
        jCheckBoxMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem10ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem10);

        jCheckBoxMenuItem11.setText("Blue");
        jCheckBoxMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem11ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem11);

        jCheckBoxMenuItem12.setSelected(true);
        jCheckBoxMenuItem12.setText("Indigo");
        jMenu8.add(jCheckBoxMenuItem12);

        jCheckBoxMenuItem13.setText("Violet");
        jCheckBoxMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem13ActionPerformed(evt);
            }
        });
        jMenu8.add(jCheckBoxMenuItem13);

        jMenu3.add(jMenu8);

        jMenu9.setText("Spin");

        jCheckBoxMenuItem1.setText("On");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu9.add(jCheckBoxMenuItem1);

        jMenuItem6.setText("Reset");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem6);

        jMenu3.add(jMenu9);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("  Setting ");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItem28.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem28.setText("Set Your Workpath...");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem28);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("  Help ");

        jMenuItem29.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem29.setText("SHAFTS Help...");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem29);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("  About...");

        jMenuItem30.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem30.setText("About Us...");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem30);

        jMenuItem31.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem31.setText("About SHAFTS...");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem31);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(southPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(westPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(centerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)
                        .addComponent(eastPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eastPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(westPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(centerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(southPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // upload user database
        JFileChooser file = new JFileChooser(
                defaultPath);
        file.setMultiSelectionEnabled(false);
        int result = file.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            DatabasePath = file.getSelectedFile().getAbsolutePath();
            File file1 = new File(DatabasePath);
            jTextField3.setText(file1.getName());
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        if (jRadioButton1.isSelected()) {
            shaftsModel = 1;
            jPanel7.removeAll();
            javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
            jPanel7.setLayout(jPanel7Layout);
            jPanel7Layout.setHorizontalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel7Layout.setVerticalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 115, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))
            );
            jPanel7.updateUI();
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        if (jRadioButton2.isSelected()) {
            shaftsModel = 2;
            jPanel7.removeAll();
            javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
            jPanel7.setLayout(jPanel7Layout);
            jPanel7Layout.setHorizontalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel7Layout.setVerticalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 115, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))
            );
            jPanel7.updateUI();
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jCheckBoxMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem18ActionPerformed
        //Solvent-Accessible Surface
        if (jCheckBoxMenuItem18.isSelected()) {
            String controller = "isosurface delete resolution 0 sasurface 1.4 translucent";
            jmolPanel.viewer.evalStringQuiet(controller);
        } else {
            jCheckBoxMenuItem19.setSelected(false);
            // jCheckBoxMenuItem18.setSelected(false);
            jCheckBoxMenuItem17.setSelected(false);
            jCheckBoxMenuItem16.setSelected(false);
            jCheckBoxMenuItem15.setSelected(false);
            String controller = "isosurface off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem18ActionPerformed

    private void jCheckBoxMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem21ActionPerformed
        // make translucent
        if (jCheckBoxMenuItem21.isSelected()) {
            String controller = "mo translucent;isosurface translucent";
            jmolPanel.viewer.evalStringQuiet(controller);
        } else {
            String controller = "isosurface off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem21ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Open new molecule file
        JFileChooser file = new JFileChooser(
                defaultPath);
        int result = file.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = file.getSelectedFile().getAbsolutePath();
            jmolPanel.viewer.openFile(filePath);
            String bandian = "dots on";
            jmolPanel.viewer.evalString(bandian);
            File file11 = new File(filePath);
            jTextField1.setText(file11.getName());
            fileName = file11.getName();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // create new molecule file
        jcpui = new JcpUI();
        jcpui.setVisible(true);
        TwoDThread TThread = new TwoDThread();
        TThread.start();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // exit the system
        int result = JOptionPane.showConfirmDialog(null, "Are you sure to close the application?", "Tips", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            CheckUserStatus CUS = new CheckUserStatus();
            try {
                CUS.offline();
            } catch (IOException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // export molecule picture
        if (jTextField1.getText() == null) {
            JOptionPane.showMessageDialog(null, "No file to export!!");
        } else {
            String gif = "write image '?FILEROOT?.gif'";
            jmolPanel.viewer.evalString(gif);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // format conversion
        new FormatConvUI().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jCheckBoxMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem14ActionPerformed
        // dot surface
        if (jCheckBoxMenuItem14.isSelected()) {
            jCheckBoxMenuItem22.setSelected(false);
            String controller = "dots on";
            jmolPanel.viewer.evalString(controller);
        } else {
            String controller = "dots off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem14ActionPerformed

    private void jCheckBoxMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem15ActionPerformed
        // van der waals surface
        if (jCheckBoxMenuItem15.isSelected()) {
            jCheckBoxMenuItem22.setSelected(false);
            String controller = "isosurface delete resolution 0 solvent 0 translucent";
            jmolPanel.viewer.evalString(controller);
        } else {
            jCheckBoxMenuItem19.setSelected(false);
            jCheckBoxMenuItem18.setSelected(false);
            jCheckBoxMenuItem17.setSelected(false);
            jCheckBoxMenuItem16.setSelected(false);
            // jCheckBoxMenuItem15.setSelected(false);
            String controller = "isosurface off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem15ActionPerformed

    private void jCheckBoxMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem16ActionPerformed
        // Molecular surface
        if (jCheckBoxMenuItem16.isSelected()) {
            jCheckBoxMenuItem22.setSelected(false);
            String controller = "isosurface delete resolution 0 molecular translucent";
            jmolPanel.viewer.evalStringQuiet(controller);
        } else {
            jCheckBoxMenuItem19.setSelected(false);
            jCheckBoxMenuItem18.setSelected(false);
            jCheckBoxMenuItem17.setSelected(false);
            // jCheckBoxMenuItem16.setSelected(false);
            jCheckBoxMenuItem15.setSelected(false);
            String controller = "isosurface off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem16ActionPerformed

    private void jCheckBoxMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem17ActionPerformed
        // Solvent Surface:
        if (jCheckBoxMenuItem17.isSelected()) {
            jCheckBoxMenuItem22.setSelected(false);
            String controller = "isosurface delete resolution 0 solvent 1.4 translucent";
            jmolPanel.viewer.evalStringQuiet(controller);
        } else {
            jCheckBoxMenuItem19.setSelected(false);
            jCheckBoxMenuItem18.setSelected(false);
            //jCheckBoxMenuItem17.setSelected(false);
            jCheckBoxMenuItem16.setSelected(false);
            jCheckBoxMenuItem15.setSelected(false);
            String controller = "isosurface off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem17ActionPerformed

    private void jCheckBoxMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem19ActionPerformed
        // electrostatic surface
        if (jCheckBoxMenuItem19.isSelected()) {
            jCheckBoxMenuItem22.setSelected(false);
            String controller = "isosurface delete resolution 0 vdw color range all map MEP translucent";
            jmolPanel.viewer.evalStringQuiet(controller);
        } else {
            //jCheckBoxMenuItem19.setSelected(false);
            jCheckBoxMenuItem18.setSelected(false);
            jCheckBoxMenuItem17.setSelected(false);
            jCheckBoxMenuItem16.setSelected(false);
            jCheckBoxMenuItem15.setSelected(false);
            String controller = "isosurface off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem19ActionPerformed

    private void jCheckBoxMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem20ActionPerformed
        // opaque
        if (jCheckBoxMenuItem20.isSelected()) {
            jCheckBoxMenuItem22.setSelected(false);
            String controller = "mo opaque;isosurface opaque";
            jmolPanel.viewer.evalStringQuiet(controller);
        } else {
            String controller = "isosurface off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem20ActionPerformed

    private void jCheckBoxMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem22ActionPerformed
        // off
        if (jCheckBoxMenuItem22.isSelected()) {
            jCheckBoxMenuItem21.setSelected(false);
            jCheckBoxMenuItem20.setSelected(false);
            jCheckBoxMenuItem19.setSelected(false);
            jCheckBoxMenuItem18.setSelected(false);
            jCheckBoxMenuItem17.setSelected(false);
            jCheckBoxMenuItem16.setSelected(false);
            jCheckBoxMenuItem15.setSelected(false);
            jCheckBoxMenuItem14.setSelected(false);
            String controller = "mo delete;isosurface delete;select *;dots off";
            jmolPanel.viewer.evalStringQuiet(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem22ActionPerformed

    private void jCheckBoxMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem4ActionPerformed
        // white
        if (jCheckBoxMenuItem4.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem12.setSelected(false);
            //jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("white");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem4ActionPerformed

    private void jCheckBoxMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem5ActionPerformed
        // black
        if (jCheckBoxMenuItem5.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            // jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("black");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem5ActionPerformed

    private void jCheckBoxMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem6ActionPerformed
        // red
        if (jCheckBoxMenuItem6.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            //jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("red");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem6ActionPerformed

    private void jCheckBoxMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem7ActionPerformed
        // orange
        if (jCheckBoxMenuItem7.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            // jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("orange");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem7ActionPerformed

    private void jCheckBoxMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem8ActionPerformed
        // yellow
        if (jCheckBoxMenuItem8.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            // jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("yellow");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem8ActionPerformed

    private void jCheckBoxMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem9ActionPerformed
        // green
        if (jCheckBoxMenuItem9.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            // jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("green");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem9ActionPerformed

    private void jCheckBoxMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem10ActionPerformed
        // cyan
        if (jCheckBoxMenuItem10.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            //jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("cyan");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem10ActionPerformed

    private void jCheckBoxMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem11ActionPerformed
        // blue
        if (jCheckBoxMenuItem11.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            // jCheckBoxMenuItem11.setSelected(false);
            jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("blue");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem11ActionPerformed

    private void jCheckBoxMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem13ActionPerformed
        // violet
        if (jCheckBoxMenuItem13.isSelected()) {
            jCheckBoxMenuItem12.setSelected(false);
            jCheckBoxMenuItem4.setSelected(false);
            jCheckBoxMenuItem5.setSelected(false);
            jCheckBoxMenuItem6.setSelected(false);
            jCheckBoxMenuItem7.setSelected(false);
            jCheckBoxMenuItem8.setSelected(false);
            jCheckBoxMenuItem9.setSelected(false);
            jCheckBoxMenuItem10.setSelected(false);
            jCheckBoxMenuItem11.setSelected(false);
            //jCheckBoxMenuItem13.setSelected(false);
            jmolPanel.viewer.setColorBackground("violet");
            centerPanel.updateUI();
        } else {
            jCheckBoxMenuItem12.setSelected(true);
            jmolPanel.viewer.setColorBackground("indigo");
            centerPanel.updateUI();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem13ActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        // spin
        if (jCheckBoxMenuItem1.isSelected()) {
            String controller = "spin on";
            jmolPanel.viewer.evalString(controller);
        } else {
            String controller = "spin off";
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        //reset
        String controller1 = "spin off; if (showBoundBox or showUnitcell) {moveto 1.0 front;moveto 2.0 back;delay 1} else {boundbox on;moveto 1.0 front;moveto 2.0 back;delay 1;boundbox off}";
        jmolPanel.viewer.evalString(controller1);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        // set work path


    }//GEN-LAST:event_jMenu4ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // set work path
        new SetworkPath().setVisible(true);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File("Readme.txt"));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File("SoftwareInfo.txt"));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed

    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // open file
        JFileChooser file = new JFileChooser(
                defaultPath);
        int result = file.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = file.getSelectedFile().getAbsolutePath();
            jmolPanel.viewer.openFile(filePath);
            String bandian = "dots on";
            jmolPanel.viewer.evalString(bandian);
            File file11 = new File(filePath);
            jTextField1.setText(file11.getName());
            fileName = file11.getName();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Format coversion
        new FormatConvUI().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // show h
        if (showHflag == 0) {
            showHflag = 1;
            String command = "hide Hydrogens";
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/noH.png"))); // NOI18N
            jmolPanel.viewer.evalString(command);
        } else if (showHflag == 1) {
            showHflag = 0;
            String command = "hide none";
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/showH.png"))); // NOI18N
            jmolPanel.viewer.evalString(command);

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // rotation
        if (rotationFlag == 0) {
            rotationFlag = 1;
            String controller = "spin on";
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/noSpin.png"))); // NOI18N
            jmolPanel.viewer.evalString(controller);
        } else if (rotationFlag == 1) {
            rotationFlag = 0;
            String controller = "spin off";
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/doSpin.png"))); // NOI18N
            jmolPanel.viewer.evalString(controller);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Reset
        String controller = "spin off";
        jmolPanel.viewer.evalString(controller);
        String controller1 = "if (showBoundBox or showUnitcell) {moveto 1.0 front;moveto 2.0 back;delay 1} else {boundbox on;moveto 1.0 front;moveto 2.0 back;delay 1;boundbox off}";
        jmolPanel.viewer.evalString(controller1);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // bar molecular model
        if (isbarFlag == 0) {
             jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/Bar.png"))); // NOI18N
            isbarFlag = 1;
            String command = " restrict bonds not selected;select not selected;wireframe 0.3;color cpk";
            jmolPanel.viewer.evalString(command);
        } else if (isbarFlag == 1) {
            jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/Ball.png"))); // NOI18N
            isbarFlag = 0;
            String command = " restrict bonds not selected;select not selected;spacefill 23%AUTO;wireframe 0.15;color cpk";
            jmolPanel.viewer.evalString(command);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // optimize molecule structure
        String command = "minimize";
        jmolPanel.viewer.evalString(command);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // model kit
        if (modelkitFlag == 0) {
            modelkitFlag = 1;
            String command = " modelkitmode = false";
            jButton9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            jmolPanel.viewer.evalString(command);
        } else if (modelkitFlag == 1) {
            modelkitFlag = 0;
            String command = " modelkitmode = true";
            jButton9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jmolPanel.viewer.evalString(command);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        //

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // select molecule threshold
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Threshold = (String) jComboBox1.getSelectedItem();
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // start the shafts work        
        String input = jTextField1.getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Please select the input molecule!");
        } else {
            switch (shaftsModel) {
                case 1:
                    if (DatabasePath == null) {
                        JOptionPane.showMessageDialog(null,
                                "Please select the upload database!");
                    } else {
                        String NewPath = localworkPath + "Job";
                        Smilarity sf = new Smilarity();
                        sf.shaftinit(NewPath, filePath, DatabasePath,
                                ResultNum, Threshold);
                        String workid = sf.getworkid();
                        addnode(workid, shaftsModel);
                        File f1 = new File(filePath);
                        String newpath = localworkPath + workid + "\\Input.mol2";
                        File f2 = new File(newpath);
                        f1.renameTo(f2);
                        jobInfor.addXML(workid, "Local", tmpPath);
                        data = sf.getdata();
                        Vector columnNames = IV.getcolumn();
                        southPanel.removeAll();
                        southPanel.updateUI();
                        jTable = new JTable();
                        jTable.setBackground(new Color(0, 51, 51));
                        jTable.setForeground(new Color(0, 255, 255));
                        jTable.setDefaultRenderer(String.class,
                                new MyCellRenderer());
                        CheckTableModle tableModel = new CheckTableModle(
                                data, columnNames);
                        jTable.setModel(tableModel);
                        jTable.getTableHeader().setDefaultRenderer(
                                new CheckHeaderCellRenderer(jTable));
                        jTable.addMouseListener(new ShowMol());
                        jScrollPane3 = new JScrollPane(jTable);
                        southPanel.add(jScrollPane3);
                        southPanel.updateUI();
                        flag = 0;
                        ifshow = new Hashtable<>();
                    }
                    break;
                case 2:
                    CheckUserStatus cus = new CheckUserStatus();
                    try {
                        int t = cus.checkauthorization();
                        switch (t) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "Authorization has expired! Please renew it. ", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 1:
                                HttpInvokerClient HIC = new HttpInvokerClient();
                                String ID = HIC.getid(filePath, screenModel, ResultNum,
                                        programModel, screenDB, Threshold);
                                addnode(ID, nodePlace);
                                if (nodePlace == 2) {
                                    jobInfor.addXML(ID, "Target Navigator", workPath);
                                } else if (nodePlace == 3) {
                                    jobInfor.addXML(ID, "Hit Explorer", tmpPath);
                                }
                                break;
                            case 2:
                                JOptionPane.showMessageDialog(null, "Server problem! ", "Tips", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            default:
                                break;
                        }
                    } catch (IOException | HeadlessException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, "Internal Error!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                default:
                    //JOptionPane.showMessageDialog(null, "Unknown Error!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // when Bioactivity Database selected 
        jComboBox3.setEnabled(true);
        jComboBox4.setEnabled(false);
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // choose screen program:FeatureAlign or ShapeFilter
        switch ((String) jComboBox2.getSelectedItem()) {
            case "SHAFTS...":
                programModel = "FeatureAlign";
                break;
            case "USR...":
                programModel = "ShapeFilter";
                break;
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // choose target database
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            screenDB = (String) jComboBox3.getSelectedItem();
            screenModel = 0;
            nodePlace = 2; //add to the target navigator
        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // choose screen database
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            screenDB = (String) jComboBox4.getSelectedItem();
            screenModel = 1;
            nodePlace = 3;// add to the hit explorer
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // when Compound Database selected
        jComboBox3.setEnabled(false);
        jComboBox4.setEnabled(true);
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new ExportXlsUI(jTable).setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String name = jButton1.getText();
        switch (name) {
            case "Not Actived":
            case "Arrearage":
                new GetenjoyUI().setVisible(true);
                break;
            case "Server Problem":
                CheckUserStatus CUS = new CheckUserStatus();
                 {
                    try {
                        status = CUS.checkauthorization();
                        if (status == 0) {
                            jButton1.setText("Arrearage");
                        } else if (status == 1) {
                            jButton1.setText("Welcome");
                        } else if (status == 3) {
                            jButton1.setText("Off Line");
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Sorry! The server is under maintenance!", "Tips", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException | HeadlessException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            case "Off Line":
                CheckUserStatus cus = new CheckUserStatus();
                 {
                    try {
                        status = cus.checkauthorization();
                        if (status == 0) {
                            jButton1.setText("Arrearage");
                        } else if (status == 1) {
                            jButton1.setText("Welcome");
                        } else if (status == 2) {
                            jButton1.setText("Server Problem");
                        } else {
                        }
                    } catch (IOException | HeadlessException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }

                break;
            case "Welcome":
                CheckUserStatus cu = new CheckUserStatus();
                int days;
                try {
                    days = cu.getleftdays();
                    int i = JOptionPane
                            .showConfirmDialog(
                                    null,
                                    "Dear user! You can still enjoy the SHAFTS for "
                                    + days
                                    + " days,\n            Do you want to extend the use date!", "Tips", JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.OK_OPTION) {
                        new GetenjoyUI().setVisible(true);
                    }
                } catch (IOException | HeadlessException ex) {
                    Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Internal Error!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        jButton2.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        jButton2.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton2MouseExited

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        jButton3.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        jButton3.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        jButton4.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        jButton4.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        jButton5.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        jButton5.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton5MouseExited

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        jButton6.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        jButton6.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        jButton7.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        jButton7.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton7MouseExited

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        jButton8.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        jButton8.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton8MouseExited

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        jButton9.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
        jButton9.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton9MouseExited

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        jButton1.setBackground(new java.awt.Color(0, 102, 102));
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        jButton1.setBackground(new java.awt.Color(0, 51, 51));
    }//GEN-LAST:event_jButton1MouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new Thread() {
            public void run() {
                LA = new LaunchAnimation();
                LA.setUndecorated(true);
                LA.setVisible(true);
            }
        }.start();
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
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainUI f = new MainUI();
                f.setVisible(true);
                f.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        int result = JOptionPane.showConfirmDialog(f, "Are you sure to close the application?", "Tips", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                           // new Thread(){
                            //public void run(){
                            CheckUserStatus CUS = new CheckUserStatus();
                            try {
                                CUS.offline();
                            } catch (IOException ex) {
                                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            		//}
                            //}.start();
                            System.exit(0);
                        }
                        
                            
                    }

                });

                LA.close();
            }
        });
    }

    private String filePath; //the input molecule file path
    private String fileName;
    private String localworkPath;
    private String targetworkPath;
    private String hitworkPath;
    private String createfilePath;  // test
    private String defaultPath;
    private String ResultPath;
    private String showMolPath; //the path of show the mol from table
    private String queryfilePath; // query mol
    private String workPath = null;
    private String tmpPath;
    private String show3Dname; //the mol name wthich to show
    private String nodeName;
    private String oldName;
    private String DatabasePath;  //user upload database
    private String programModel;
    private String screenDB;
    private int IsRename = 0;
    private int screenModel;
    private int shaftsModel = 1;   // upload database or network,the first model as default 
    private int showHflag = 0;
    private int rotationFlag = 0;
    private int isbarFlag = 0;
    private int modelkitFlag = 0;
    private int status;
    private int flag;
    private int nodePlace;
    private boolean resultcomplete = true; //judge the result if has to download or download complete
    private boolean iscan;
    private Vector data;
    private JPopupMenu pm;
    private DefaultMutableTreeNode nowNode;
    private TreeNode treeNode;
    private static JcpUI jcpui;
    private JmolPanel1 jmolPanel;
    private JobInfor_XML jobInfor;
    private InitVector IV;
    private JTable jTable;
    private Hashtable<String, Integer> ifshow;
    private static LaunchAnimation LA;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel eastPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem10;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem11;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem12;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem13;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem14;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem15;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem16;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem17;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem18;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem19;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem20;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem21;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem22;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem4;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem6;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem7;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem8;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem9;
    private javax.swing.JComboBox jComboBox1;
    private String Threshold;
    private String ResultNum;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTree jTree1;
    private DefaultMutableTreeNode treeRoot;
    private DefaultMutableTreeNode localworkNode;
    private DefaultMutableTreeNode networkNode;
    private DefaultMutableTreeNode targetNode;
    private DefaultMutableTreeNode hitNode;
    private javax.swing.JPanel southPanel;
    private javax.swing.JPanel westPanel;
    // End of variables declaration//GEN-END:variables
}