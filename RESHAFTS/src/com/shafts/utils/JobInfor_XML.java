/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.utils;

/**
 *
 * @author Little-Kitty
 */
import java.awt.HeadlessException;
import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class JobInfor_XML {
    //param of delete node
    //  private static String deleteID;
    //param of update node 
    // private static String updateNumber;
    //读取传入的路径，返回一个document对象
    public static Document loadInit(String filePath) {
        Document document = null;
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (!file.exists()) {
                document = builder.newDocument();
                Element root = document.createElement("MyJob");
                document.appendChild(root);
            } else{
                document = builder.parse(new File(filePath));
                document.normalize();
            }
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * delete node form xml
     *
     * @param deleteID node id
     * @param jobModel whitch kind of job: Local, Target Navigator and Hit
     * Explorer
     * @param filePath XML path
     * @return
     */
    public void deleteXML(String deleteID, String jobModel, String filePath) {
        Document document = loadInit(filePath);
            NodeList nodeList = document.getElementsByTagName("job");
            for (int i = 0; i < nodeList.getLength(); i++) {
                String jobmodel = document.getElementsByTagName("Model").item(i).getFirstChild().getNodeValue();
                String number_ = document.getElementsByTagName("ID").item(i).getFirstChild().getNodeValue();
                //delete the node
                if (jobmodel.equals(jobModel.replace(" ", "")) && number_.equals(deleteID)) {
                    Node node = nodeList.item(i);
                    node.getParentNode().removeChild(node);
                    saveXML(document, filePath);
                    break;
                }
            }
    }

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
     * 
     * @param jobID
     * @param jobModel
     * @param filePath
     * @return 
     */
    public String getJobStatus(String jobID, String jobModel, String filePath){
        String status = null;
         Document document = loadInit(filePath);
            NodeList nodeList = document.getElementsByTagName("job");
            for (int i = 0; i < nodeList.getLength(); i++) {
                String jobmodel = document.getElementsByTagName("Model").item(i).getFirstChild().getNodeValue();
                String number_ = document.getElementsByTagName("ID").item(i).getFirstChild().getNodeValue();                  
                if (jobmodel.equals(jobModel.replace(" ", "")) && number_.equals(jobID)) {
                    String status_ = document.getElementsByTagName("job").item(i).getChildNodes().item(2).getTextContent();
                    status = status_;
                    break;
                }
            }
        return status;
    }
    /**
     * 
     * @param jobID
     * @param jobModel
     * @param filePath 
     */
    public void setJobStatus(String jobID, String jobModel, String filePath){
        Document document = loadInit(filePath);
       
            //get leaf ode
            NodeList nodeList = document.getElementsByTagName("job");//jobModel.replace(" ", "")
            //Traversal leaf node
            for (int i = 0; i < nodeList.getLength(); i++) {
                String jobmodel = document.getElementsByTagName("Model").item(i).getFirstChild().getNodeValue();
                String number = document.getElementsByTagName("ID").item(i).getFirstChild().getNodeValue();
                if (jobmodel.equals(jobModel.replace(" ", "")) && number.equals(jobID)) {
                        Node n = document.getElementsByTagName("job").item(i).getChildNodes().item(2);
                        n.setTextContent("YES");
                        saveXML(document, filePath);
                   break;
                    }             
                }
            
    }
    /**
     * update node from xml
     *
     * @param updateID the node to change
     * @param newID the new name of the node
     * @param jobModel
     * @param filePath
     * @param ResultPath
     * @return
     */
    public boolean updateXML(String updateID, String newID, String jobModel, String filePath, String ResultPath) {
        //get the object of document
        boolean isUpdate = false;
        Document document = loadInit(filePath);
       
            //get leaf ode
            NodeList nodeList = document.getElementsByTagName("job");//jobModel.replace(" ", "")
            //Traversal leaf node
            for (int i = 0; i < nodeList.getLength(); i++) {
                String jobmodel = document.getElementsByTagName("Model").item(i).getFirstChild().getNodeValue();
                String number = document.getElementsByTagName("ID").item(i).getFirstChild().getNodeValue();
                if (jobmodel.equals(jobModel.replace(" ", "")) && number.equals(updateID)) {
                    File file = new File(ResultPath + newID);
                    if (file.exists()) {
                        JOptionPane.showMessageDialog(null, "Failed! Node name has exists!");
                    } else {
                        renameFile(updateID, file, ResultPath); // rename
                        document.getElementsByTagName("ID").item(i).getFirstChild().setTextContent(newID);
                        isUpdate = true;
                    }
                    break;
                }
            }
            saveXML(document, filePath);
   
        return isUpdate;
    }

    /**
     * add node to the XML
     *
     * @param addID
     * @param jobModel
     * @param filePath
     */
    public void addXML(String addID, String jobModel, String filePath) {
        try {
            Document document = loadInit(filePath);
            //create leafnode
            String job = jobModel.replace(" ", "");
            Element newjob = document.createElement("job");
            Element jobStyle = document.createElement("Model");//create the first element
            Element eltNumber = document.createElement("ID");//create the second element            
            Text model = document.createTextNode(job);
            Text id = document.createTextNode(addID);//create the text node             
            eltNumber.appendChild(id);          
            jobStyle.appendChild(model);
            newjob.appendChild(jobStyle);
            newjob.appendChild(eltNumber);
            if (jobModel.equals("Local")) {
                Element eltNumber1 = document.createElement("Status");//create the third element
                Text status = document.createTextNode("NO");
               eltNumber1.appendChild(status);
                //eltNumber1.setNodeValue("NO");
                newjob.appendChild(eltNumber1);
            }
            //get root node
            Element eltRoot = document.getDocumentElement();
            //add the new node to the root
            eltRoot.appendChild(newjob);
            //save
            saveXML(document, filePath);
        } catch (Exception e) {
        }
    }

    /**
     * save the new file
     *
     * @param document
     * @param filePath
     * @return
     */
    public void saveXML(Document document, String filePath) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * get the XML data
     *
     * @param filePath
     * @return
     */
    public void initdataXML(String filePath) {
        LocalOB = new Vector();
        TargetOB = new Vector();
        HitOB = new Vector();
        try {
            Document document = loadInit(filePath);
            //get leaf node
            NodeList nodeList = document.getElementsByTagName("job");
            //traversal local
            for (int i = 0; i < nodeList.getLength(); i++) {
                String jobmodel = document.getElementsByTagName("Model").item(i).getFirstChild().getNodeValue();
                String id = document.getElementsByTagName("ID").item(i).getFirstChild().getNodeValue();
                switch (jobmodel) {
                    case "Local":
                        LocalOB.add(id);
                        break;
                    case "TargetNavigator":
                        TargetOB.add(id);
                        break;
                    case "HitExplorer":
                        HitOB.add(id);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get localjob
     *
     * @return
     */
    public Vector getLocal() {
        return LocalOB;
    }

    /**
     * get target job
     *
     * @return
     */
    public Vector getTarget() {
        return TargetOB;
    }

    /**
     * get hit job
     * @return
     */
    public Vector getHit() {
        return HitOB;
    }

    public static void main(String args[]) {
        //String path = "E:\\Master\\Test\\jobinfor.xml";
        String path = "D:\\MyOffice\\Github\\MyTask\\RESHAFTS\\workspace\\TMP\\jobinfor.xml";
        JobInfor_XML job = new JobInfor_XML();
        job.addXML("25769", "Target Navigator", path);
        //job.setJobStatus("Job20", "Local", path);
        //String s = job.getJobStatus("Job22", "Local", path);
        //job.initdataXML(path);
        //Vector local = job.getLocal();
        //Vector target = job.getTarget();
        //Vector hit = job.getHit();
       // job.deleteXML("delete", "Local", path);
        System.out.println("OK");
       // System.out.println(s);
    }
    private Vector LocalOB;
    private Vector TargetOB;
    private Vector HitOB;
}
