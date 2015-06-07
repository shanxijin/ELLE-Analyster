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


        uploadRecord.uploadRecord(tableAssignment, modifiedDataList);


        verify(tableAssignment).getColumnName(2);
        verify(statement).executeUpdate(anyString());

    }


}
