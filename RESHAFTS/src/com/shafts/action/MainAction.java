/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.action;

import com.shafts.bridge.CheckNetWork;
import com.shafts.bridge.CheckUserStatus;
import com.shafts.bridge.HttpInvokerClient;
import com.shafts.bridge.LoadPDB;
import com.shafts.bridge.SFTPConnection;
import com.shafts.module.InitTable;
import com.shafts.module.JobTree;
import com.shafts.module.LaunchGif;
import com.shafts.module.RunningGif;
import com.shafts.module.Similarity;
import com.shafts.module.WaitingGif;
import com.shafts.render.CheckHeaderCellRenderer;
import com.shafts.render.CheckTableModle;
import com.shafts.render.MyCellRenderer;
import com.shafts.render.TestTargetTable;
import com.shafts.render.TipsRender;
import com.shafts.render.TreeRender;
import com.shafts.ui.ExportXlsUI;
import com.shafts.ui.JcpUI;
import com.shafts.ui.LaunchpathSetUI;
import com.shafts.ui.MainUI;
import com.shafts.utils.CreateMolecule;
import com.shafts.utils.FormatConv;
import com.shafts.utils.GetButtonName;
import com.shafts.utils.InitTargetJob;
import com.shafts.utils.InitVector;
import com.shafts.utils.JobInfor_XML;
import com.shafts.utils.ListFinder;
import com.shafts.utils.PropertyConfig;
import com.shafts.utils.ThreadCount;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-10 13:02:24
 */
public class MainAction extends MainUI {

    public MainAction() {
        lg = new LaunchGif();
        initSystem();
        setLocation(10, 10);
        initTree();
        initAction();
        setVisible(true);
        lg.close();
    }

    /**
     * init system
     */
    private void initSystem() {
        new Thread() {
            @Override
            public void run() {
                lg.setVisible(true);
            }
        }.start();
        cus = new CheckUserStatus();
        lg.showProgress("Init the progress...");
        Color color = new Color(204, 204, 204);
        getContentPane().setBackground(color);
        lg.showProgress("Check config file...");
        PropertyConfig pc = new PropertyConfig();
        ArrayList<String> a = pc.readProperties();
        if (a == null || (pc.getProperty("showagain")).equals("NO")) {
            new LaunchpathSetUI().setVisible(true);
        }
        ArrayList<String> a1 = pc.readProperties();
        lg.showProgress("Check work space...");
        workPath = a1.get(1);
        File file = new File(workPath);
        if (!file.exists()) {
            lg.showProgress("Create work path...");
            file.mkdir();
        }
        localworkPath = workPath + "\\localwork\\";
        File file4 = new File(localworkPath);
        if (!file4.exists()) {
            lg.showProgress("Create local work path...");
            file4.mkdir();
        }
        String networkPath = workPath + "\\network\\";
        File file1 = new File(networkPath);
        if (!file1.exists()) {
            lg.showProgress("Create net work path...");
            file1.mkdir();
        }
        targetworkPath = networkPath + "TargetNavigator\\";
        File file2 = new File(targetworkPath);
        if (!file2.exists()) {
            lg.showProgress("Create target navigator work path...");
            file2.mkdir();
        }
        hitworkPath = networkPath + "HitExplorer\\";
        File file3 = new File(hitworkPath);
        if (!file3.exists()) {
            lg.showProgress("Create hit explorer work path...");
            file3.mkdir();
        }
        defaultPath = System.getProperty("user.dir");

        String tempPath = defaultPath + "\\files\\";
        File file6 = new File(tempPath);
        if (!file6.exists()) {
            lg.showProgress("Create file path...");
            file6.mkdir();
        }
        createfilePath = tempPath + "MOL\\";
        File file7 = new File(createfilePath);
        if (!file7.exists()) {
            //lg.showProgress("Create pdb path...");
            file7.mkdir();
        }
        pdbfilePath = tempPath + "PDB\\";
        File file8 = new File(pdbfilePath);
        if (!file8.exists()) {
            lg.showProgress("Create pdb path...");
            file8.mkdir();
        }
        mol2filePath = tempPath + "MOL2\\";
        File file9 = new File(mol2filePath);
        if (!file9.exists()) {
            lg.showProgress("Create mol2 file path...");
            file9.mkdir();
        }
        lg.showProgress("Check encrypt file...");
        String keypath = defaultPath + "\\configuration\\shafts.pem";
        File file5 = new File(keypath);
        if (!file5.exists()) {
            iscan = false;
            new Thread() {
                public void run() {
                    new GetpieAction().setVisible(true);
                }
            }.start();
        } else {
            try {
                lg.showProgress("Verify user..");
                iscan = cus.verify();
            } catch (Exception ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                iscan = false;
            }
        }
        lg.showProgress("Enter system...");
    }

