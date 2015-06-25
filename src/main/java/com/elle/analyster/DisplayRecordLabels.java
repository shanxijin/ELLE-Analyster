
package com.elle.analyster;

/**
 * @author Carlos Igreja
 * @since June 24, 2015
 */
public class DisplayRecordLabels implements ITableNameConstants{
    
    // attributes
    private String tableName;
    private int totalRecords;
    private int recordsShown;

    /**
     * CONSTRUCTOR
     */
    public DisplayRecordLabels() {
        tableName = "";
        totalRecords = 0;
        recordsShown = 0;
    }

    /**
     * CONSTRUCTOR
     * @param tableName
     * @param totalRecords
     * @param recordsShown 
     */
    public DisplayRecordLabels(String tableName, int totalRecords, int recordsShown) {
        this.tableName = tableName;
        this.totalRecords = totalRecords;
        this.recordsShown = recordsShown;
    }
    
    /**************************************************************************
     ********************** Setters & Getters *********************************
     **************************************************************************/
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getRecordsShown() {
        return recordsShown;
    }

    public void setRecordsShown(int recordsShown) {
        this.recordsShown = recordsShown;
    }
    
    /**************************************************************************
     *************************** Methods **************************************
     **************************************************************************/
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString(){
        
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
        
} // end DisplayRecordsLabels
