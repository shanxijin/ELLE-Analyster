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
import java.util.HashMap;
import java.util.Map;

/**
 * User: danielabecker
 */
public class LoadTables implements ITableNameConstants, IColumnConstants{

    private Analyster ana = Analyster.getInstance();
    JLabel numOfRecords1 = ana.getNumOfRecords1();
    JLabel numOfRecords2 = ana.getNumOfRecords2();
//    JTable assignmentTable = ana.getassignmentTable();
//    JTable reportTable = ana.getReportTable();
//    JTable archiveAssignTable = ana.getArchiveAssignTable();
//    JTable viewerTable = ana.getViewerTable();
    Logger log = LoggerFactory.getLogger(LoadTables.class);
    
    Map<String,Tab> tabsMap = ana.getTabsMap(); // to store ne tab objects
    
    JTable assignmentTable = tabsMap.get(ASSIGNMENTS_TABLE_NAME).getTable();
    JTable reportTable = tabsMap.get(REPORTS_TABLE_NAME).getTable();
    JTable archiveAssignTable = tabsMap.get(ARCHIVE_TABLE_NAME).getTable();

    /**
     * CONSTRUCTOR
     */
    public void loadTables() {
        
        /**
         * This code looks like it can be refactored
         * one method loadTable(JTable t)
         * I have to now make individual changes for every method
         * rather than just make changes once in one method
         */
        
        loadAssignmentTable();
        //ana.setTerminalsFunction(ana.getassignmentTable());
        ana.setTerminalsFunction(assignmentTable);

        loadReportTable();
        //ana.setTerminalsFunction(ana.getReportTable());
        ana.setTerminalsFunction(reportTable);


        loadArchiveAssignTable();
        //ana.setTerminalsFunction(ana.getArchiveAssignTable());
        ana.setTerminalsFunction(archiveAssignTable);

//        loadViewerTable();
        ana.setLastUpdateTime();
        
        //pass table map back to analyster
        tabsMap.get(ASSIGNMENTS_TABLE_NAME).setTable(assignmentTable);
        tabsMap.get(REPORTS_TABLE_NAME).setTable(reportTable);
        tabsMap.get(ARCHIVE_TABLE_NAME).setTable(archiveAssignTable);
        ana.setTabsMap(tabsMap);
    }

    public void loadAssignmentTable() {

        try {
            connection(ana.sqlQuery(Analyster.getAssignmentsTableName()), assignmentTable);
        } catch (SQLException e) {
            log.error("Error", e);
        }
        ana.setColumnFormat(ana.columnWidthPercentage1, assignmentTable);
        ana.getAssignments().init(assignmentTable, new String[]{"Symbol", "Analyst"});
        ana.setFilterTempAssignment(TableRowFilterSupport.forTable(assignmentTable).actions(true).apply());
        ana.getFilterTempAssignment().getTable();   // create filter when the table is loaded.
        ana.setNumberAssignmentInit(assignmentTable.getRowCount());
        ana.getjActivateRecord().setEnabled(false);
        ana.getjArchiveRecord().setEnabled(true);

        numOfRecords1.setText("Number of records in Assignments: " + assignmentTable.getRowCount());
        numOfRecords2.setText("Number of records shown: " + assignmentTable.getRowCount());
        
        // testing
        tabsMap.get("Assignments").setFilter(ana.getFilterTempAssignment());
        tabsMap.get("Assignments").setTotalRecords(assignmentTable.getRowCount());

    }

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
        ana.setNumberAssignmentInit(assignmentTable.getRowCount());
        ana.getjActivateRecord().setEnabled(false);
        ana.getjArchiveRecord().setEnabled(true);

        numOfRecords1.setText("Number of records in Assignments: " + assignmentTable.getRowCount());
        numOfRecords2.setText("Number of records shown: " + assignmentTable.getRowCount());

        // testing, looks like just filter an number
        tabsMap.get("Assignments").setFilter(filter);
        tabsMap.get("Assignments").setTotalRecords(assignmentTable.getRowCount());
        
