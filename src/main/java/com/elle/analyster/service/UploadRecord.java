package com.elle.analyster.service;

import com.elle.analyster.Analyster;
import com.elle.analyster.GUI;
import com.elle.analyster.domain.ModifiedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: danielabecker Date: 5/28/15 Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadRecord {

    private Logger log = LoggerFactory.getLogger(UploadRecord.class);

    public void uploadRecord(JTable table, List<ModifiedData> modifiedDataList) {
        Analyster ana = Analyster.getInstance();
        int id, col;
        boolean flag = false;   // if upload successfully, flag turns to true
        Object value;

        for (ModifiedData modifiedData : modifiedDataList) {
            String tableName = modifiedData.getTableName();
            id = modifiedData.getId();
            col = modifiedData.getColumnIndex();
            value = modifiedData.getValueModified();

            try {
                String sqlChange;
                if (value.equals("")) {
                    value = null;
                    sqlChange = "UPDATE " + tableName + " SET " + table.getColumnName(col)
                            + " = " + value + " WHERE ID = " + id + ";";
                } else {
                    sqlChange = "UPDATE " + tableName + " SET " + table.getColumnName(col)
                            + " = '" + value + "' WHERE ID = " + id + ";";
                }
                System.out.println(sqlChange);
                GUI.getStmt().executeUpdate(sqlChange);
                ana.logwind.sendMessages(sqlChange);
                flag = true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Upload failed!");
                ana.getLogwind().sendMessages(ex.getMessage());
                ana.getLogwind().sendMessages(ex.getSQLState() + "\n");
                log.error("Error: ", ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error!");
                log.error("Error: ", ex);
                ana.getLogwind().sendMessages(ex.getMessage());
            }
        }
        if (flag) {
            JOptionPane.showMessageDialog(null, "Edits uploaded!");
            table.setAutoCreateRowSorter(true);
        }
    }
}
