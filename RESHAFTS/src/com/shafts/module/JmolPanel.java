package com.shafts.module;

import com.shafts.utils.CreateMolecule;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;

public class JmolPanel extends JPanel {

    private static final long serialVersionUID = -4747481289286342678L;

    public static void main(String[] args) {
//		(new JmolTest()).viewer.loadInline(strXyzHOH);
        JmolPanel jmolPanel = new JmolPanel();

        JFrame newFrame = new JFrame();
        newFrame.add(jmolPanel);
        newFrame.setSize(500, 500);
        newFrame.setVisible(true);
    }

    public JmolPanel() {
        initialize();
    }

    public void initialize() {
        adapter = new SmarterJmolAdapter();
        viewer = JmolViewer.allocateViewer(this, adapter);
        // viewer.setColorBackground("black");//*******************2013.11.19
        // Color color = new Color(245,245,220);
        viewer.setColorBackground("black");
        viewer.evalString("set language en");
        // viewer.
        //String kongzhi = "dots on";
        // viewer.evalString(kongzhi);
        //viewer
    }

    @Override
    public void paint(Graphics g) {
        getSize(currentSize);
        viewer.renderScreenImage(g, currentSize.width, currentSize.height);
    }

    public void writeimage() {

    }

    public void setpath(String path) {
        this.filePath = path;
    }

    public void createstart() {
        TwoDThread td = new TwoDThread();
        td.start();
    }

    /**
     *
     */
    class TwoDThread extends Thread {

        private long lastmodified1 = 0;
        private final int SLEEP_TIME = 500;
        File tempfile;

        @SuppressWarnings("static-access")
        @Override
        public void run() {
            //tempfile = new File(CreateMolecule.RENDER_FILE_NAME);
            //String tempfilePath = tempfile.getAbsolutePath();
            //
            String tempfilePath = System.getProperty("user.dir") + "\\" + CreateMolecule.RENDER_FILE_NAME;
            tempfile = new File(tempfilePath);
            // JOptionPane.showMessageDialog(null, tempfilePath, "Message",
            // JOptionPane.ERROR_MESSAGE);
            while (true) {
                if (tempfile.exists() && tempfile.lastModified() > lastmodified1) {
                    viewer.openFile(tempfilePath);// Open
                    // CreateMolecule.RENDER_FILE_NAME                    
                    File file = new File(filePath);
                    if (file.exists()) {
                        file.delete();
                    }
                    tempfile.renameTo(file);
                    viewer.openFile(filePath);
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

    public String[] getchains() {
        String chainID = null;
        //String chain[] = null;
        String chaincommand = "show chain";
        chainID = viewer.scriptWaitStatus(chaincommand, jmolSuffix).toString().replace("\n", "").trim();//scriptWait(chaincommand);
        String chain[] = new String[chainID.length() + 1];
        chain[0] = "Select chain...";
        for (int i = 0; i < chainID.length(); i++) {
            chain[i + 1] = String.valueOf(("chain: " + chainID.charAt(i)));
        }
        return chain;
    }
   
    /* public int getmodelcount(){
     var modelInfo = Jmol.getPropertyAsArray(jmolApplet0, "modelInfo");
     alert(modelInfo.modelCount);
     for (int i = 0; i < modelInfo.modelCount; i++)
     alert(modelInfo.models[i].name);
     }*/
    private String filePath;
    //private FileManeger filemanager;
    // private JmolScriptWait jsw;
    private String jmolSuffix;
    public JmolViewer viewer;
    private JmolAdapter adapter;
    private Dimension currentSize = new Dimension();
}
