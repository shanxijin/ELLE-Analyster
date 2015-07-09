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

/**
 * JTableFilter class
 * @author cigreja
 */
public class JTableFilter {

    private TableRowFilter filter = new TableRowFilter();
    private TableModel tableModelPreviousState;
    private int columnIndex = -1;
    private TableModel  myTableModelInitial;
    private Collection <DistinctColumnItem> itemChecked;
    
    // Moved from AbstractTableFilter class
    private final Set<IFilterChangeListener> listeners = Collections.synchronizedSet(new HashSet<IFilterChangeListener>());

    private final Map<Integer, Collection<DistinctColumnItem>> distinctItemCache
            = Collections.synchronizedMap(new HashMap<Integer, Collection<DistinctColumnItem>>());

    private JTable table = new JTable(); // this was Type T
    private TableFilterState filterState = new TableFilterState();
    
    /**
     * CONSTRUCTOR
     * JTableFilter
     * @param table 
     */
    public JTableFilter(JTable table) {
        this.table = table; // this was abs constructor
        setupDistinctItemCacheRefresh(); // this was abs constructor
        myTableModelInitial  = copyTableModel(table);
    }
    
    /**
     * setupDistinctItemCacheRefresh
     */
    public void setupDistinctItemCacheRefresh() {
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

    /**
     * clearDistinctItemCache
     */
    private void clearDistinctItemCache() {
        distinctItemCache.clear();
    }

    /**
     * execute 
     * @param col
     * @param items
     * @return 
     */
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

    /**
     * saveTableState
     */
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

    /**
     * getTableModelPreviousState
     * @return 
     */
    public MyTableModel getTableModelPreviousState() {
        return (MyTableModel)tableModelPreviousState;
    }

    /**
     * saveFilterCriteria
     * @param collection 
     */
    public void saveFilterCriteria(Collection collection) {
             itemChecked = collection;
    }

    /**
     * getFilterCriteria
     * @return 
     */
    public Collection<DistinctColumnItem> getFilterCriteria() {
        return itemChecked;
    }

    /**
     * modelChanged
     * @param model 
     */
    public void modelChanged(TableModel model) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);
        getTable().setRowSorter(sorter);
    }

    /**
     * setColumnIndex
     * @param mColumnIndex 
     */
    public void setColumnIndex(int mColumnIndex) {
        columnIndex = mColumnIndex;

    }

    /**
     * getColumnIndex
     * @return 
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * getMyTableModelInitial
     * @return 
     */
    public TableModel getMyTableModelInitial() {
        return myTableModelInitial;
    }
    
    /**
     * setFilterState
     * @param column
     * @param values 
     */
    public void setFilterState(int column, Collection<DistinctColumnItem> values) {
    filterState.setValues(column, values);
    }

    /**
     * fireFilterChange
     */
    public final void fireFilterChange() {
        for (IFilterChangeListener l : listeners) {
            l.filterChanged((JTableFilter) this); 
        }
    }
    
    /**
     * collectDistinctColumnItems
     * @param column
     * @return 
     */
    private Collection<DistinctColumnItem> collectDistinctColumnItems(int column) {
        Set<DistinctColumnItem> result = new TreeSet<DistinctColumnItem>(); // to collect unique items
        for (int row = 0; row < table.getModel().getRowCount(); row++) {
            Object value = table.getModel().getValueAt(row, column);
            
            // handle null exception
            if(value == null)value = "";
            
            result.add(new DistinctColumnItem(value, row));
        }
        return result;
    }
    
    /**
     * getTable
     * @return 
     */
    public JTable getTable() {
        return table;
    }

    /**
     * apply
     * @param col
     * @param items
     * @return 
     */
    public boolean apply(int col, Collection<DistinctColumnItem> items) {
        setFilterState(col, items); 
        boolean result = false;
        if (result = execute(col, items)) {
            fireFilterChange();
        }
        return result;
    }

    /**
     * apply
     * @param col
     * @param selectField
     * @return 
     */
    public boolean apply(int col, Object selectField) { //Create Collection from selected fields 
        Collection<DistinctColumnItem> item = new ArrayList<>();
        
        // handle null exceptions
        if(selectField == null) selectField = "";
        
        DistinctColumnItem distinctColumnItem =new DistinctColumnItem(selectField, col);
        item.add(distinctColumnItem);
        return apply(col, item);
    }
    
    /**
     * addChangeListener
     * @param listener 
     */
    public final void addChangeListener(IFilterChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * removeChangeListener
     * @param listener 
     */
    public final void removeChangeListener(IFilterChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * getDistinctColumnItems
     * @param column
     * @return 
     */
    public Collection<DistinctColumnItem> getDistinctColumnItems(int column) {
        Collection<DistinctColumnItem> result = distinctItemCache.get(column);
        if (result == null) {
            result = collectDistinctColumnItems(column);
            distinctItemCache.put(column, result);
        }
        return result;

    }

    /**
     * getFilterState
     * @param column
     * @return 
     */
    public Collection<DistinctColumnItem> getFilterState(int column) {
        return filterState.getValues(column);
    }

    /**
     * isFiltered
     * @param column
     * @return 
     */
    public boolean isFiltered(int column) {
        Collection<DistinctColumnItem> checks = getFilterState(column);
        return !(CollectionUtils.isEmpty(checks)) && getDistinctColumnItems(column).size() != checks.size();
    }

    /**
     * includeRow
     * @param row
     * @return 
     */
    public boolean includeRow(JTableFilter.Row row) {
        return filterState.include(row);
    }

    /**
     * clear
     */
    public void clear() {
        filterState.clear();
        Collection<DistinctColumnItem> items = Collections.emptyList();
        for (int column = 0; column < table.getModel().getColumnCount(); column++) {
            execute(column, items);
        }
        fireFilterChange();
    }
    
    /**
     * NESTED CLASS
     * TableRowFilter
     */
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

            return includeRow(new Row() {

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
    
    /**
     * NESTED INTERFACE 
     * Row
     */
    public interface Row {
        
        int getValueCount();

        Object getValue(int column);
    }

}
