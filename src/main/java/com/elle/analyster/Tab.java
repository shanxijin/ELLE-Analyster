
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
    private JTable filtered;
    private ITableFilter<?> filter;
    private int numberInit;
    private TableState tableState;

    /**
     * CONSTRUCTOR
     */
    public Tab() {
        table = new JTable();
        filtered = new JTable();
        numberInit = 0;
        tableState = new TableState();
        // filter is an instance and does not get initialized
    }
    
    /**
     * CONSTRUCTOR
     * @param table
     * @param filtered
     * @param filter
     * @param numberInit
     * @param tableState 
     */
    public Tab(JTable table, JTable filtered, ITableFilter<?> filter, int numberInit, TableState tableState) {
        this.table = table;
        this.filtered = filtered;
        this.filter = filter;
        this.numberInit = numberInit;
        this.tableState = tableState;
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
    
    public JTable getFiltered() {
        return filtered;
    }

    public void setFiltered(JTable filtered) {
        this.filtered = filtered;
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

}
