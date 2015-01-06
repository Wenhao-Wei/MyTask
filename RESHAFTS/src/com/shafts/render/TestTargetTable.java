package com.shafts.render;

import com.shafts.module.InitTable;
import com.shafts.module.JmolPanel;
import com.shafts.ui.TargetEffectInfor;
import com.shafts.utils.InitTargetJob;
import com.shafts.utils.InitVector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import static java.awt.SystemColor.desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TestTargetTable {

    public void setpath(String path){
        this.path = path;
    }
    public JTable getTargetTable(String path) {
        setpath(path);
        itj = new InitTargetJob();
        target = itj.getTarget(path);
        targetlength = itj.gettargetlength();
        //JFrame frame = new JFrame();
        table = new TargetTable(new TestTargetTableModel());
         table.setBackground(new Color(254, 254, 254));
         table.setForeground(new Color(0, 0, 0));
         table.setDefaultRenderer(String.class, new MyCellRenderer());
         TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
           table.setRowSorter(sorter);
           sorter.setSortable(0, false);
                    sorter.setSortable(1, false);
                    sorter.setSortable(2, false);
                    sorter.setSortable(5, false);
                    //sorter.setSortable(2, false);
                    
                    //table.setDefaultRenderer(, new MyCellRenderer());
        table.getTableHeader().setDefaultRenderer(new FathertableHeadrenderForTarget(table));
        
        table.addMouseListener(new fatherTableLink());
        //frame.getContentPane().add(new JScrollPane(table), "Center");
       // frame.setSize(800, 600);
       // frame.setLocationRelativeTo(null);
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // frame.setVisible(true);
        return table;
    }
    class fatherTableLink extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            int column = table.columnAtPoint(e.getPoint());
            if (column == 1) {
                String value = (String) table.getValueAt(row, column);
                String[] v = value.split(":");
                String fv = v[1].trim();
                String url = "http://www.uniprot.org/uniprot/" + fv;
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(url));
                } catch (URISyntaxException | IOException ex) {
                    Logger.getLogger(TestTargetTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
    class targetTableLink extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = targetTable.rowAtPoint(e.getPoint());
            int column = targetTable.columnAtPoint(e.getPoint());
            String[] db = ((String)targetTable.getValueAt(row, 2)).split("<u>");
            String database = db[1];
            String id = (String) targetTable.getValueAt(row, 0); // the data key id
            if (column == 2) {                              
                String value = itj.getsrcid(path, keyID,id);
                String url = "https://www.ebi.ac.uk/chembldb/compound/inspect/" + value;
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(url));
                } catch (URISyntaxException | IOException ex) {
                    Logger.getLogger(TestTargetTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(column == 3 && database.equals("DrugBank") && !((String)targetTable.getValueAt(row, 3)).equals("No Available")){
                String effect = itj.geteffectinfor(path, keyID, id);
                effect = "<html><body>" + effect + "</body></html>";
                new TargetEffectInfor(effect).setVisible(true);
            }

        }
    }

    class TestTargetTableModel extends AbstractTableModel implements TargetTableModel {

        public boolean isRowExpandable(int rowIndex) {
            return true;
        }

        public TableModel getSubModel(int rowIndex) {
            return null;
        }

        public int getColumnCount() {
            return 6;
        }

        public int getRowCount() {
            return targetlength;
        }

        public String getColumnName(int column) {
            String result = "";
            switch (column) {
                case 0:
                    result = "Target Name";
                    break;
                case 1:
                    result = "Ref. Link";
                    break;
                case 2:
                    result = "Species";
                    break;
                case 3:
                    result = "Simi. Score";
                    break;
                case 4:
                    result = "Score";
                    break;
                case 5:
                    result = "Rank";

            }
            return result;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            //return "Test(" + rowIndex + "," + columnIndex + ")";
            if (columnIndex == 1) {
                String link = "<html><body><u>" + target[rowIndex][columnIndex];// + "</u></html>"; color='#8E236B'
                return link;
            } else {
                return target[rowIndex][columnIndex];
            }
        }

        @Override
        public JComponent getSubComponent(int rowIndex) {
            targetTable = new JTable();
            data = new Vector();
            iv = new InitVector();
            hasShow = new Hashtable();
            headerName = new Vector();
            keyID = (String) table.getValueAt(rowIndex, 0);
            data = itj.getTargetdata(path, keyID);
            headerName = iv.getTargetHeader();
            targetTable.setBackground(new Color(254, 254, 254));
            targetTable.setForeground(new Color(0, 0, 0));
            targetTable.setRowHeight(20);
            targetTable.setDefaultRenderer(String.class, new MyCellRenderer());
            CheckTableModelForTarget tableModel = new CheckTableModelForTarget(data, headerName);
            targetTable.setModel(tableModel);
            targetTable.addMouseListener(new targetTableLink());
            targetTable.getTableHeader().setDefaultRenderer(new HeaderCellRenderForTarget(targetTable));
             targetTable.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                int row =  targetTable.rowAtPoint(e.getPoint());
                int col =  targetTable.columnAtPoint(e.getPoint());
                if (row > -1 && col > -1) {
                    Object value =  targetTable.getValueAt(row, col);
                    if (null != value && !"".equals(value)) {
                         targetTable.setToolTipText(value.toString());//悬浮显示单元格内容
                    } else {
                         targetTable.setToolTipText(null);//关闭提示
                    }
                }
            }
        });
             targetTable.addMouseListener(new ShowTargetMol());
            JPanel p = new JPanel(new BorderLayout());
            p.add(targetTable, BorderLayout.CENTER);
            p.add(targetTable.getTableHeader(), BorderLayout.NORTH);
            //JPanel pp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            JPanel pp = new JPanel(new GridLayout());
            pp.add(p);
            return pp;
        }
    }

    public void setpara(JmolPanel jmolPanel, String showMolPath){
        this.showMolPath = showMolPath;
        this.jmolPanel = jmolPanel;
    }
    /**
     * show target molecule
     *
     * @author Little-Kitty
     *
     */
    class ShowTargetMol extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {// left click
                String stringName[] = new String[20];
                showNo = jmolPanel.viewer.getAtomCount();
                if(showNo > 0)
                    showNo = 1;
                else 
                    showNo = 0;
                int colummCount = targetTable.getModel().getColumnCount();// get
                // table
                // column
                // count
                for (int i = 0; i < colummCount - 1; i++) {
                    stringName[i] = targetTable.getModel().getValueAt(targetTable.getSelectedRow(), i).toString();
                }
                show3Dname = stringName[0];
                String path1 = showMolPath + show3Dname + ".mol2";
                File mol2file = new File(path1);
                if (!mol2file.exists()) {
                    show3Dname = stringName[1];
                }
                boolean bl = !(boolean) targetTable.getModel().getValueAt(targetTable.getSelectedRow(), 6);
               targetTable.getModel().setValueAt(bl, targetTable.getSelectedRow(), 6);
                String path2 = showMolPath + show3Dname + ".mol2";
               // if (targetTable.getSelectedColumn() == 6) {                    
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
                        
                            showNo++;
                            hasShow.put(show3Dname, showNo);
                            String controller1 = "load append " + "\"" + path2 + "\"" + " ;frame*" + " ;hide Hydrogens";
                            // String controller2 = "";
                            jmolPanel.viewer.evalString(controller1);
                            // jmolPanel2.viewer.evalString(controller2);
                    } else {
                            int a = (int) hasShow.get(show3Dname);
                            String b = a + ".1";
                            String controller = "zap " + b + " ;hide Hydrogens";
                            jmolPanel.viewer.evalString(controller);
                            hasShow.remove(show3Dname);
                    }
                }
            //}
        }
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
        }
       TestTargetTable tt = new TestTargetTable();
       String testpath = "E:\\Master\\Test\\99ZXL0GSIDResult.xml";
       JTable table = tt.getTargetTable(testpath);
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(table), "Center");
       frame.setSize(800, 600);
       frame.setLocationRelativeTo(null);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setVisible(true);

    }
    private String path;// = "E:\\Master\\Test\\99ZXL0GSIDResult.xml";
    private String show3Dname;
    private String keyID; // node value or name
    private InitTargetJob itj;
    private InitVector iv;
    private String[][] target;
    private int targetlength;
    private TargetTable table;
    public JTable targetTable;
    private Vector data;
    private Vector headerName;
    private Hashtable<String, Integer> hasShow;
    private int showNo;//record the opened molfile num
    private String showMolPath;
    private JmolPanel jmolPanel;
}
