package com.github.worldmaomao.ui.tableModel;

import javax.swing.table.DefaultTableModel;

/**
 */
public class NoEditableTableModel extends DefaultTableModel {


    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
