package com.github.worldmaomao.ui.tableModel;

import javax.swing.table.DefaultTableModel;

/**
 */
public class DiskItemSearchTableModel extends DefaultTableModel {

    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 0:
                return true;
            default:
                return false;

        }
    }


    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        }
        return Object.class;
    }
}
