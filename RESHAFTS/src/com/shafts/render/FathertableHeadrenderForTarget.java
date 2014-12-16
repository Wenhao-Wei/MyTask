package com.shafts.render;

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

public class FathertableHeadrenderForTarget implements TableCellRenderer{
	CheckTableModelForTarget tableModel;
    JTableHeader tableHeader;

    public FathertableHeadrenderForTarget(final JTable table) {
        //this.tableModel = (CheckTableModelForTarget)table.getModel();
        this.tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(51,51,51));
        tableHeader.setForeground(new Color(255,255,255));
        //TableColumn column = table.getColumnModel().getColumn(0);
       // column.setPreferredWidth(100);
         table.setDefaultRenderer(String.class, new MyCellRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
       table.getColumnModel().getColumn(5).setMaxWidth(50);           
        //悬浮提示单元格的值 
        table.setRowHeight(25);
        table.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row > -1 && col > -1) {
                    Object value = table.getValueAt(row, col);
                    if (null != value && !"".equals(value)) {
                        table.setToolTipText(value.toString());//悬浮显示单元格内容
                    } else {
                        table.setToolTipText(null);//关闭提示
                    }
                }
            }
        });
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
