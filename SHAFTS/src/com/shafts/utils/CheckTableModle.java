package com.shafts.utils;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class CheckTableModle extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    public CheckTableModle(Vector data, Vector columnNames) {
        super(data, columnNames);

    }

    public boolean isCellEditable(int row, int column) {
        if (column == 5) {
            return true;
        } else {
            return false;
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void selectAllOrNull(boolean value) {
        for (int i = 0; i < getRowCount(); i++) {
            this.setValueAt(value, i, 5);
        }
    }

}
