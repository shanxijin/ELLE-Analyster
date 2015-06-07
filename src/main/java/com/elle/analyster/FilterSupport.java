/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

import javax.swing.JTable;

/**
 *
 * @author danielabecker
 */
public class FilterSupport {

    JTable assignmentFiltered;
    JTable reportFiltered;
    
    public void setFilter(JTable table, String name) {
        if (name.equals("Assignments")) {
            assignmentFiltered = table;
        } else {
            reportFiltered = table;
        }

    }

    public JTable getAssignmentFiltered() {
        return assignmentFiltered;
    }

    public JTable getReportFileterd() {
        return reportFiltered;

    }

}
