/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.module;

import com.shafts.render.CheckHeaderCellRenderer;
import com.shafts.render.CheckTableModle;
import com.shafts.render.MyCellRenderer;
import com.shafts.utils.TableSorter;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-14 10:51:12
 */
public class InitTable {

    public JTable getTable(final Vector data, Vector columnNames) {
        this.columnNames = columnNames;
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
        //TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);

       // jTable.setRowSorter(sorter);
        jTable.setModel(tableModel);
        jTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(jTable));
        jTable.getTableHeader().addMouseListener(new SortTable());
        //sorter.setSortable(0, false);
        //sorter.setSortable(1, false);
        //sorter.setSortable(5, false);
        //sorter.setRowFilter(null);
        /*sorter.addRowSorterListener(new RowSorterListener(){

         @Override
         public void sorterChanged(RowSorterEvent e) {
         int row;
         //int col;
         for (row = 0; row < jTable.getRowCount(); row++) {
         if (row != 0 &&  jTable.getValueAt(row, 1).equals("Input")) {
         System.out.println("***************");
         tableModel.moveRow(row, row, 0);
         jTable.repaint();
         }
         }
         }
         });*/
        return jTable;
    }

    class SortTable extends MouseAdapter {

        int col2Flag = 1;
        int col3Flag = 0;
        int col4Flag = 0;
        Vector sortedData;
        TableSorter tableSorter = new TableSorter();

        @Override
        public void mouseClicked(MouseEvent e) {
            Vector sortData = new Vector();
            for (int i = 0; i < jTable.getRowCount(); i++) {
                Vector rowData = new Vector();
                for (int j = 0; j < jTable.getColumnCount(); j++) {
                    rowData.add(jTable.getValueAt(i, j));
                }
                sortData.add(rowData);
            }
            int col = jTable.getTableHeader().columnAtPoint(e.getPoint());
            if (col == 2 || col == 3 || col == 4) {
                switch (col) {
                    case 2:
                        sortedData = new Vector();
                        col3Flag = 0;
                        col4Flag = 0;
                        if (col2Flag == 0) {
                            //no sort
                            col2Flag = 1;
                            sortedData = tableSorter.sortTable(sortData, col);
                        }
                        else{
                           sortedData = tableSorter.reverseVector(sortData);
                        }
                        jTable.removeAll();
                        CheckTableModle tableModel = new CheckTableModle(sortedData, columnNames);
                        jTable.setModel(tableModel);
                        jTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(jTable));
                        jTable.getTableHeader().addMouseListener(this);
                        jTable.repaint();

                        break;
                    case 3:
                        sortedData = new Vector();
                        col2Flag = 0;
                        col4Flag = 0;
                        if (col3Flag == 0) {
                            //no sort
                            col3Flag = 1;
                            sortedData = tableSorter.sortTable(sortData, col);
                        }
                        else{
                           sortedData = tableSorter.reverseVector(sortData);
                        }
                        jTable.removeAll();
                        CheckTableModle tableModel1 = new CheckTableModle(sortedData, columnNames);
                        jTable.setModel(tableModel1);
                        jTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(jTable));
                        jTable.getTableHeader().addMouseListener(this);
                        jTable.repaint();

                        break;
                    case 4:
                        sortedData = new Vector();
                        col3Flag = 0;
                        col2Flag = 0;
                        if (col4Flag == 0) {
                            //no sort
                            col4Flag = 1;
                            sortedData = tableSorter.sortTable(sortData, col);
                        }
                        else{
                           sortedData = tableSorter.reverseVector(sortData);
                        }
                        jTable.removeAll();
                        CheckTableModle tableModel2 = new CheckTableModle(sortedData, columnNames);
                        jTable.setModel(tableModel2);
                        jTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(jTable));
                        jTable.getTableHeader().addMouseListener(this);
                        jTable.repaint();

                        break;
                }
            }
        }
    }
    private JTable jTable;
    private Vector columnNames;
}
