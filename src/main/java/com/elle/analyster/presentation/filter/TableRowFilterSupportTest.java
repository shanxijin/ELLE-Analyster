package com.elle.analyster.presentation.filter;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
//This class it was design to assign table data to filter./////

public final class TableRowFilterSupportTest {
    private IObjectToStringTranslator translator;
    private final ITableFilter<?> filter;
    private boolean actionsVisible = true;
    private int filterIconPlacement = SwingConstants.LEADING;
    private boolean useTableRenderers = false;

    /**
     * 
     * @param filter 
     */
    public TableRowFilterSupportTest( ITableFilter<?> filter ) {
        if ( filter == null ) throw new NullPointerException();
        this.filter = filter;
    }
    
    
    public TableRowFilterSupportTest( JTable table ) {
        if ( table == null ) throw new NullPointerException();
        this.filter = new JTableFilter(table);
    }

    /**
     * this method does not make sense, the constructor should just be called
     * */
    public static TableRowFilterSupportTest forTable( JTable table ) {
        return new TableRowFilterSupportTest(new JTableFilter(table));
    }

    /**
     * this method does not make sense, the constructor should just be called
     * */
    public static TableRowFilterSupportTest forFilter( ITableFilter<?> filter ) {
        return new TableRowFilterSupportTest(filter);
    }

    /**
     * Additional actions visible in column filter list
     * @param visible
     * @return
     */
    public TableRowFilterSupportTest actions( boolean visible ) {
        this.actionsVisible = visible;
        return this;
    }

    /**
     * Set the placement of the filter icon with respect to the existing icon
     * in the column label.
     *
     * @param filterIconPlacement either SwingConstants.LEADING or
     *         SwingConstants.TRAILING.
     * @return this
     */
    public TableRowFilterSupportTest filterIconPlacement(int filterIconPlacement) {
        if (filterIconPlacement != SwingConstants.LEADING &&
                filterIconPlacement != SwingConstants.TRAILING) {
            throw new UnsupportedOperationException("The filter icon " +         
                    "placement can only take the values of " +                   
                    "SwingConstants.LEADING or SwingConstants.TRAILING");
        }
        this.filterIconPlacement = filterIconPlacement;
        return this;
    }


    public TableRowFilterSupportTest searchTransalator( IObjectToStringTranslator translator ) {
        this.translator = translator;
        return this;
    }

    public TableRowFilterSupportTest useTableRenderers( boolean value ) {
        this.useTableRenderers = value;
        return this;
    }

    public ITableFilter<?> apply() {

        final TableFilterColumnPopup filterPopup = new TableFilterColumnPopup(filter);
        filterPopup.setEnabled(true);
        filterPopup.setActionsVisible(actionsVisible);
        filterPopup.setSearchTranslator(translator);
        filterPopup.setUseTableRenderers( useTableRenderers );

        setupTableHeader();
        
        return filter;
    }


    private void setupTableHeader() {

        final JTable table = filter.getTable();
        

        filter.addChangeListener(new IFilterChangeListener() {

            @Override
            public void filterChanged(ITableFilter<?> filter) {
                table.getTableHeader().repaint();
                table.getModel().getRowCount();
            }
        });
        

        // make sure that search component is reset after table model changes
        setupHeaderRenderers(table.getModel(), true );

    }

    private void setupHeaderRenderers( TableModel newModel, boolean fullSetup ) {

        JTable table =  filter.getTable();
        

        FilterTableHeaderRenderer headerRenderer =
                new FilterTableHeaderRenderer(filter, filterIconPlacement);
        filter.modelChanged( newModel );

        for( TableColumn c:  Collections.list( table.getColumnModel().getColumns()) ) {
            c.setHeaderRenderer( headerRenderer );
        }

        if ( !fullSetup ) return;

        table.addPropertyChangeListener("model", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                setupHeaderRenderers( (TableModel) e.getNewValue(), false );
            }
            
        });

        

    }


}