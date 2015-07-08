package com.elle.analyster.presentation.filter;

public interface IFilterChangeListener {

    
    // this is used in AbstractTableFilter & TableRowFilterSupport
    void filterChanged(JTableFilter filter); 
}