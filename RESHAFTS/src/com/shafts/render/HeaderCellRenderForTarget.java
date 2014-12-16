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

public class HeaderCellRenderForTarget implements TableCellRenderer {
    CheckTableModelForTarget tableModel;
    JTableHeader tableHeader;
    final JCheckBox selectBox;

    public HeaderCellRenderForTarget(JTable table) {
        this.tableModel = (CheckTableModelForTarget)table.getModel();
        this.tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(51,51,51));
        tableHeader.setForeground(new Color(255,255,255));
        //TableColumn column = table.getColumnModel().getColumn(0);
       // column.setPreferredWidth(100);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);
      //  table.getColumnModel().getColumn(5).setMaxWidth(50);
        selectBox = new JCheckBox(tableModel.getColumnName(6));
        selectBox.setSelected(false);
       // selectBox.setEnabled(false);
        tableHeader.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    int selectColumn = tableHeader.columnAtPoint(e.getPoint());
                    if (selectColumn == 6) {
                        boolean value = !selectBox.isSelected();
                        //selectBox.setSelected(value);
                        //tableModel.selectAllOrNull(value);
                        tableHeader.repaint();
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
        selectBox.setHorizontalAlignment(SwingConstants.CENTER);
        selectBox.setBorderPainted(true);
        JComponent component = label;//(column == 5) ? selectBox : label; 
        component.setForeground(tableHeader.getForeground());
        component.setBackground(tableHeader.getBackground());
        component.setFont(tableHeader.getFont());
        component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

        return component;
    }

}

