
/**
 * User: danielabecker
 * 
 * -Refactored
 * @author Carlos Igreja
 * @since 6-29-2015
 * @version 0.6.5b
 */

package com.elle.analyster;

import com.elle.analyster.presentation.filter.DistinctColumnItem;
import com.elle.analyster.presentation.filter.ITableFilter;
import com.elle.analyster.presentation.filter.TableRowFilterSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Collection;

import static com.elle.analyster.service.Connection.connection;
import java.util.Map;


public class LoadTables implements ITableNameConstants{

    private Analyster ana =Analyster.getInstance();
    JLabel recordsLabel = ana.getRecordsLabel();

    Logger log = LoggerFactory.getLogger(LoadTables.class);
    
    Map<String,Tab> tabs = ana.getTabs(); // to store ne tab objects
    
    JTable assignmentTable = tabs.get(ASSIGNMENTS_TABLE_NAME).getTable();
    JTable reportTable = tabs.get(REPORTS_TABLE_NAME).getTable();
    JTable archiveAssignTable = tabs.get(ARCHIVE_TABLE_NAME).getTable();


    /**
     *  called once to initialize the total row counts of each tabs table
     * @param tabs
     * @return 
     */
    public Map<String,Tab> initTotalRowCounts(Map<String,Tab> tabs) {
        
        int totalRecords;
 
        boolean isFirstTabRecordLabelSet = false;
        
        for (Map.Entry<String, Tab> entry : tabs.entrySet())
        {
            totalRecords = tabs.get(entry.getKey()).getTable().getRowCount();
            tabs.get(entry.getKey()).setTotalRecords(totalRecords);
            
            if(isFirstTabRecordLabelSet == false){
                ana.getRecordsLabel()
                        .setText(tabs.get(entry.getKey()).getRecordsLabel());
                isFirstTabRecordLabelSet = true; // now its set
            }
        }

        return tabs;
    }
    
    
    /**
     * This method takes a tabs Map and loads all the tabs/tables
     * @param tabs
     * @return 
     */
    public Map<String,Tab> loadTables(Map<String,Tab> tabs) {
        
        for (Map.Entry<String, Tab> entry : tabs.entrySet())
        {
            loadTable(tabs.get(entry.getKey()).getTable());
            ana.setTerminalsFunction(tabs.get(entry.getKey()).getTable());
        }

        ana.setLastUpdateTime();
        
        return tabs;
    }
    

      /**
       * This method takes a table and loads it
       * Does not need to pass the table back since it is passed by reference
       * However, it can make the code clearer and it's good practice to return
       * @param table 
       */
    public JTable loadTable(JTable table) {
        
        // this is temporary switch statement to handle current code
        switch(table.getName()){
            case ASSIGNMENTS_TABLE_NAME:
                
                try {
                    connection(ana.sqlQuery(Analyster.getAssignmentsTableName()), assignmentTable);
                } catch (SQLException e) {
                    log.error("Error", e);
                }
                ana.setColumnFormat(ana.columnWidthPercentage1, assignmentTable);
                ana.getAssignments().init(assignmentTable, new String[]{"Symbol", "Analyst"});
                ana.setFilterTempAssignment(TableRowFilterSupport.forTable(assignmentTable).actions(true).apply());
                ana.getFilterTempAssignment().getTable();   // create filter when the table is loaded.
                //ana.setNumberAssignmentInit(assignmentTable.getRowCount());
                ana.getjActivateRecord().setEnabled(false);
                ana.getjArchiveRecord().setEnabled(true);

                // testing
                tabs.get("Assignments").setFilter(ana.getFilterTempAssignment());
                //tabs.get("Assignments").setTotalRecords(assignmentTable.getRowCount());

                // set label record information -> this should only happen in Analyster
                //recordsLabel.setText(tabs.get(ASSIGNMENTS_TABLE_NAME).getRecordsLabel()); 
                
                break;
                
            case REPORTS_TABLE_NAME:
                
                try {
                    connection(ana.sqlQuery(Analyster.getReportsTableName()), reportTable);
                } catch (SQLException e) {
                    log.error("Error", e);
                }
                ana.setColumnFormat(ana.columnWidthPercentage2, reportTable);
                ana.getReports().init(reportTable, new String[]{"Symbol", "Author"});
                //ana.setNumberReportsInit(reportTable.getRowCount());

                //ana.tabs.get("Reports").setTotalRecords(reportTable.getRowCount());
                
                break;
                
            case ARCHIVE_TABLE_NAME:
                
                try {
                    connection(ana.sqlQuery(Analyster.getArchiveTableName()), archiveAssignTable);
                } catch (SQLException e) {
                    log.error("Error", e);
                }
                ana.setColumnFormat(ana.columnWidthPercentage1, archiveAssignTable);
                ana.getArchiveAssign().init(archiveAssignTable, new String[]{"Symbol", "Analyst"});
                ana.setTerminalsFunction(archiveAssignTable);
                //ana.setNumberArchiveAssignInit(archiveAssignTable.getRowCount());

                //ana.tabs.get("Assignments_Archived").setTotalRecords(archiveAssignTable.getRowCount());
                
                break; 
                
            default:
                
                // do nothing this is a temporary switch statement until it can be refactored better
                break;
                
        }
        
        return table;
    }
    
    
    /**
     * This method is called by LoadPrevious method in Analyster class
     * @param columnIndex
     * @param filterCriteria 
     */
    public void loadAssignmentTableWithFilter(int columnIndex, Collection<DistinctColumnItem> filterCriteria) {

        try {
            connection(ana.sqlQuery(Analyster.getAssignmentsTableName()), assignmentTable);
        } catch (SQLException e) {
            log.error("Error", e);
        }
        ana.setColumnFormat(ana.columnWidthPercentage1, assignmentTable);
        ana.getAssignments().init(assignmentTable, new String[]{"Symbol", "Analyst"});
        ITableFilter<?> filter = TableRowFilterSupport
                                        .forTable(assignmentTable)
                                        .actions(true)
                                        .apply();
        ana.setFilterTempAssignment(filter);
        ana.getFilterTempAssignment().getTable();   // create filter when the table is loaded.
        //ana.setNumberAssignmentInit(assignmentTable.getRowCount());
        ana.getjActivateRecord().setEnabled(false);
        ana.getjArchiveRecord().setEnabled(true);


        // testing, looks like just filter and number
        tabs.get("Assignments").setFilter(filter);
        //tabs.get("Assignments").setTotalRecords(assignmentTable.getRowCount());

        // set label record information -> this should not be done here : only in Analyster
        //recordsLabel.setText(tabs.get(ASSIGNMENTS_TABLE_NAME).getRecordsLabel()); 

        // why is this code here?
        filter.apply(columnIndex, filterCriteria);
        filter.saveTableState();
        filter.saveFilterCriteria(filterCriteria);
        filter.setColumnIndex(columnIndex);


    }
      
} // end LoadTables