    private void initButton() {
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        jButton4.setEnabled(true);
        jButton5.setEnabled(true);
        jButton6.setEnabled(true);
        jButton7.setEnabled(true);
        jButton8.setEnabled(true);
        jMenu3.setEnabled(true);
    }

    /**
     * init the action event
     */
    private void initAction() {
        jTable = new JTable();
        childTable = new JTable();
        IV = new InitVector();
        gbn = new GetButtonName();
        hasShow = new Hashtable<>();
        wg = new WaitingGif();
        rg = new RunningGif();
        inittable = new InitTable();
        loadpdb = new LoadPDB();
        sf = new Similarity();
        tiprender = new TipsRender();
        cnw = new CheckNetWork();
        TTT = new TestTargetTable();
        inittargetjob = new InitTargetJob();
        //jLabel9.setText(" " + ThreadCount + " jobs are running!");
        /**
         * create new molecule
         */
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // create new molecule file
                jcpui = new JcpUI();
                jcpui.setVisible(true);
                String filename = (new File(CreateMolecule.RENDER_FILE_NAME)).getName();
                filePath = createfilePath + filename;
                jmolPanel.setpath(filePath);
                initButton();
                southPanel.removeAll();
                southPanel.add(jPanel8);
                southPanel.updateUI();
                jEditorPane1.setText("<html><body><br><br><center><strong> No Target Infor to Show</strong></center></body></html>");
                jTextField1.setText(filename);
                jTextField1.setToolTipText(filePath);
                jmolPanel.createstart();
            }
        });

        jEditorPane1.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    Desktop desktop = Desktop.getDesktop();

                    try {
                        desktop.browse(new URI(e.getURL().toString()));
                    } catch (IOException | URISyntaxException ex) {
                        Logger.getLogger(MainAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        /**
         * format conversion
         */
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        wg.setvisible(true);
                        //String mol2FilePath = System.getProperty("user.dir") + "\\MOL2";
                        File mol2 = new File(mol2filePath);
                        if (!mol2.exists()) {
                            mol2.mkdir();
                        }
                        String outpath = mol2filePath + "\\" + jTextField1.getText().substring(0, (jTextField1.getText()).lastIndexOf(".")) + ".mol2";
                        boolean convFlag = new FormatConv().formatconv(filePath, outpath, "mol2");
                        if (convFlag) {
                            filePath = outpath;
                            jTextField1.setText(new File(filePath).getName());
                            jTextField1.setToolTipText(filePath);
                            jPanel1.updateUI();
                            jmolPanel.viewer.openFile(filePath);
                        }
                        wg.close();
                    }
                }.start();
            }
        });
        /**
         * optimize
         */
        jButton7.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                new Thread() {
                    public void run() {
                        wg.setvisible(true);
                        if (((filePath.substring(filePath.lastIndexOf(".") + 1)).equals("mol2"))) {
                            filePath = sf.generateConformer(filePath, "File");
                            jTextField1.setText(new File(filePath).getName());
                            jTextField1.setToolTipText(filePath);
                            jPanel1.updateUI();
                            jmolPanel.viewer.openFile(filePath);
                        } else {
                            String str = "Please converse the file format to mol2 first!";
                            tiprender.render(TipLable, str, "Error");
                        }
                        wg.close();
                    }
                }.start();
            }
        });
        String name;
        if (!cnw.netstatus()) {
            name = "Off Line";
        } else {
            name = gbn.getname(iscan);
        }
        jRadioButton2.setVisible(true);            //********************iscan
        jButton9.setText(name);
        /**
         * active component
         */
        jButton9.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = jButton9.getText();
                switch (name) {
                    case "Not Actived":
                        new GetpieAction().setVisible(true);
                        break;
                    case "Arrearage":
                        new GetpieAction().setVisible(true);
                        break;
                    case "Server Problem":
                        String bname = gbn.getname(true);
                        if (bname.equals("Server Problem")) {
                            tiprender.render(TipLable, "Sorry! The server is under maintenance!", "Error");
                        } else {
                            jButton9.setText(bname);
                        }
                        break;
                    case "Off Line":
                        if (!cnw.netstatus()) {
                            tiprender.render(TipLable, "Connection failed! Please check your net work!", "Error");
                        } else {
                            String bname1 = gbn.getname(true);
                            jButton9.setText(bname1);
                        }
                        break;
                    case "Welcome":
                        int days;
                        try {
                            days = cus.getleftdays();
                            int i = JOptionPane.showConfirmDialog(null, "Dear user! You can still enjoy the SHAFTS for " + days
                                    + " days,\n            Do you want to extend the use date!", "Message", JOptionPane.YES_NO_OPTION);
                            if (i == JOptionPane.OK_OPTION) {
                                new GetpieAction().setVisible(true);
                            }
                        } catch (IOException | HeadlessException ex) {
                            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                }
            }
        });
        /**
         * job tree
         */
        jTree1.addMouseListener(new TreeHandle());
        jTree1.setCellRenderer(new TreeRender());
        /**
         * eastpanel
         */
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            //choose the local database
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser file = new JFileChooser(defaultPath);
                file.setMultiSelectionEnabled(false);
                int result = file.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    DatabasePath = file.getSelectedFile().getAbsolutePath();
                    File file1 = new File(DatabasePath);
                    jTextField3.setText(file1.getName());
                }
            }
        });
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            //export the similarity result
            @Override
            public void actionPerformed(ActionEvent ae) {
                new ExportXlsUI(jTable, showMolPath).setVisible(true);
            }
        });
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            // start the shafts work

            @Override
            public void actionPerformed(ActionEvent ae) {
                String input = jTextField1.getText();
                if (input.isEmpty()) {
                    String str = "Please select the input molecule!";
                    tiprender.render(TipLable, str, "Error");
                } else if ((new File(filePath)).length() == 0) {
                    tiprender.render(TipLable, "Invalid File!", "Error");
                } else {
                    switch (shaftsModel) {
                        case 1://local model
                            if (DatabasePath == null) {
                                String str = "Please select the upload database!";
                                tiprender.render(TipLable, str, "Error");
                            } else if (filePath.contains(" ") || DatabasePath.contains(" ")) {
                                String str = "All input file path cannot contains spaces character!";
                                tiprender.render(TipLable, str, "Error");
                            } else if (!((DatabasePath.substring(DatabasePath.lastIndexOf(".") + 1)).equals("mol2"))) {
                                String str = "Database format must be mol2!";
                                tiprender.render(TipLable, str, "Error");
                            } else {
                                ThreadCount++;
                                startRun run = new startRun();
                                run.start();
                            }
                            break;
                        case 2: //network model
                            if (!(cnw.netstatus())) {
                                tiprender.render(TipLable, "Connection failed! Please check your net work!", "Error");
                            } else {
                                //try {
                                // int t = cus.checkauthorization();
                                /**
                                 * *************************unlocked************************
                                 * for test
                                 */
                                switch (1) {
                                    case 0:
                                        tiprender.render(TipLable, "Authorization has expired! Please renew it. ", "Error");
                                        break;
                                    case 1:
                                        HttpInvokerClient HIC = new HttpInvokerClient();
                                        String ID = HIC.getid(filePath, screenModel, jTextField2.getText(), programModel, screenDB,
                                                Threshold);
                                        addnode(ID, nodePlace);
                                        if (nodePlace == 2) {
                                            jobInfor.addXML(ID, "TargetNavigator", workPath);
                                        } else if (nodePlace == 3) {
                                            jobInfor.addXML(ID, "HitExplorer", tmpPath);
                                        }
                                        break;
                                    case 2:
                                        tiprender.render(TipLable, "Sorry! The server is under maintenance!", "Error");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            // } 
                                /*catch (IOException | HeadlessException ex) {
                             Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                             JOptionPane.showMessageDialog(null, "Internal Error!", "Message", JOptionPane.INFORMATION_MESSAGE);
                             }*/
                            break;
                        default:
                            // JOptionPane.showMessageDialog(null, "Unknown Error!", "Message",
                            // JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }
                }
            }
        });

        /**
         * PDB download
         */
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            boolean downloadsuc = false;
            String url = "http://www.rcsb.org/pdb/files/" + text + ".pdb.gz";

            //String pdbname = jTextField4.getText();
            @Override
            public void actionPerformed(ActionEvent ae) {
                new Thread() {
                    @Override
                    public void run() {
                        wg.setvisible(true);
                        String pdbpath = pdbfilePath + text + ".pdb.gz";
                        String command = "select all;write pdb " + pdbpath + ";load " + pdbpath + ";select not protein and not solvent;spacefill off;select not selected;cpk off";
                        jmolPanel.viewer.evalString(command);

                        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(jmolPanel.getchains()));
                        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select ligand..."}));
                        wg.close();
                        tiprender.render(TipLable, "Success! " + text + "has saved in the: " + pdbpath, localModel);
                        // }
                    }
                }.start();
            }
        });
        
        /**
         * save ligand file to local and open it in jmol
         */
        jButton14.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                filePath = createfilePath + jTextField4.getText() + chainid + ligandid + ".mol";
                String command = "write mol " + filePath;
                jmolPanel.viewer.scriptWait(command);
                jmolPanel.viewer.openFile(filePath);
                jTextField1.setText((new File(filePath)).getName());
                jTextField1.setToolTipText(filePath);
                initButton();
            }
        });
        
        /**
         * collapse or expand the tree
         */
        jButton15.addActionListener(new java.awt.event.ActionListener() {

            int treeFlag = 0; //callopse the tree
            @Override
            public void actionPerformed(ActionEvent e) {
                if(treeFlag == 0){
                     jobtree.expandAll(jTree1, new TreePath(treeRoot), false);
                     treeFlag = 1;
                }
                else{
                     jobtree.expandAll(jTree1, new TreePath(treeRoot), true);
                     treeFlag = 0;
                }
                jTree1.updateUI();
            }
        });
        
        jButton16.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jmolPanel.viewer.evalString("zap all");
                southPanel.removeAll();
                southPanel.add(jPanel8);
                southPanel.updateUI();
                jEditorPane1.setText("<html><body><br><br><center><strong> No Target Infor to Show</strong></center></body></html>");
            }
        });
    }

    class startRun extends Thread {

        @Override
        public void run() {
            //rg.setvisible(true);
            String NewPath = localworkPath + "Job";
            if (jCheckBox1.isSelected()) {
                localModel = "conformer";
            } else {
                localModel = "normal";
            }
            String workid = sf.getworkid(NewPath);
            String str = workid + " has been created! " + ThreadCount + " jobs are running.";
            addnode(workid, shaftsModel);
            tiprender.render(TipLable, str, "Tip");
            showMolPath = localworkPath + workid + "\\";
            jobInfor.addXML(workid, "Local", tmpPath);
            sf.shaftinit(NewPath, filePath, DatabasePath, jTextField2.getText(), Threshold, localModel);
            jobInfor.setJobStatus(workid, "Local", tmpPath);// set complete falg
            ThreadCount--;
            String str1 = workid + " running complete! You can check it now. ";
            tiprender.render(TipLable, str1, "Tip");
            //rg.close();
            /* jmolPanel.viewer.evalString("zap all");
             jTextField1.removeAll();
             String workid = sf.getworkid();
             addnode(workid, shaftsModel);
             showMolPath = localworkPath + workid + "\\";
             jobInfor.addXML(workid, "Local", tmpPath);
             rg.close();
             data = sf.getdata();
             Vector columnNames = IV.getcolumn();
             southPanel.removeAll();
             jTable = inittable.getTable(data, columnNames);
             jTable.addMouseListener(new ShowMol());
             jScrollPane4.setViewportView(jTable);
             southPanel.add(jScrollPane4);
             southPanel.updateUI();
             showNo = 0;
             if (!hasShow.isEmpty()) {
             hasShow.clear();
             }
             showNo++;
             hasShow.put("Input", showNo);
             String InputFilepath = showMolPath + "Input.mol2";
             jTextField1.setText("Input.mol2");
             jTextField1.setToolTipText(InputFilepath);
             jPanel1.updateUI();
             String controller1 = "load APPEND " + "\"" + InputFilepath + "\"" + " ;frame*" + " ;hide Hydrogens" + ";select 1.1; color [0,51,51]";
             jmolPanel.viewer.evalString(controller1);
             */
        }
    }

    /**
     * show the similarity result
     */
    class showTable extends Thread {

        @Override
        public void run() {
            wg.setvisible(true);
            nodeName = treeNode.toString();
            ResultPath = jobtree.getpath((treeNode.getParent()).toString(), localworkPath, targetworkPath, hitworkPath);
            showMolPath = ResultPath + nodeName + "\\";
            //File test = new File(showMolPath);
            //if (!test.isDirectory()) {
            //   tiprender.render(TipLable, "File not exist", "Error");
            //} else {
            String name = new ListFinder().GetList(showMolPath);
            if (name == null && (treeNode.getParent().toString().equals("Target Navigator") || treeNode.getParent().toString().equals("Hit Explorer"))) {
                resultcomplete = false;
                SFTPConnection sftp = new SFTPConnection();
                sftp.connect();
                resultcomplete = sftp.batchDownLoadFile(nodeName, showMolPath);
                sftp.disconnect();
                name = new ListFinder().GetList(showMolPath);
            } else if (name == null && treeNode.getParent().toString().equals("Local")) {
                resultcomplete = false;
                String localcomplete = jobInfor.getJobStatus(nodeName, "Local", tmpPath);
                if (localcomplete.equals("NO")) {
                    String str1 = nodeName + " Still running! Please waiting... ";
                    tiprender.render(TipLable, str1, "Tip");
                } else if (localcomplete.equals("YES")) {
                    String str1 = " Invalid job! ";
                    tiprender.render(TipLable, str1, "Error");
                }
            }
            if (resultcomplete) {

                String path = showMolPath + name;
                data = IV.getdata(path);
                if (data != null) {
                    jTable = new JTable();
                    southPanel.removeAll();
                    jScrollPane4 = new JScrollPane();
                    jScrollPane4.getViewport().setBackground(new java.awt.Color(0,51,51));
                    jScrollPane4.setBorder(null);
                    if (treeNode.getParent().toString().equals("Target Navigator")) {
                        XMLpath = showMolPath + new ListFinder().getXML(showMolPath);
                        TTT.setpara(jmolPanel, showMolPath);
                        jTable = TTT.getTargetTable(XMLpath);
                        jTable.addMouseListener(new showInfor());
                    } else {
                        jTable = inittable.getTable(data, IV.getcolumn());
                        jTable.addMouseListener(new ShowMol());
                    }

                    jEditorPane1.setText("<html><body><br><br><center><strong> No Target Infor to Show</strong></center></body></html>");
                    jmolPanel.viewer.evalString("zap all");
                    jScrollPane4.setViewportView(jTable);
                    southPanel.add(jScrollPane4);
                    southPanel.updateUI();
                    showNo = 0;
                    if (!hasShow.isEmpty()) {
                        hasShow.clear();
                    }
                    showNo++;
                    hasShow.put("Input", showNo);
                    String InputFilepath = showMolPath + "Input.mol2";
                    String controller1 = "load APPEND " + "\"" + InputFilepath + "\"" + " ;frame*" + " ;hide Hydrogens" + ";select 1.1; color [0,51,51]";
                    jmolPanel.viewer.evalString(controller1);
                }

                initButton();
            }
            wg.close();

            // }
        }

    }

    class showInfor extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int row = jTable.getSelectedRow();
                String targetName;
                targetName = (String) jTable.getValueAt(row, 0);
                // System.out.println(XMLpath + "*********" + targetName);
                String target = inittargetjob.getHtml(XMLpath, targetName);
                if (target == null) {
                    target = "<html><body><br><br><center><strong> No Target Infor to Show</strong></center></body></html>";
                }
                jEditorPane1.setText(target);
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
            if (e.getButton() == MouseEvent.BUTTON1) {// left click
                String stringName[] = new String[20];
                int colummCount = jTable.getModel().getColumnCount();// get
                // table
                // column
                // count
                for (int i = 0; i < colummCount - 1; i++) {
                    stringName[i] = jTable.getModel().getValueAt(jTable.getSelectedRow(), i).toString();
                }
                show3Dname = stringName[1];
                String path1 = showMolPath + show3Dname + ".mol2";
                File mol2file = new File(path1);
                if (!mol2file.exists()) {
                    show3Dname = stringName[0];
                }
                boolean bl = !(boolean) jTable.getModel().getValueAt(jTable.getSelectedRow(), 5);
                jTable.getModel().setValueAt(bl, jTable.getSelectedRow(), 5);
                String path2 = showMolPath + show3Dname + ".mol2";
               // if (jTable.getSelectedColumn() == 5) {
                    if (bl) {
                        Iterator iter = hasShow.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next(); //得到这个序列的映射项，就是set中的类型，HashMap都是Map.Entry类型
                            //String key = (String) entry.getKey(); //获得key
                            int value = (int) entry.getValue(); //获得value，都要强制转换一下
                            if (showNo < value) {
                                showNo = value;
                            }
                        }
                        // jTable.getModel().setValueAt(true,
                        // jTable.getSelectedRow(), 5);
                        if (jTable.getSelectedRow() == 0) {
                            showNo++;
                            hasShow.put("Input", showNo);
                            queryfilePath = showMolPath + "Input.mol2";
                            String controller1 = "load APPEND " + "\"" + queryfilePath + "\"" + " ;frame*" + " ;hide Hydrogens" + ";select " + showNo + ".1; color [0,51,51]"; // first row is the query mol                                   
                            jmolPanel.viewer.evalString(controller1);

                        } else {
                            showNo++;
                            hasShow.put(show3Dname, showNo);
                            String controller1 = "load APPEND " + "\"" + path2 + "\"" + " ;frame*" + " ;hide Hydrogens";
                            // String controller2 = "";
                            jmolPanel.viewer.evalString(controller1);
                            // jmolPanel2.viewer.evalString(controller2);
                        }
                    } else {
                        if (jTable.getSelectedRow() == 0) {
                            int a = (int) hasShow.get("Input");
                            String b = a + ".1";
                            String controller = "zap " + b + " ;hide Hydrogens";
                            jmolPanel.viewer.evalString(controller);
                            hasShow.remove("Input");

                        } // jTable.getModel().setValueAt(false,
                        // jTable.getSelectedRow(), 5);
                        else {
                            int a = (int) hasShow.get(show3Dname);
                            String b = a + ".1";
                            String controller = "zap " + b + " ;hide Hydrogens";
                            jmolPanel.viewer.evalString(controller);
                            hasShow.remove(show3Dname);
                        }
                        if (hasShow.isEmpty()) {
                            showNo = 0;
                            jmolPanel.viewer.evalString("zap all");
                        }
                    }
                //}
            }
        }
    }

    /**
     * job tree mouse event
     *
     * @author baoabo
     *
     */
    class TreeHandle extends MouseAdapter {

        boolean F = false;

        @Override
        public void mousePressed(final MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                F = true;
                JTree jTree = (JTree) e.getSource();
                int rowLocation = jTree.getRowForLocation(e.getX(), e.getY());
                TreePath treepath = jTree.getPathForRow(rowLocation);
                if (treepath == null) {
                    F = false;
                    if (IsRename == 1) {//do rename operation
                        jTree.stopEditing();
                        String newName = ((DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent()).toString();
                        boolean renameflag = jobInfor.updateXML(oldName, newName, nowNode.getParent().toString(),
                                tmpPath, ResultPath);
                        if (renameflag) {
                            //jTree.stopEditing();

                            jTree1.clearSelection();
                            IsRename = 0;
                        } else {
                            IsRename = 0;
                            jTree1.startEditingAtPath(jTree1.getSelectionPath());

                        }
                    } else {
                        jTree1.cancelEditing();
                        jTree1.clearSelection();
                    }
                } else {
                    treeNode = (TreeNode) treepath.getLastPathComponent();
                    if (!treeNode.isLeaf() || (treeNode.getParent()).toString().equals("My Work")
                            || (treeNode.getParent()).toString().equals("Network")) {
                        F = false;
                    }
                    // System.out.println(nodeName);
                }
            }
            if (e.isMetaDown()) { // right click
                if (F) {
                    nodeName = treeNode.toString();
                    setmenupop();
                    pm.show(jTree1, e.getX(), e.getY());
                }
            }
            if (e.getClickCount() == 2 && F) {
                new showTable().start();
            }
        }
    };

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
                nowNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
                if (nowNode.isLeaf()) {
                    oldName = nowNode.toString();
                    ResultPath = jobtree.getpath((nowNode.getParent()).toString(), localworkPath, targetworkPath, hitworkPath);
                    jTree1.startEditingAtPath(jTree1.getSelectionPath());
                    IsRename = 1;
                }
            }
        });
        menuitem2.setText("Open In Explorer");
        menuitem2.addActionListener(new ActionListener() { // open the job
            // directory
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultPath = jobtree.getpath((treeNode.getParent()).toString(), localworkPath, targetworkPath, hitworkPath);
                try {
                    String filepath = ResultPath + nodeName;
                    Runtime.getRuntime().exec("explorer.exe /select," + filepath);
                } catch (IOException e1) {
                }
            }
        });
        menuitem3.setText("Delete");
        menuitem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultPath = jobtree.getpath((treeNode.getParent()).toString(), localworkPath, targetworkPath, hitworkPath);
                String filepath = ResultPath + nodeName;
                // System.out.println(filepath);
                int i = JOptionPane.showConfirmDialog(null, "Do you want to delete the source file at the same time?",
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
                        tiprender.render(TipLable, "Delete has been interrupt! ", "Error");
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
     * init the job tree
     */
    private void initTree() {
        jobtree = new JobTree();
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
            for (Iterator it = local.iterator(); it.hasNext();) {
                Object local1 = it.next();
                localworkNode.add(new DefaultMutableTreeNode(local1));
            }
        }
        if (target != null) {
            for (Iterator it = target.iterator(); it.hasNext();) {
                Object target1 = it.next();
                targetNode.add(new DefaultMutableTreeNode(target1));
            }
        }
        if (hit != null) {
            for (Iterator it = hit.iterator(); it.hasNext();) {
                Object hit1 = it.next();
                hitNode.add(new DefaultMutableTreeNode(hit1));
            }
        }
        jTree1.setEditable(false);
        jTree1.scrollPathToVisible(new TreePath(treeRoot.getPath()));
        jobtree.expandAll(jTree1, new TreePath(treeRoot), true);
        jTree1.updateUI();
    }

    public int getThreadCount() {
        return ThreadCount;
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*   try {
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
            public void run() {
                final MainAction f = new MainAction();
                f.setVisible(true);
                f.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        int result = JOptionPane.showConfirmDialog(f, "Are you sure to close the application?", "Message",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
     // new Thread(){
                            // public void run(){

                            // }
                            // }.start();
                            System.exit(0);
                        }

                    }

                });
            }
        });
    }

    private JcpUI jcpui;
    // private int status;
    private int IsRename;//rename flag 
    private int showNo;//record the opened molfile num 
    private int ThreadCount = 0;
    private String tmpPath;
    private String workPath;
    private String DatabasePath;
    private String localworkPath;
    private String targetworkPath;
    private String hitworkPath;
    private String createfilePath;
    private String showMolPath;
    private String queryfilePath; // query mol
    private String pdbfilePath;//pdb file path
    private String mol2filePath; //mol2 file path
    private String ResultPath;
    private String oldName;
    private String nodeName;
    private String show3Dname; // the mol name wthich to show
    private String localModel = "normal"; // whether generate conformer
    private String XMLpath;
    private boolean iscan;
    private boolean resultcomplete = true;
    private Hashtable<String, Integer> hasShow;
    private GetButtonName gbn;
    private TipsRender tiprender;
    private JobInfor_XML jobInfor;
    private CheckNetWork cnw;
    private InitVector IV;
    private TestTargetTable TTT;
    private JobTree jobtree;
    private Similarity sf;
    private WaitingGif wg;
    private RunningGif rg;
    private LaunchGif lg;
    public InitTable inittable;
    private LoadPDB loadpdb;
    private InitTargetJob inittargetjob;
    private CheckUserStatus cus;
    private Vector data;
    private JTable jTable;
    private JTable childTable;
    private DefaultMutableTreeNode nowNode;
    private TreeNode treeNode;
    private JPopupMenu pm;
}
