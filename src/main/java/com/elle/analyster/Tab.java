
package com.elle.analyster;

import com.elle.analyster.presentation.filter.ITableFilter;
import javax.swing.JTable;

/**
 *
 * @author Carlos Igreja
 * @since  June 25, 2015
 */
public class Tab {

    private JTable table;
    private JTable filteredTable;
    private ITableFilter<?> filter;
    private int numberInit;
    private TableState tableState;
    private float[] colWidthPercent;

    /**
     * CONSTRUCTOR
     */
    public Tab() {
        table = new JTable();
        filteredTable = new JTable();
        numberInit = 0;
        tableState = new TableState();
        // filter is an instance and does not get initialized
    }
    
    /**************************************************************************
     ********************** Setters & Getters *********************************
     **************************************************************************/

    
    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }
    
    public JTable getFilteredTable() {
        return filteredTable;
    }

    public void setFilteredTable(JTable filteredTable) {
        this.filteredTable = filteredTable;
    }

    public ITableFilter<?> getFilter() {
        return filter;
    }

    public void setFilter(ITableFilter<?> filter) {
        this.filter = filter;
    }

    public int getNumberInit() {
        return numberInit;
    }

    public void setNumberInit(int numberInit) {
        this.numberInit = numberInit;
    }

    public TableState getTableState() {
        return tableState;
    }

    public void setTableState(TableState tableState) {
        this.tableState = tableState;
    }

    public float[] getColWidthPercent() {
        return colWidthPercent;
    }

    public void setColWidthPercent(float[] colWidthPercent) {
        this.colWidthPercent = colWidthPercent;
    }
}
