package com.server.utils;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class CheckTableModle extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    public CheckTableModle(Vector data, Vector columnNames) {
        super(data, columnNames);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
            return false;
    }
}
