package com.elle.analyster.presentation.filter;

import com.elle.analyster.MyTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.Serializable;
import java.util.Collection;

public interface ITableFilter<T extends JTable> extends Serializable {

    /**
     * The table under filter
     *
     * @return
     */
    T getTable();

    /**
     * @param column model column index
     * @return
     */
    Collection<DistinctColumnItem> getDistinctColumnItems(int column);

    /**
     * @param column model column index
     * @return
     */
    Collection<DistinctColumnItem> getFilterState(int column);

    /**
     * Checks if column is filtered
     *
     * @param column model column index
     * @return true if column is filtered
     */
    boolean isFiltered(int column);

    boolean includeRow(Row entry);

    /**
     * Apply filter for specified column based on collection of distinct items
     *
     * @param col
     * @param items
     * @return
     */
    boolean apply(int col, Collection<DistinctColumnItem> items);

    boolean apply(int col, Object selectField);

    void addChangeListener(IFilterChangeListener listener);

    void removeChangeListener(IFilterChangeListener listener);

    /**
     * Clear the filter
     */
    void clear();

    /**
     * Describes what filter has to do when table model changes
     *
     * @param model
     */
    void modelChanged(TableModel model);

    /**
     * Save temporary table form Filter table.
     */
    void saveTableState();

    /**
     * Get table filtered previous state
     *
     * @return
     */
    MyTableModel getTableModelPreviousState();

    /**
     *  Save Collection of filter criteria
     * @return
     */
    void saveFilterCriteria(Collection collection);

    /**
     * Return Collection of filter criteria
     * @return
     */
    Collection<DistinctColumnItem> getFilterCriteria();

    int getColumnIndex();

    void setColumnIndex(int mcolumnIndex);

    TableModel getMyTableModelInitial();

    public interface Row {
        int getValueCount();

        Object getValue(int column);
    }


}