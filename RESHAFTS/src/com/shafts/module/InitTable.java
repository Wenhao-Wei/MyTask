/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.module;

import com.shafts.render.CheckHeaderCellRenderer;
import com.shafts.render.CheckTableModle;
import com.shafts.render.MyCellRenderer;
import java.awt.Color;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-14 10:51:12
 */
public class InitTable {
    public JTable getTable(Vector data, Vector columnNames){
        TableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
         jTable = new JTable();
         jTable.setCellSelectionEnabled(true);
                    jTable.setBackground(new Color(254, 254, 254));
                    jTable.setForeground(new Color(0, 0, 0));
                    jTable.setDefaultRenderer(String.class, new MyCellRenderer());
                    CheckTableModle tableModel = new CheckTableModle(data, columnNames);
                    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
                    
                    
        jTable.setRowSorter(sorter);
                    jTable.setModel(tableModel);
                    jTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(jTable));
                    sorter.setSortable(0, false);
                    sorter.setSortable(1, false);
                    sorter.setSortable(5, false);
                    return jTable;
    }
private JTable jTable;
}
