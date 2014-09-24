package com.server.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class CheckHeaderCellRenderer implements TableCellRenderer {
    CheckTableModle tableModel;
    JTableHeader tableHeader;

    public CheckHeaderCellRenderer(JTable table) {
        this.tableModel = (CheckTableModle)table.getModel();
        this.tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(0,204,51));
        //TableColumn column = table.getColumnModel().getColumn(0);
       // column.setPreferredWidth(100);;
       // selectBox.setEnabled(false);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub
        String valueStr = (String) value;
        JLabel label = new JLabel(valueStr);
        label.setHorizontalAlignment(SwingConstants.CENTER); 
        JComponent component = label;//(column == 5) ? selectBox : label; 
        component.setForeground(tableHeader.getForeground());
        component.setBackground(tableHeader.getBackground());
        component.setFont(tableHeader.getFont());
        component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

        return component;
    }

}

