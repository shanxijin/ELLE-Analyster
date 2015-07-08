package com.elle.analyster.presentation.filter;

import com.elle.analyster.MyTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.Serializable; // removed did not notice any difference
import java.util.Collection;

public interface ITableFilter<T extends JTable> {

//    T getTable();
//
//    Collection<DistinctColumnItem> getDistinctColumnItems(int column);
//
//    Collection<DistinctColumnItem> getFilterState(int column);
//
//    boolean isFiltered(int column);
//
//    boolean includeRow(JTableFilter.Row entry);
//
//    boolean apply(int col, Collection<DistinctColumnItem> items);
//
//    boolean apply(int col, Object selectField);
//
//    void addChangeListener(IFilterChangeListener listener);
//
//    void removeChangeListener(IFilterChangeListener listener);
//
//    void clear();
//
//    void modelChanged(TableModel model);
//
//    void saveTableState();
//
//    MyTableModel getTableModelPreviousState();
//
//    void saveFilterCriteria(Collection collection);
//
//    Collection<DistinctColumnItem> getFilterCriteria();
//
//    int getColumnIndex();
//
//    void setColumnIndex(int mcolumnIndex);
//
//    TableModel getMyTableModelInitial();
}