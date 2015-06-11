package com.elle.analyster.presentation.filter;

import com.elle.analyster.MyTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.io.Serializable;
import java.util.Collection;

public class JTableFilter extends AbstractTableFilter<JTable> {

    private static final long serialVersionUID = 1L;
    private final TableRowFilter filter = new TableRowFilter();
    private TableModel tableModelPreviousState;
    private int columnIndex = -1;
    private final TableModel  myTableModelInitial;
    private Collection <DistinctColumnItem> itemChecked;
    public JTableFilter(JTable table) {
        super(table);
        myTableModelInitial  = copyTableModel(table);
    }


    @Override
    protected boolean execute(int col, Collection<DistinctColumnItem> items) {

        RowSorter<?> rs = getTable().getRowSorter();

        if (!(rs instanceof DefaultRowSorter)) {
            return false;
        }

        DefaultRowSorter<?, ?> drs = (DefaultRowSorter<?, ?>) rs;

        @SuppressWarnings("unchecked")
        RowFilter<Object, Object> prevFilter = (RowFilter<Object, Object>) drs.getRowFilter();
        if (!(prevFilter instanceof TableRowFilter)) {
            filter.setParentFilter(prevFilter);
        }

        drs.setRowFilter(filter);
        return true;

    }

    @Override
    public void saveTableState() {
        JTable table = this.getTable();
        tableModelPreviousState = copyTableModel(table);
    }

    private TableModel copyTableModel(JTable table) {
        Object[] columnNames = new Object[table.getColumnCount()];

        TableModel tableModelOriginal = table.getModel();
        for (int i = 0; i < table.getColumnCount(); i++) {
            columnNames[i] = tableModelOriginal.getColumnName(i);
        }
        Object[][] rowData = new Object[table.getRowCount()][table.getColumnCount()];
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                rowData[i][j] = table.getValueAt(i, j);
            }
        }
        return new MyTableModel(rowData, columnNames, true);
    }


    @Override
    public MyTableModel getTableModelPreviousState() {
        return (MyTableModel)tableModelPreviousState;
    }

    @Override
    public void saveFilterCriteria(Collection collection) {
             itemChecked = collection;
    }

    @Override
    public Collection<DistinctColumnItem> getFilterCriteria() {
        return itemChecked;
    }

    @Override
    public void modelChanged(TableModel model) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);
        getTable().setRowSorter(sorter);
    }

    @Override
    public void setColumnIndex(int mColumnIndex) {
        columnIndex = mColumnIndex;

    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }
    @Override
    public TableModel getMyTableModelInitial() {
        return myTableModelInitial;
    }

    class TableRowFilter extends RowFilter<Object, Object> implements Serializable {

        private static final long serialVersionUID = 1L;
        private RowFilter<Object, Object> parentFilter;

        public RowFilter<Object, Object> getParentFilter() {
            return parentFilter;
        }

        public void setParentFilter(RowFilter<Object, Object> parentFilter) {
            this.parentFilter = (parentFilter == null || parentFilter == this) ? null : parentFilter;
        }

        @Override
        public boolean include(final Entry<? extends Object, ? extends Object> entry) {

            // use parent filter condition
            if (parentFilter != null && !parentFilter.include(entry)) {
                return false;
            }

            return includeRow(new ITableFilter.Row() {

                @Override
                public Object getValue(int column) {
                    return entry.getValue(column);
                }

                @Override
                public int getValueCount() {
                    return entry.getValueCount();
                }

            });
        }
    }

}
