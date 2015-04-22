/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.utils;

import java.util.Vector;

/**
 *
 * @author Little-Kitty
 * @date 2015-4-17 11:03:22
 */
public class TableSorter {
    
    /**
     * from big to small
     * @param tableData
     * @param col
     * @return 
     */
    public Vector sortTable(Vector tableData, int col){
       // Vector data = new Vector();
        for(int i = 1; i < tableData.size(); i++){                
            for(int j = i+1; j < tableData.size(); j++){
                Vector data1 = (Vector) tableData.get(i);
                Vector data2 = (Vector) tableData.get(j);
                if(Double.parseDouble((String) data1.get(col)) < Double.parseDouble((String) data2.get(col))){
                    //Vector v = new Vector();
                    //v = data1;
                    tableData.setElementAt(data2, i);
                    tableData.setElementAt(data1, j);
                }
            }
        }
        return tableData;
    }
    
    public Vector reverseVector(Vector tableData){
        Vector data = new Vector();
        data.add(tableData.get(0));
        for(int i = tableData.size()-1; i > 0; i--){
            data.add(tableData.get(i));
        }
        return data;
    }
    public static void main(String args[]){
        Vector v1 = new Vector();
        Vector v = new Vector();
        v1.add("0");
        v1.add("diyiwei");
        v1.add("1.5");
        v1.add("3.2");
        v1.add(false);
        v.add(v1);
        v1 = new Vector();
        v1.add("0");
        v1.add("dierwei");
        v1.add("1");
        v1.add("3");
        v1.add(false);
        v.add(v1);
        v1 = new Vector();
        v1.add("0");
        v1.add("disanwei");
        v1.add("1.734");
        v1.add("2.45");
        v1.add(false);
        v.add(v1);
        v1 = new Vector();
        v1.add("0");
        v1.add("disiwei");
        v1.add("1.256");
        v1.add("1.325");
        v1.add(false);
        v.add(v1);
        System.out.println("排序前：******************************");
        for(int i = 0; i < v.size(); i++)
            System.out.println(v.get(i));
        Vector vv = new TableSorter().sortTable(v, 3);
        System.out.println("排序后：******************************");
        for(int i = 0; i < vv.size(); i++)
            System.out.println(vv.get(i));
    }

}