        // why is this code here?
        filter.apply(columnIndex, filterCriteria);
        filter.saveTableState();
        filter.saveFilterCriteria(filterCriteria);
        filter.setColumnIndex(columnIndex);

    }

    public void loadArchiveAssignTable() {

        try {
            connection(ana.sqlQuery(Analyster.getArchiveTableName()), archiveAssignTable);
        } catch (SQLException e) {
            log.error("Error", e);
        }
        ana.setColumnFormat(ana.columnWidthPercentage1, archiveAssignTable);
        ana.getArchiveAssign().init(archiveAssignTable, new String[]{"Symbol", "Analyst"});
        ana.setTerminalsFunction(archiveAssignTable);
        ana.setNumberArchiveAssignInit(archiveAssignTable.getRowCount());
        
        // testing
        //ana.tabsMap.get("Assignments_Archived").setTable(archiveAssignTable);
        //ana.tabsMap.get("Assignments_Archived").setFilter(TableRowFilterSupport.forTable(archiveAssignTable).actions(true).apply());
        //ana.tabsMap.get("Assignments_Archived").setFilteredTable(ana.getFilterTempAssignment().getTable());
        ana.tabs.get("Assignments_Archived").setTotalRecords(archiveAssignTable.getRowCount());
    }
    
    
    public void loadArchiveTableWithFilter(int columnIndex, Collection<DistinctColumnItem> filterCriteria) {

        try {
            connection(ana.sqlQuery(Analyster.getArchiveTableName()), archiveAssignTable);
        } catch (SQLException e) {
            log.error("Error", e);
        }
        ana.setColumnFormat(ana.columnWidthPercentage1, archiveAssignTable);
        ana.getAssignments().init(archiveAssignTable, new String[]{"Symbol", "Analyst"});
        ITableFilter<?> filter = TableRowFilterSupport
                .forTable(archiveAssignTable)
                .actions(true)
                .apply();
        ana.setFilterTempArchive(filter);
        ana.getFilterTempAssignment().getTable();   // create filter when the table is loaded.
        ana.setNumberAssignmentInit(archiveAssignTable.getRowCount());
        ana.getjActivateRecord().setEnabled(true);
        ana.getjArchiveRecord().setEnabled(false);

//        numOfRecords1.setText("Number of records in Assignments: " + assignmentTable.getRowCount());
//        numOfRecords2.setText("Number of records shown: " + assignmentTable.getRowCount());

        
        // testing
        //ana.tabsMap.get("Assignments_Archived").setTable(archiveAssignTable);
        ana.tabs.get("Assignments_Archived").setFilter(filter);
        //ana.tabsMap.get("Assignments_Archived").setFilteredTable(ana.getFilterTempAssignment().getTable());
        ana.tabs.get("Assignments_Archived").setTotalRecords(archiveAssignTable.getRowCount());
        
        
        // why is this code here?
        filter.apply(columnIndex, filterCriteria);
        filter.saveTableState();
        filter.saveFilterCriteria(filterCriteria);
        filter.setColumnIndex(columnIndex);

    }
      public void loadReportTable() {
          try {
              connection(ana.sqlQuery(Analyster.getReportsTableName()), reportTable);
          } catch (SQLException e) {
              log.error("Error", e);
          }
          ana.setColumnFormat(ana.columnWidthPercentage2, reportTable);
        ana.getReports().init(reportTable, new String[]{"Symbol", "Author"});
        ana.setNumberReportsInit(reportTable.getRowCount());
        
        
        // testing
        //ana.tabsMap.get("Reports").setTable(reportTable);
        //ana.tabsMap.get("Reports").setFilter(TableRowFilterSupport.forTable(reportTable).actions(true).apply());
        //ana.tabsMap.get("Reports").setFilteredTable(ana.getFilterTempAssignment().getTable());
        ana.tabs.get("Reports").setTotalRecords(reportTable.getRowCount());
    }

    public void loadReportTableWithFilter(int columnIndex, Collection<DistinctColumnItem> filterCriteria) {

        try {
            connection(ana.sqlQuery(Analyster.getReportsTableName()), reportTable);
        } catch (SQLException e) {
            log.error("Error", e);
        }
        ana.setColumnFormat(ana.columnWidthPercentage2, reportTable);
        ana.getAssignments().init(reportTable, new String[]{"Symbol", "Author"});
        ITableFilter<?> filter = TableRowFilterSupport
                .forTable(reportTable)
                .actions(true)
                .apply();
        ana.setFilterTempReport(filter);
        ana.getFilterTempReport().getTable();   // create filter when the table is loaded.
        ana.setNumberAssignmentInit(reportTable.getRowCount());
        ana.getjActivateRecord().setEnabled(false);
        ana.getjArchiveRecord().setEnabled(true);

//        numOfRecords1.setText("Number of records in Assignments: " + assignmentTable.getRowCount());
//        numOfRecords2.setText("Number of records shown: " + assignmentTable.getRowCount());

        filter.apply(columnIndex, filterCriteria);
        filter.saveTableState();
        filter.saveFilterCriteria(filterCriteria);
        filter.setColumnIndex(columnIndex);
        
        // testing
        //ana.tabsMap.get("Reports").setTable(reportTable);
        ana.tabs.get("Reports").setFilter(filter);
        //ana.tabsMap.get("Reports").setFilteredTable(ana.getFilterTempAssignment().getTable());
        ana.tabs.get("Reports").setTotalRecords(reportTable.getRowCount());
    }
//    public void loadViewerTable(){
//        try {
//            connection(ana.sqlQuery(Analyster.getAssignmentsTableName()), viewerTable);
//        } catch (SQLException e) {
//            log.error("Error", e);
//        }
//        ana.setColumnFormat(ana.columnWidthPercentage1, viewerTable);
//        ana.getViewer().init(viewerTable, new String[]{"Symbol", "Analyst"});
//        ana.setFilterTempAssignment(TableRowFilterSupport.forTable(viewerTable).actions(true).apply());
//        ana.getFilterTempAssignment().getTable();   // create filter when the table is loaded.
//        Analyster.setNumberAssignmentInit(viewerTable.getRowCount());
//        ana.getjActivateRecord().setEnabled(false);
//        ana.getjArchiveRecord().setEnabled(true);
//    }
  

}
