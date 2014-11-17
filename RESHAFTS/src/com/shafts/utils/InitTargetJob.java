/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.utils;

import static com.shafts.utils.JobInfor_XML.loadInit;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Little-Kitty
 * @date 2014-11-13 13:09:52
 */
public class InitTargetJob {
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
            if (file.exists()) {

                document = builder.parse(new File(filePath));
                document.normalize();
            }
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(InitTargetJob.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }

    /**
     * get the target data
     *
     * @param filePath
     * @return
     */
    public String[][] getTarget(String filePath) {
        Document document = loadInit(filePath);
        //get leaf node
        NodeList nodeList = document.getElementsByTagName("target");
        row = nodeList.getLength();
        String[][] target = new String[row][6];
        for (int i = 0; i < row; i++) {
            String targetname;
            String reflink;
            String species = "Not Available";;
            String simiscore;
            String score;
            String rank;
            targetname = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
            Node rootchild = nodeList.item(i);
            NamedNodeMap attributes = rootchild.getAttributes();
            reflink = "swiss-port:" + attributes.getNamedItem("uniprot").getNodeValue();
            for (int j = 0; j < attributes.getLength(); j++) {
                if (attributes.item(j).getNodeName().equals("species")) {
                    species = attributes.getNamedItem("species").getNodeValue();
                    break;
                }
            }
            //System.out.println(attributes.item(6).getNodeName());
            simiscore = attributes.getNamedItem("simiscore").getNodeValue();
            score = attributes.getNamedItem("score").getNodeValue();
            rank = attributes.getNamedItem("rank").getNodeValue();
            //System.out.println("*****" + targetname + "*****" + reflink + "*****" + species + "*****" + simiscore + "*****" + score + "*****" + rank);
            target[i][0] = targetname;
            target[i][1] = reflink;
            target[i][2] = species;
            target[i][3] = simiscore;
            target[i][4] = score;
            target[i][5] = rank;
        }
        return target;
    }

    public int gettargetlength() {
        return row;
    }

