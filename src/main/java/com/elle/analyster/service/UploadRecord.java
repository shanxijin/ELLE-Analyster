package com.elle.analyster.service;

import com.elle.analyster.GUI;
import com.elle.analyster.domain.ModifiedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: DanielaBecker Date: 5/28/15 Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadRecord {

    private final Logger log = LoggerFactory.getLogger(UploadRecord.class);
    private GUI gui = new GUI();

    public String uploadRecord(JTable table, List<ModifiedData> modifiedDataList) throws SQLException {
        int id, col;
        Object value;
        String sqlChange = null;

        for (ModifiedData modifiedData : modifiedDataList) {
            String tableName = modifiedData.getTableName();
            id = modifiedData.getId();
            col = modifiedData.getColumnIndex();
            value = modifiedData.getValueModified();
            try {

                if ("".equals(value)) {
                    value = null;
                    sqlChange = "UPDATE " + tableName + " SET " + table.getColumnName(col)
                            + " = " + value + " WHERE ID = " + id + ";";
                } else {
                    sqlChange = "UPDATE " + tableName + " SET " + table.getColumnName(col)
                            + " = '" + value + "' WHERE ID = " + id + ";";
                }
                log.info(sqlChange);
                gui.getStmt().executeUpdate(sqlChange);
                table.setAutoCreateRowSorter(true);

            } catch (SQLException ex) {
                log.error("Error: ", ex);
                throw ex;
            }
        }
        return sqlChange;
    }
}
