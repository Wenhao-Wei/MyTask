/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.action;

import com.shafts.bridge.CheckNetWork;
import com.shafts.bridge.DecName;
import com.shafts.bridge.HttpInvokerClient;
import com.shafts.bridge.LoadPDB;
import com.shafts.bridge.SFTPConnection;
import com.shafts.bridge.ServerGate;
import com.shafts.module.InitTable;
import com.shafts.module.JobTree;
import com.shafts.module.LaunchGif;
import com.shafts.module.RunningGif;
import com.shafts.module.Similarity;
import com.shafts.module.WaitingGif;
import com.shafts.render.TestTargetTable;
import com.shafts.render.TipPanel;
import com.shafts.render.TipsRender;
import com.shafts.ui.EnterKeyUI;
import com.shafts.ui.ExportXlsUI;
import com.shafts.ui.JcpUI;
import com.shafts.ui.LaunchpathSetUI;
import com.shafts.ui.LoginUI;
import com.shafts.ui.MainUI;
import com.shafts.ui.StatusUI;
import com.shafts.utils.CreateMolecule;
import com.shafts.utils.FormatConv;
import com.shafts.utils.InitTargetJob;
import com.shafts.utils.InitVector;
import com.shafts.utils.JobInfor_XML;
import com.shafts.utils.ListFinder;
import com.shafts.utils.PropertyConfig;
import com.socket.bean.StatusBean;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
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
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

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
        setLocationRelativeTo(null);
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

        //cus = new CheckUserStatus();
        serGate = new ServerGate();        
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
        String namePath = defaultPath + "\\configuration\\.namespace.5";
        File file10 = new File(namePath);
        String keyPath = defaultPath + "\\configuration\\shafts.pem";
        File file5 = new File(keyPath);
        if (!file10.exists()) {
            iscan = false;
            loginAction();
        } else if (!file5.exists()) {
            iscan = false;
            userName = new DecName().getAccount();
            jButton9.setText(userName);
            /* //license = "unistalled";
             new Thread() {
             @Override
             public void run() {
             //no key file
             new EnterKeyUI(true, jButton9.getText()).setVisible(true);
             }
             }.start();*/
        } else {
            try {
                userName = new DecName().getAccount();
                license = "installed";
                jButton9.setText(userName);
                lg.showProgress("Verify user..");
                iscan = serGate.verify();
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
    
    private void loginAction() {
        new Thread() {
            @Override
            public void run() {
                login = new LoginUI();
                jButton9.setText("Login");
                login.setButton(jButton9);
                login.setVisible(true);
                boolean isLogin = login.getIsLogin();
                if (isLogin) {
                    jButton9.setText(login.getAccountName());
                    iscan = serGate.verify();
                }
                if (iscan) {
                    netJobPanel.removeAll();
                    netJobPanel.add(netJobParaPanel);
                } else {
                    netJobPanel.removeAll();
                    netJobPanel.add(netJobLockPanel);
                }
                
            }
        }.start();
    }

    /**
     * init the action event
     */
    private void initAction() {
        jTable = new JTable();
        childTable = new JTable();
        IV = new InitVector();
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
                southPanel.add(jLabel8);
                southPanel.updateUI();
                jEditorPane1.setText("<html><body><br><br><center><strong> No Target Infor to Show</strong></center></body></html>");
                jTextField1.setText(filename);
                jTextField1.setToolTipText(filePath);
                jmolPanel.createstart();
            }
        });

        //exit system
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // exit the system
                String content = "Are you sure to close the application?";
                int status = 0; // no job running
                if (getThreadCount() > 0) {
                    content = "There are " + getThreadCount() + " jobs are running! Do you want keep it running in the \n background even close the SHAFTS?";
                    status = 1;
                }
                int result = JOptionPane.showConfirmDialog(null, content, "Tips",
                        JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (status == 1) {
                        new Thread() {
                            @Override
                            public void run() {
                                while (true) {
                                    if (getThreadCount() == 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(60000);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(LaunchAction.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                System.exit(0);
                            }
                        }.start();
                        dispose();
                    } else {
                        System.exit(0);
                    }
                } else {
                    if (status == 1) {
                        System.exit(0);
                    }
                }
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
         * no active
         */
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                boolean isLogin = false;
                if (jButton9.getText().equals("Login")) {
                    login = new LoginUI();
                    jButton9.setText("Login");
                    login.setButton(jButton9);
                    login.setVisible(true);
                    isLogin = login.getIsLogin();
                    if (isLogin) {
                        jButton9.setText(login.getAccountName());
                        iscan = serGate.verify();
                    }
                    if (iscan) {
                        netJobPanel.removeAll();
                        netJobPanel.add(netJobParaPanel);
                    } else {
                        netJobPanel.removeAll();
                        netJobPanel.add(netJobLockPanel);
                    }
                }
                if (isLogin) {
                    new EnterKeyUI(iscan, jButton9.getText()).setVisible(true);
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
                            localJobPanel.updateUI();
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
        /*  jButton7.addActionListener(new java.awt.event.ActionListener() {

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
         }*/
        // jRadioButton2.setVisible(iscan);            //********************iscan
        //jButton9.setText(name);
        /**
         * active component
         */
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = jButton9.getText();
                switch (name) {
                    case "Login":
                        loginAction();
                    default:
                        statusBean = serGate.getStatusInfor(name);
                        boolean isOrder;
                        isOrder = serGate.verify();
                        userName = jButton9.getText();
                        new StatusUI(isOrder, license, userName, statusBean).setVisible(true);
                        break;
                    
                }
            }
        });
        /**
         * job tree
         */
        jTree1.addMouseListener(new TreeHandle());
        // jTree1.setCellRenderer(new TreeRender());
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
                ExportXlsUI exportXlsUI = new ExportXlsUI(jTable, showMolPath);
                exportXlsUI.setTipPanel(tipPanel1);
                exportXlsUI.setVisible(true);
            }
        });
        
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            // start the shafts work

            @Override
            public void actionPerformed(ActionEvent ae) {
                String input = jTextField1.getText();
                if (input.isEmpty()) {
                    tipPanel2.removeAll();
                    String str = "Please input molecule!";
                    tipPanel2.add(new TipPanel(str, "Error"));
                } else if ((new File(filePath)).length() == 0) {
                    tipPanel2.removeAll();
                    tipPanel2.add(new TipPanel("Invailid File!", "Error"));
                } else {
                    switch (shaftsModel) {
                        case 1://local model
                            if (DatabasePath == null) {
                                String str = "Database can't be null!";
                                tipPanel2.removeAll();
                                tipPanel2.add(new TipPanel(str, "Error"));
                            } else if (filePath.contains(" ") || DatabasePath.contains(" ")) {
                                String str = "File path format error!";
                                tipPanel2.removeAll();
                                tipPanel2.add(new TipPanel(str, "Error"));
                            } else if (!((DatabasePath.substring(DatabasePath.lastIndexOf(".") + 1)).equals("mol2"))) {
                                String str = "Database format must be mol2!";
                                tipPanel2.removeAll();
                                tipPanel2.add(new TipPanel(str, "Error"));
                            } else {
                                ThreadCount++;
                                startRun run = new startRun();
                                run.start();
                                rg.setvisible(true);
                            }
                            break;
                        case 2: //network model
                            if (!(cnw.netstatus())) {
                                String str = "Connection failed! Please check your net work!";
                                tipPanel1.removeAll();
                                tipPanel1.add(new TipPanel(str, "Error"));
                            } else if (screenDB.equals("Choose...")) {
                                //JOptionPane.showMessageDialog(null, "Login first!", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                                tipPanel2.removeAll();
                                tipPanel2.add(new TipPanel("Choose a database!", "Error"));
                            } else {
                                String isExpired = serGate.isExpired(jButton9.getText());
                                switch (isExpired) {
                                    case "ISEXPIRED":
                                        tipPanel1.removeAll();
                                        String str = "Authorization has expired! Please renew it. ";
                                        tipPanel1.add(new TipPanel(str, "Error"));
                                        break;
                                    case "NOEXPIRED":
                                        HttpInvokerClient HIC = new HttpInvokerClient();
                                        String ID = HIC.getid(filePath, screenModel, jTextField2.getText(), programModel, screenDB,
                                                Threshold);
                                        addnode(ID, nodePlace);
                                        JOptionPane.showMessageDialog(null, "", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                                        if (nodePlace == 2) {
                                            jobInfor = new JobInfor_XML();
                                            jobInfor.addXML(ID, "TargetNavigator", workPath);
                                            JOptionPane.showMessageDialog(null, "Job " + ID + " hsa been created in TargetNavigator, this may spend some time. \n   Check it later", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                                        } else if (nodePlace == 3) {
                                            jobInfor = new JobInfor_XML();
                                            jobInfor.addXML(ID, "HitExplorer", tmpPath);
                                            JOptionPane.showMessageDialog(null, "Job " + ID + " hsa been created in HitExplorer, this may spend some time. \n   Check it later", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                        break;
                                    case "ERROR":
                                        tipPanel1.removeAll();
                                        String str1 = "Sorry! The server is under maintenance!";
                                        tipPanel1.add(new TipPanel(str1, "Error"));
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case 0:
                            tipPanel1.removeAll();
                            tipPanel1.add(new TipPanel("Choose the database!", "Error"));
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
                        if (!((new File(pdbpath)).exists())) {
                            String command = "select all;write pdb " + pdbpath + ";load " + pdbpath + ";select not protein and not solvent;spacefill off;select not selected;cpk off";
                            jmolPanel.viewer.evalString(command);
                        }
                        String loadpdb = "load \"=" + pdbpath + "\";select not protein and not solvent;spacefill off;select not selected;cpk off";
                        jmolPanel.viewer.scriptWait(loadpdb);
                        
                        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(jmolPanel.getchains()));
                        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select ligand..."}));
                        wg.close();
                        tipPanel1.removeAll();
                        tipPanel1.add(new TipPanel("Success! " + text + "saved in: " + pdbpath, "Tip"));
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
                if (treeFlag == 0) {
                    jobtree.expandAll(jTree1, new TreePath(treeRoot), false);
                    treeFlag = 1;
                } else {
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
                southPanel.add(jLabel8);
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
            tipPanel1.removeAll();
            tipPanel1.add(new TipPanel(str, "Error"));
            showMolPath = localworkPath + workid + "\\";
            jobInfor.addXML(workid, "Local", tmpPath);
            sf.shaftinit(NewPath, filePath, DatabasePath, jTextField2.getText(), Threshold, localModel);
            jobInfor.setJobStatus(workid, "Local", tmpPath);// set complete falg
            ThreadCount--;
            tipPanel1.removeAll();
            str = workid + " running complete! You can check it now. ";
            tipPanel1.add(new TipPanel(str, "Error"));
            rg.close();
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
            String str;
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
                    str = nodeName + " Still running! Please waiting... ";
                    tipPanel1.removeAll();
                    tipPanel1.add(new TipPanel(str, "Error"));
                } else if (localcomplete.equals("YES")) {
                    str = " Invalid job! ";
                    tipPanel1.removeAll();
                    tipPanel1.add(new TipPanel(str, "Error"));
                }
            }
            if (resultcomplete) {
                
                String path = showMolPath + name;
                data = IV.getdata(path);
                if (data != null) {
                    jTable = new JTable();
                    southPanel.removeAll();
                    jScrollPane4 = new JScrollPane();
                    //jScrollPane4.getViewport().setBackground(new java.awt.Color(0,51,51));
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
                    String controller1 = "load APPEND " + "\"" + InputFilepath + "\"" + " ;frame*" + " ;hide Hydrogens" + ";select 1.1;";// color [0,51,51]";
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
            String rank;
            if (e.getButton() == MouseEvent.BUTTON1) {// left click
                String stringName[] = new String[20];
                int colummCount = jTable.getModel().getColumnCount();// get table column count
                for (int i = 0; i < colummCount - 1; i++) {
                    stringName[i] = jTable.getModel().getValueAt(jTable.getSelectedRow(), i).toString();
                }
                show3Dname = stringName[1];
                rank = jTable.getModel().getValueAt(jTable.getSelectedRow(), 0).toString();
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
                        Map.Entry entry = (Map.Entry) iter.next();
                        //String key = (String) entry.getKey(); //获得key
                        int value = (int) entry.getValue();
                        if (showNo < value) {
                            showNo = value;
                        }
                    }
                    if (jTable.getSelectedRow() == 0) {
                        showNo++;
                        hasShow.put("Input", showNo);
                        queryfilePath = showMolPath + "Input.mol2";
                        String controller1 = "load APPEND " + "\"" + queryfilePath + "\"" + " ;frame*" + " ;hide Hydrogens" + ";select " + showNo + ".1;";// color [0,51,51]"; // first row is the query mol                                   
                        jmolPanel.viewer.evalString(controller1);
                        
                    } else {
                        showNo++;
                        hasShow.put(rank, showNo);
                        String controller1 = "load APPEND " + "\"" + path2 + "\"" + " ;frame*" + " ;hide Hydrogens";
                        jmolPanel.viewer.evalString(controller1);
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
                        int a = (int) hasShow.get(rank);
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
                        //tiprender.render(TipLable, "Delete has been interrupt! ", "Error");
                        tipPanel1.removeAll();
                        tipPanel1.add(new TipPanel("Delete has been interrupt! ", "Error"));
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

    //设置全局字体
    public static void initGlobalFontSetting(Font fnt) {
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
             //String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            //UIManager.setLookAndFeel(lookAndFeel);

            //BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            //BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            Border bd = new org.jb2011.lnf.beautyeye.ch8_toolbar.BEToolBarUI.ToolBarBorder(UIManager.getColor(Color.GRAY)//Floatable 时触点的颜色 
                    , UIManager.getColor(Color.BLUE)//Floatable 时触点的阴影颜色 
                    , new Insets(2, 2, 2, 2)); //border 的默认insets
            UIManager.put("ToolBar.border", new BorderUIResource(bd));
            
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            //UIManager.put("ToolBar.isPaintPlainBackground",Boolean.TRUE);
            UIManager.put("RootPane.setupButtonVisible", false);
            UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(2, 5, 2, 5));
            //new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName
            //UIManager.setLookAndFeel(new SubstanceRavenGraphiteGlassLookAndFeel());
            //SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
            // SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
        } catch (Exception ex) {
            Logger.getLogger(MainAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*try {
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
                Font font = new Font("微软雅黑", Font.BOLD, 12);
                // initGlobalFontSetting(font);
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
    private String userName;
    private String license = "uninstalled";
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
    private LoginUI login;
    private ServerGate serGate;
    private StatusBean statusBean;
    private Vector data;
    private JTable jTable;
    private JTable childTable;
    private DefaultMutableTreeNode nowNode;
    private TreeNode treeNode;
    private JPopupMenu pm;
}
