/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

import com.elle.analyster.service.TableService;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 * @author danielabecker
 */
public class TableServiceTest {

    private TableService tableService;

    @Before
    public void setUp() {
        tableService = new TableService();
    }

    @Test
    public void shouldReturnColumnNames() {
        JTable tableAssignment = mock(JTable.class);
        when(tableAssignment.getColumnCount()).thenReturn(3);
        when(tableAssignment.getColumnName(1)).thenReturn("Symbol");
        when(tableAssignment.getColumnName(2)).thenReturn("Analyst");
        tableService.setAssignmentTable(tableAssignment);
        tableService.setReportTable(mock(JTable.class));

        String[] columnNames = tableService.getColumnNames(1);

        assertThat(columnNames.length, is(2));
        assertThat(columnNames[0], is("Symbol"));
        assertThat(columnNames[1], is("Analyst"));
    }

    
//    @Test
//    public void shouldReturntextToNumOfRecords1(){
//     
//      assertThat( tableService.textToNumOfRecords(339, "Assignments"), is("Number of records in Assignments: 339"));
//      
//    }
     @Test
    public void shouldReturntextToNumOfRecords2(){
     
      assertThat( tableService.textToNumOfRecords(339), is("Number of records shown: 339"));
      
    }

}
