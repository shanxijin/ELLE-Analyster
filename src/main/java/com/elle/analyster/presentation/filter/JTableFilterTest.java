package com.elle.analyster.presentation.filter;

import com.elle.analyster.MyTableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class JTableFilterTest{
    
    // Abstract class
    private  Set<IFilterChangeListener> listeners = Collections.synchronizedSet(new HashSet<IFilterChangeListener>());

    private  Map<Integer, Collection<DistinctColumnItem>> distinctItemCache
            = Collections.synchronizedMap(new HashMap<Integer, Collection<DistinctColumnItem>>());

    private JTable table;
    private TableFilterState filterState = new TableFilterState();


    // JTable class
    private static final long serialVersionUID = 1L;
    private final TableRowFilter filter = new TableRowFilter();
    private TableModel tableModelPreviousState;
    private int columnIndex = -1;
    private final TableModel  myTableModelInitial;
    private Collection <DistinctColumnItem> itemChecked;
    
    // constructor
    public JTableFilterTest(JTable table) {
        this.table = table;
        myTableModelInitial  = copyTableModel(table);
        setupDistinctItemCacheRefresh(); // from abstact 
    }


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


    
    public MyTableModel getTableModelPreviousState() {
        return (MyTableModel)tableModelPreviousState;
    }

    
    public void saveFilterCriteria(Collection collection) {
             itemChecked = collection;
    }

    
    public Collection<DistinctColumnItem> getFilterCriteria() {
        return itemChecked;
    }

    
    public void modelChanged(TableModel model) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);
        getTable().setRowSorter(sorter);
    }

    
    public void setColumnIndex(int mColumnIndex) {
        columnIndex = mColumnIndex;

    }

    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    
    public TableModel getMyTableModelInitial() {
        return myTableModelInitial;
    }
    
    private void clearDistinctItemCache() {
        distinctItemCache.clear();
    }

    /**
     * IFilterChangeListener is another interface with one method
     */
    public final void fireFilterChange() {
       // for (IFilterChangeListener l : listeners) l.filterChanged(JTableFilterTest.this);
    }
    
    
    public JTable getTable() {
        return table;
    }
    
    
    public Collection<DistinctColumnItem> getDistinctColumnItems(int column) {
        Collection<DistinctColumnItem> result = distinctItemCache.get(column);
        if (result == null) {
            result = collectDistinctColumnItems(column);
            distinctItemCache.put(column, result);
        }
        return result;

    }
    
    private Collection<DistinctColumnItem> collectDistinctColumnItems(int column) {
        Set<DistinctColumnItem> result = new TreeSet<DistinctColumnItem>(); // to collect unique items
        for (int row = 0; row < table.getModel().getRowCount(); row++) {
            Object value = table.getModel().getValueAt(row, column);
            result.add(new DistinctColumnItem(value, row));
        }
        return result;
    }

    
    public Collection<DistinctColumnItem> getFilterState(int column) {
        return filterState.getValues(column);
    }

    
    public boolean isFiltered(int column) {
        Collection<DistinctColumnItem> checks = getFilterState(column);
        return !(CollectionUtils.isEmpty(checks)) && getDistinctColumnItems(column).size() != checks.size();
    }

    
    public boolean includeRow(ITableFilter.Row row) {
        return filterState.include(row);
    }

    public void setFilterState(int column, Collection<DistinctColumnItem> values) {
        filterState.setValues(column, values);
    }

    
    public boolean apply(int col, Collection<DistinctColumnItem> items) {
        setFilterState(col, items); 
        boolean result = false;
        if (result = execute(col, items)) {
            fireFilterChange();
        }
        return result;
    }

    
    public boolean apply(int col, Object selectField) { //Create Collection from selected fields 
        Collection<DistinctColumnItem> item = new ArrayList<>();
        DistinctColumnItem distinctColumnItem =new DistinctColumnItem(selectField, col);
        item.add(distinctColumnItem);
        return apply(col, item);
    }

    
    public final void addChangeListener(IFilterChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    
    public final void removeChangeListener(IFilterChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    
    public void clear() {
        filterState.clear();
        Collection<DistinctColumnItem> items = Collections.emptyList();
        for (int column = 0; column < table.getModel().getColumnCount(); column++) {
            execute(column, items);
        }
        fireFilterChange();
    }
    
    private void setupDistinctItemCacheRefresh() {
        clearDistinctItemCache();
        this.table.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                clearDistinctItemCache();
                TableModel model = (TableModel) e.getNewValue();
                if (model != null) {
                    model.addTableModelListener(new TableModelListener() {

                        @Override
                        public void tableChanged(TableModelEvent e) {
                            clearDistinctItemCache();
                        }
                    });
                }
            }
        });
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
        public boolean include(final RowFilter.Entry<? extends Object, ? extends Object> entry) {

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
