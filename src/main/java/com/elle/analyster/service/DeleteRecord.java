/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.service;

import com.elle.analyster.Analyster;
import com.elle.analyster.GUI;
import com.elle.analyster.LoadTables;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * @author Daniela.
 */
//Delete from DataBase record selected
public class DeleteRecord {
    public LoadTables loadTables =new LoadTables();
    Analyster ana = Analyster.getInstance();

    public void deleteRecordSelected( JTable table) throws HeadlessException {
        String tableName = table.getName();
        int rowsSelected = table.getSelectedRows().length;
        if (rowsSelected != -1) {
            for (int i = 0; i < rowsSelected; i++) {
                int row = table.getSelectedRows()[i];
                Integer selectedTask = (Integer) table.getValueAt(row, 0); // Add Note to selected taskID
                String sqlDelete = "DELETE FROM " + GUI.getDatabase() + "." + tableName + " where ID=" + selectedTask;
                try {
                    GUI.getStmt().executeUpdate(sqlDelete);
                    ana.logwind.sendMessages(sqlDelete);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
            }
            if (tableName.equals(ana.getAssignmentsTableName())) {
                new LoadTables().loadAssignmentTable();
            } else if (tableName.equals(ana.getReportsTableName())) {
                new LoadTables().loadReportTable();
            } else {
                new LoadTables().loadArchiveAssignTable();
            }
            JOptionPane.showMessageDialog(null, rowsSelected + " Record(s) Deleted");
        }
    }
}
