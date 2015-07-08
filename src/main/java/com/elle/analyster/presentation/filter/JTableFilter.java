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

public class JTableFilter {

    private static final long serialVersionUID = 1L;
    private final TableRowFilter filter = new TableRowFilter();
    private TableModel tableModelPreviousState;
    private int columnIndex = -1;
    private final TableModel  myTableModelInitial;
    private Collection <DistinctColumnItem> itemChecked;
    
    // Moved from AbstractTableFilter class
    private final Set<IFilterChangeListener> listeners = Collections.synchronizedSet(new HashSet<IFilterChangeListener>());

    private final Map<Integer, Collection<DistinctColumnItem>> distinctItemCache
            = Collections.synchronizedMap(new HashMap<Integer, Collection<DistinctColumnItem>>());

    private JTable table = new JTable(); // this was Type T
    private TableFilterState filterState = new TableFilterState();
    
    // constructor
    public JTableFilter(JTable table) {
        //super(table);
        this.table = table; // this was abs constructor
        setupDistinctItemCacheRefresh(); // this was abs constructor
        myTableModelInitial  = copyTableModel(table);
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

    //@Override
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


    //@Override
    public MyTableModel getTableModelPreviousState() {
        return (MyTableModel)tableModelPreviousState;
    }

    //Override
    public void saveFilterCriteria(Collection collection) {
             itemChecked = collection;
    }

    //@Override
    public Collection<DistinctColumnItem> getFilterCriteria() {
        return itemChecked;
    }

    //@Override
    public void modelChanged(TableModel model) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);
        getTable().setRowSorter(sorter);
    }

    //@Override
    public void setColumnIndex(int mColumnIndex) {
        columnIndex = mColumnIndex;

    }

    //@Override
    public int getColumnIndex() {
        return columnIndex;
    }
    
    //@Override
    public TableModel getMyTableModelInitial() {
        return myTableModelInitial;
    }
    
    
    /**************************************************************************
     ******************* Moved from AbstractFilterClass ***********************
     **************************************************************************/

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

    private void clearDistinctItemCache() {
        distinctItemCache.clear();
    }
    
    public void setFilterState(int column, Collection<DistinctColumnItem> values) {
    filterState.setValues(column, values);
    }

    public final void fireFilterChange() {
        for (IFilterChangeListener l : listeners) {
            l.filterChanged((JTableFilter) this); 
        }
    }
    
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
    
    //@Override
    public JTable getTable() {
        return table;
    }

    //@Override
    public boolean apply(int col, Collection<DistinctColumnItem> items) {
        setFilterState(col, items); 
        boolean result = false;
        if (result = execute(col, items)) {
            fireFilterChange();
        }
        return result;
    }

    //@Override
    public boolean apply(int col, Object selectField) { //Create Collection from selected fields 
        Collection<DistinctColumnItem> item = new ArrayList<>();
        
        // handle null exceptions
        if(selectField == null) selectField = "";
        
        DistinctColumnItem distinctColumnItem =new DistinctColumnItem(selectField, col);
        item.add(distinctColumnItem);
        return apply(col, item);
    }
    //@Override
    public final void addChangeListener(IFilterChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    //@Override
    public final void removeChangeListener(IFilterChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    //@Override
    public Collection<DistinctColumnItem> getDistinctColumnItems(int column) {
        Collection<DistinctColumnItem> result = distinctItemCache.get(column);
        if (result == null) {
            result = collectDistinctColumnItems(column);
            distinctItemCache.put(column, result);
        }
        return result;

    }

    //@Override
    public Collection<DistinctColumnItem> getFilterState(int column) {
        return filterState.getValues(column);
    }

    //@Override
    public boolean isFiltered(int column) {
        Collection<DistinctColumnItem> checks = getFilterState(column);
        return !(CollectionUtils.isEmpty(checks)) && getDistinctColumnItems(column).size() != checks.size();
    }

    //@Override
    public boolean includeRow(JTableFilter.Row row) {
        return filterState.include(row);
    }

    //@Override
    public void clear() {
        filterState.clear();
        Collection<DistinctColumnItem> items = Collections.emptyList();
        for (int column = 0; column < table.getModel().getColumnCount(); column++) {
            execute(column, items);
        }
        fireFilterChange();
    }
    
    /**************************************************************************
     ******************* End Moved from AbstractFilterClass *******************
     **************************************************************************/

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
    
    public interface Row {
        
        int getValueCount();

        Object getValue(int column);
    }

}