    /**
     * 获取带注释结果的数据
     *
     * @param filePath
     * @param KeyID
     * @return
     */
    public Vector getTargetdata(String filePath, String KeyID) {
        Document document = loadInit(filePath);
        Vector data = new Vector();
        NodeList nodeList = document.getElementsByTagName("target");
        for (int i = 0; i < nodeList.getLength(); i++) {
            String targetname = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
            if (targetname.equals(KeyID)) {//find the compounds

                NodeList nodeList1 = document.getElementsByTagName("compounds").item(i).getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node compoundNode = nodeList1.item(j);
                    NamedNodeMap attributes = compoundNode.getAttributes();
                    String id = attributes.getNamedItem("id").getNodeValue();
                    String score = attributes.getNamedItem("score").getNodeValue();
                    String db = attributes.getNamedItem("srcdb").getNodeValue();
                    //String srcdb = attributes.getNamedItem("srcid").getNodeValue();
                    NodeList compoundList = compoundNode.getChildNodes(); //find compound item
                    String name = compoundList.item(0).getFirstChild().getNodeValue();
                    String smiles = compoundList.item(1).getFirstChild().getNodeValue();
                    Node activityNode = null;
                    NamedNodeMap attributes1 = null;
                    if (compoundList.item(3).getFirstChild() != null) {
                        activityNode = compoundList.item(3).getFirstChild();
                        attributes1 = activityNode.getAttributes();
                    }
                    String activity = "No Available";
                    switch (db) {
                        case "ChEMBL":
                            if (attributes1 != null) {
                                activity = attributes1.getNamedItem("type").getNodeValue() + " " + attributes1.getNamedItem("value").getNodeValue();
                            }
                            break;
                        case "DrugBank":
                            if (compoundList.item(2).getFirstChild() != null) {//chek the effects if null
                                activity = "<html><body><u> Model of Action";
                                //effect = compoundList.item(2).getFirstChild().getNodeValue();                                                                  
                            }
                            break;
                    }

                    Vector rowdata = new Vector();
                    //System.out.println("*****" + id + "*****" + name + "*****" + srcdb + "*****" + activity + "*****" + score + "*****" + smiles);
                    rowdata.add(id);
                    rowdata.add(name);
                    rowdata.add("<html><body><u>" + db);
                    rowdata.add(activity);
                    rowdata.add(score);
                    rowdata.add(smiles);
                    rowdata.add(false);
                    data.add(rowdata);
                }
            }
        }
        return data;
    }

    /**
     * get the compound effect infor
     *
     * @param filePath
     * @param KeyID
     * @param id
     * @return
     */
    public String geteffectinfor(String filePath, String KeyID, String id) {
        Document document = loadInit(filePath);
        NodeList nodeList = document.getElementsByTagName("target");
        for (int i = 0; i < nodeList.getLength(); i++) {
            String targetname = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
            if (targetname.equals(KeyID)) {//find the compounds
                NodeList nodeList1 = document.getElementsByTagName("compounds").item(i).getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node compoundNode = nodeList1.item(j);
                    NamedNodeMap attributes = compoundNode.getAttributes();
                    String dataid = attributes.getNamedItem("id").getNodeValue();
                    if (dataid.equals(id)) {
                        NodeList compoundList = compoundNode.getChildNodes(); //find compound item
                        effect = compoundList.item(2).getFirstChild().getNodeValue();
                        break;
                    }
                }
            }
        }
        return effect;
    }

    /**
     * get the source database
     *
     * @param filePath
     * @param KeyID
     * @param id
     * @return
     */
    public String getsrcid(String filePath, String KeyID, String id) {
        Document document = loadInit(filePath);
        String srcid = null;
        NodeList nodeList = document.getElementsByTagName("target");
        for (int i = 0; i < nodeList.getLength(); i++) {
            String targetname = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
            if (targetname.equals(KeyID)) {//find the compounds
                NodeList nodeList1 = document.getElementsByTagName("compounds").item(i).getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node compoundNode = nodeList1.item(j);
                    NamedNodeMap attributes = compoundNode.getAttributes();
                    String dataid = attributes.getNamedItem("id").getNodeValue();
                    if (dataid.equals(id)) {
                        srcid = attributes.getNamedItem("srcid").getNodeValue();
                    }
                    break;
                }
            }
        }
        return srcid;
    }

    /**
     * 获取goid并解析成html
     *
     * @param filePath
     * @param KeyID name
     * @return
     */
    public String getHtml(String filePath, String KeyID) {
        String html = null;
        Document document = loadInit(filePath);
        //get leaf node
        NodeList nodeList = document.getElementsByTagName("target");
        for (int i = 0; i < nodeList.getLength(); i++) {
            String name = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
            Node rootchild = nodeList.item(i);
            NamedNodeMap attributes = rootchild.getAttributes();
            String confirm = attributes.getNamedItem("confirm").getNodeValue();
            if (name.equals(KeyID) && confirm.equals("Predicted")) {//存在相关的Go标签
                html = "";
                html += "<html>\n";
                html += "<body>\n";
                html += "<font face=\"黑体\"><b> GeneID: </b></font><a href=\"http://www.ncbi.nlm.nih.gov/gene/" + attributes.getNamedItem("geneid").getNodeValue() + "\">" + attributes.getNamedItem("geneid").getNodeValue() + "</a><br>\n";
                html += "<font face=\"黑体\"><b>Gene Name:</b></font>" + attributes.getNamedItem("genename").getNodeValue() + "<br>\n";
                html += "<font face=\"黑体\"><b>Synonyms:</b></font>" + attributes.getNamedItem("genesyn").getNodeValue() + "<br>\n";
                html += "<font face=\"黑体\"><b>Function:</b></font><br>\n";
                html += "<p style=\"text-align:justify\">";
                html += document.getElementsByTagName("function").item(i).getFirstChild().getNodeValue() + "<br>\n";
                NodeList nodeList1 = document.getElementsByTagName("go_list").item(i).getChildNodes();
                html += "</p>";
                Node pathwaylist = document.getElementsByTagName("pathway_list").item(i).getFirstChild();
                if (pathwaylist != null) {
                    NamedNodeMap attributes2 = pathwaylist.getAttributes();
                    String id = attributes2.getNamedItem("id").getNodeValue();
                    String pathwaylink = attributes2.getNamedItem("link").getNodeValue();
                    html += "<font face=\"黑体\"><b>Pathway:</b> </font><br>\n";
                    html += "<table width=\"80%\" border=\"1\">\n";
                    html += "<tr><th><font face=\"黑体\"> Reactom ID </font></th>\n";
                    html += "<th><font face=\"黑体\"> Name </font></th></tr>\n";
                    html += "<tr><th><a href=\"" + pathwaylink + "\">" + id + "</a></th>\n";
                    html += "<th>" + pathwaylist.getFirstChild().getNodeValue() + "</th></tr></table><br><br>\n";
                }
                html += "<font face=\"黑体\"><b> Go: </b></font><br>\n";
                html += "<table width=\"80%\" border=\"1\">\n";
                html += "<tr><th><font face=\"黑体\"> GO ID </font></th>\n";
                html += "<th><font face=\"黑体\"> Definition </font></th>\n";
                html += "<th><font face=\"黑体\"> Branch </font></th></tr>\n";
                for (int j = 0; j < nodeList1.getLength(); j++) { // find go
                    Node nodeChild = nodeList1.item(j);
                    NamedNodeMap attributes1 = nodeChild.getAttributes();
                    String id1 = attributes1.getNamedItem("id").getNodeValue();
                    String link1 = "http://amigo.geneontology.org/amigo/term/" + id1;
                    String branch = attributes1.getNamedItem("branch").getNodeValue();
                    String goname = nodeChild.getFirstChild().getNodeValue();
                    html += "<tr><th><a href=\"" + link1 + "\">" + id1 + "</a></th>\n";
                    html += "<th>" + goname + "</th>\n";
                    html += "<th>" + branch + "</th></tr>\n";
                }
                html += "</table></body></html>\n";
            }
        }
        return html;
    }

    private int row;

    public static void main(String args[]) {
        InitTargetJob ttt = new InitTargetJob();
    //int a = ttt.gettargetlength("E:\\Master\\Test\\99ZXL0GSIDResult.xml");
        //  System.out.println(a);
        //String[][] target = new String[a][6];
        //String[][] target = ttt.getTarget("E:\\Master\\MyOffice\\NetBeansWorkspace\\RESHAFTS\\workspace\\network\\TargetNavigator\\23672\\ZR8KOY3YYAResult.xml");//.getTarget("E:\\Master\\MyOffice\\NetBeansWorkspace\\RESHAFTS\\workspace\\network\\TargetNavigator\\23672\\ZR8KOY3YYAResult.xml","Prostaglandin G/H synthase 2");
        Vector v = new Vector();
        v = ttt.getTargetdata("E:\\Master\\MyOffice\\NetBeansWorkspace\\RESHAFTS\\workspace\\network\\TargetNavigator\\23672\\ZR8KOY3YYAResult.xml", "Aromatic-amino-acid aminotransferase");//,"Aromatic-amino-acid aminotransferase");
// for(int i = 0; i < target.length; i++){
        // System.out.println(target[i][0] + "*****" + target[i][1] + "*****" + target[i][2] + "*****" + target[i][3] + "*****" + target[i][4] + "*****" + target[i][5]);
        //  }
        //System.out.println("***************" + target);
    }
    private String effect;
}
