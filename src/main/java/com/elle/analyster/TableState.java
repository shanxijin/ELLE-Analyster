
package com.elle.analyster;

import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import java.util.*;

public class TableState {

    private JTable table;
    private int rowsNum;
    private int columnsNum;
    private int recordsNum;
    private TableRowSorter sorter;
    private String[] searchFields;
    private Vector columnNames = new Vector();
    private Vector data = new Vector();


    public TableState() {
        // to do  
    }

    public TableState(JTable t) {
        table = t;
    }

    public void init(JTable t, String[] sf) {  // init should only be called after "select * from" command in Analyster (loadData)
        table = t;
        columnsNum = table.getColumnCount();
        recordsNum = table.getRowCount();
        setSearchFields(sf);
        setColumnNames();
        setData();
        // (to do) initialize sorter
    }

    public int getRecordsNumber() {
        return recordsNum;
    }


    public Vector getData() {
        return data;
    }

    public String[] getSearchFields() {
        return searchFields;
    }

    public int getRowsNumber() {
        return rowsNum;
    }

    public TableRowSorter getSorter() {
        return sorter;
    }

    public void setData(Vector dataset) {
        data = dataset;
    }

    public void setData() {
        data.clear();
        for (int i = 0; i < rowsNum; i++) {
            Vector rowData = new Vector(columnsNum);
            for (int j = 0; j < columnsNum; j++) {
                rowData.add(table.getValueAt(i, j));
            }
            data.add(rowData);
        }
    }

    public void setColumnNames() {
        int i;
        columnNames.clear();
        for (i = 0; i < table.getColumnCount(); i++) {
            columnNames.add(table.getColumnName(i));
        }
    }

    public void setColumnNames(Vector col) {
        columnNames = col;
    }

    public void setRowsNumber(int num) {
        rowsNum = num;
    }

    public void setRecordsNumber(int num) {
        recordsNum = num;
    }

    public void setSearchFields(String[] temp) {
        searchFields = temp;
    }

}
