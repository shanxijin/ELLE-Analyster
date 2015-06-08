package com.elle.analyster.service;

import com.elle.analyster.GUI;
import com.elle.analyster.domain.ModifiedData;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: danielabecker
 * Date: 6/5/15
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadRecordTest {

    private Statement statement;

    @Before
    public void setUp() throws SQLException {
        statement = mock(Statement.class);
        GUI.setStmt(statement);

        when(statement.executeUpdate(anyString())).thenReturn(0);

    }

    @Test
    public void testUploadRecord() throws Exception {
        JTable tableAssignment = mock(JTable.class);
        List<ModifiedData> modifiedDataList = new ArrayList<>();
        when(tableAssignment.getColumnCount()).thenReturn(7);
        UploadRecord uploadRecord = new UploadRecord();
        ModifiedData modifiedData = new ModifiedData();
        modifiedData.setValueModified("Eric");
        modifiedData.setTableName("Assignments");
        modifiedData.setId(320);
        modifiedData.setColumnIndex(2);
        modifiedDataList.add(modifiedData);


        String uploadedQuedy = uploadRecord.uploadRecord(tableAssignment, modifiedDataList);
        assertThat(uploadedQuedy, notNullValue());

        verify(tableAssignment).getColumnName(2);
        verify(statement).executeUpdate(anyString());

    }

    @Test
    public void testUpdateBatchEdit() throws SQLException {
        JTable tableAssignment = mock(JTable.class);
        List<ModifiedData> modifiedDataList = new ArrayList<>();
        when(tableAssignment.getColumnCount()).thenReturn(7);
        UploadRecord uploadRecord = new UploadRecord();

        ModifiedData modifiedData = new ModifiedData();
        modifiedData.setValueModified("Eric");
        modifiedData.setTableName("Assignments");
        modifiedData.setColumnIndex(2);
        modifiedData.setId(320);
        modifiedDataList.add(modifiedData);
        ModifiedData modifiedData1 = new ModifiedData();
        modifiedData1.setValueModified("Eric");
        modifiedData1.setTableName("Assignments");
        modifiedData1.setColumnIndex(2);
        modifiedData1.setId(420);
        modifiedDataList.add(modifiedData1);
        ModifiedData modifiedData2 = new ModifiedData();
        modifiedData2.setValueModified("Eric");
        modifiedData2.setTableName("Assignments");
        modifiedData2.setColumnIndex(2);
        modifiedData2.setId(466);
        modifiedDataList.add(modifiedData2);


        String uploadedQuedy = uploadRecord.uploadRecord(tableAssignment, modifiedDataList);
        assertThat(uploadedQuedy, notNullValue());

        verify(tableAssignment, times(3)).getColumnName(2);
        verify(statement, times(3)).executeUpdate(anyString());


    }


}
