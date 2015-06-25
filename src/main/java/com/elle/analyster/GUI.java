/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;


import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author Tina
 */
@Component
public class GUI implements ITableNameConstants{

    public static boolean status = false;    // already logged in?
    protected static String db_url;
    protected static String database;
    protected static String username;
    protected static String password;
    protected static Statement stmt;
    protected static Connection con = null;
    protected static boolean isFiltering = false;
    public static boolean filterAssignmentIsActive = false;
    public static boolean filterReportIstActive;
    static boolean filterArchiveIsActive;
    protected static boolean isChangesUploaded =false;
    final JTable temporaryTable = new JTable();
    private static GUI guiInstance;
    
     public static GUI getInstance() {
        if (guiInstance == null) {
            guiInstance = new GUI();
        }
        return guiInstance;
    }

    public static boolean isIsFiltering() {
        return isFiltering;
    }

    public static String getDatabase() {
        return database;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static void setStmt(Statement statement){
        stmt = statement;
    }
   
    public static JTable setTemporaryTable (JTable table){
         final JTable temporaryTable = table;
         return temporaryTable;
    }

    public static void columnFilterStatus(int columnIndex, JTable table) {
        if (columnIndex != -1) {
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setBackground(Color.GREEN);
            cellRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(columnIndex)
                    .setHeaderRenderer(cellRenderer);
            isFiltering = true;

            if (table.getName().equals("Assignments")) {
                filterAssignmentIsActive = true; //Change status to Assignment table
                //displayRecordLabels.setTableName(ASSIGNMENTS_TABLE_NAME);
            } else if (table.getName().equals("Reports")) {
                filterReportIstActive = true; // Change status to Reports table
                //displayRecordLabels.setTableName(REPORTS_TABLE_NAME);
            } else {
                filterArchiveIsActive = true;
                //displayRecordLabels.setTableName(ARCHIVE_TABLE_NAME);
            }
        }

        //displayRecordLabels.setRecordsShown(table.getRowCount());
        //recordsLabel.setText(displayRecordLabels.toString());
        
        //System.out.println(recordsLabel.getText());
    }

    public static void cleanColumnFilterStatus(int columnIndex, JTable table) {
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(new HeaderRenderer(table));
        isFiltering = false;
        
        //displayRecordLabels.setRecordsShown(table.getRowCount());
        //recordsLabel.setText(displayRecordLabels.toString());

    }

    public static void cleanAllColumnFilterStatus(JTable table) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i)
                    .setHeaderRenderer(new HeaderRenderer(table));
            isFiltering = false;
            
        //displayRecordLabels.setRecordsShown(table.getRowCount());
        //recordsLabel.setText(displayRecordLabels.toString());
        }
    }

}
