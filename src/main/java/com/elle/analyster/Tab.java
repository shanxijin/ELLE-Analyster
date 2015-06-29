
package com.elle.analyster;

import com.elle.analyster.presentation.filter.ITableFilter;
import javax.swing.JTable;

/**
 *
 * @author Carlos Igreja
 * @since  June 25, 2015
 */
public class Tab implements ITableNameConstants{

    private String tableName; 
    private JTable table;
    private JTable filteredTable;
    private ITableFilter<?> filter;
    private TableState tableState;
    private float[] colWidthPercent;
    private int totalRecords;
    private int recordsShown;

    /**
     * CONSTRUCTOR
     */
    public Tab() {
        tableName = "";
        table = new JTable();
        filteredTable = new JTable();
        tableState = new TableState();
        totalRecords = 0;
        recordsShown = 0;
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
    
    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getRecordsShown() {
        return getTable().getRowCount();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    
    
    /**************************************************************************
     *************************** Methods **************************************
     **************************************************************************/
    
    /**
     * 
     * @return 
     */
    public String getRecordsLabel(){
        
        String output;
        
        switch (getTableName()) {
            case ASSIGNMENTS_TABLE_NAME:
                output = "<html><pre>"
                       + "Number of records in Assignments: " + getTotalRecords() 
                  + "<br/>         Number of records shown: " + getRecordsShown()
                     + "</pre></html>";
                break;
            case REPORTS_TABLE_NAME:
                output = "<html><pre>"
                       + "Number of records in Reports: " + getTotalRecords() 
                  + "<br/>     Number of records shown: " + getRecordsShown()
                     + "</pre></html>";
                break;
            case ARCHIVE_TABLE_NAME:
                output = "<html><pre>"
                       + "Number of records in Archive: " + getTotalRecords() 
                  + "<br/>     Number of records shown: " + getRecordsShown()
                     + "</pre></html>";
                break;
            default:
                // this means an invalid table name constant was passed
                // this exception will be handled and thrown here
                // the program will still run and show the stack trace for debugging
                output = "<html><pre>"
                       + "*******ATTENTION*******"
                  + "<br/>Not a valid table name constant entered"
                     + "</pre></html>";
                try {
                    String errorMessage = "ERROR: unknown table";
                    throw new NoSuchFieldException(errorMessage);
                } catch (NoSuchFieldException ex) {
                    ex.printStackTrace();
                    // post to log.txt
                    Analyster.getInstance().getLogwind().sendMessages(ex.getMessage());
                }
        
                break;
        }
        
        return output;
    }
}
