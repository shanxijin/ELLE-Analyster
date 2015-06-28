/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;


import com.elle.analyster.service.TableService;
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
public class GUI {

    public boolean status = false;    // already logged in?
    protected String db_url;
    protected String database;
    protected String username;
    protected String password;
    protected Statement stmt;
    protected Connection con = null;
    protected boolean isFiltering = false;
    public boolean filterAssignmentIsActive = false;
    public boolean filterReportIstActive;
    boolean filterArchiveIsActive;
    protected boolean isChangesUploaded =false;
    final JTable temporaryTable = new JTable();
    private GUI guiInstance;
    private Analyster analyster = Analyster.getInstance();
    
     public GUI getInstance() {
        if (guiInstance == null) {
            guiInstance = new GUI();
        }
        return guiInstance;
    }

    public boolean isIsFiltering() {
        return isFiltering;
    }

    public String getDatabase() {
        return database;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement statement){
        stmt = statement;
    }
   
    public JTable setTemporaryTable (JTable table){
         final JTable temporaryTable = table;
         return temporaryTable;
    }

    public void columnFilterStatus(int columnIndex, JTable table) {
        if (columnIndex != -1) {
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setBackground(Color.GREEN);
            cellRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(columnIndex)
                    .setHeaderRenderer(cellRenderer);
            isFiltering = true;

            if (table.getName().equals("Assignments")) {
                filterAssignmentIsActive = true; //Change status to Assignment table
            } else if (table.getName().equals("Reports")) {
                filterReportIstActive = true; // Change status to Reports table
            } else {
                filterArchiveIsActive = true;
            }
        }
        TableService tableService = new TableService();
        
        // set label record information
        analyster.getRecordsLabel().setText(analyster.getTabs().get(table.getName()).getRecordsLabel()); 
    }

    public void cleanColumnFilterStatus(int columnIndex, JTable table) {
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(new HeaderRenderer(table));
        isFiltering = false;
        TableService tableService = new TableService();
        // set label record information
        analyster.getRecordsLabel().setText(analyster.getTabs().get(table.getName()).getRecordsLabel()); 

    }

    public void cleanAllColumnFilterStatus(JTable table) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i)
                    .setHeaderRenderer(new HeaderRenderer(table));
            isFiltering = false;
            TableService tableService = new TableService();
            // set label record information
            analyster.getRecordsLabel().setText(analyster.getTabs().get(table.getName()).getRecordsLabel()); 
        }
    }

}
