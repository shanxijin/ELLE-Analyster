
package com.elle.analyster;

import com.elle.analyster.presentation.filter.ITableFilter;
import javax.swing.JTable;

/**
 *
 * @author Carlos Igreja
 * @since  June 25, 2015
 */
public class Tab {
    
    private JTable filtered;
    private ITableFilter<?> filter;
    private int numberInit;
    private TableState tableState;

    /**
     * CONSTRUCTOR
     */
    public Tab() {
        filtered = new JTable();
        numberInit = 0;
        tableState = new TableState();
        // filter is an instance and does not get initialized
    }
    
    /**
     * CONSTRUCTOR
     * @param filtered
     * @param filter
     * @param numberInit
     * @param tableState 
     */
    public Tab(JTable filtered, ITableFilter<?> filter, int numberInit, TableState tableState) {
        this.filtered = filtered;
        this.filter = filter;
        this.numberInit = numberInit;
        this.tableState = tableState;
    }
    
    /**************************************************************************
     ********************** Setters & Getters *********************************
     **************************************************************************/

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
