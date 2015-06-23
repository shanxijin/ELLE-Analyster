package com.elle.analyster.presentation.filter;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
//This class it was design to assign table data to filter./////

public final class TableRowFilterSupportTest {
    
    // class attributes
    private IObjectToStringTranslator translator;
    private final ITableFilter<?> filter;
    private boolean actionsVisible = true;
    private int filterIconPlacement = SwingConstants.LEADING;
    private boolean useTableRenderers = false;


    /**
     * CONSTRUCTOR
     * @param table 
     */
    public TableRowFilterSupportTest( JTable table ) {
        
        // initialize class variables
        translator = new IObjectToStringTranslator() {

            @Override
            public String translate(Object obj) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        filter = new JTableFilter(table);
        actionsVisible = true; // this should start at false and turned on
        filterIconPlacement = SwingConstants.LEADING;
        useTableRenderers = false;
    }

    /**
     * Additional actions visible in column filter list
     * @param visible
     * @return
     */
    public void setActionsVisible( boolean visible ) {
        actionsVisible = visible;
    }

    /**
     * Set the placement of the filter icon with respect to the existing icon
     * in the column label.
     *
     * @param filterIconPlacement either SwingConstants.LEADING or
     *         SwingConstants.TRAILING.
     */
    public void filterIconPlacement(int filterIconPlacement) {
        if (filterIconPlacement != SwingConstants.LEADING &&
                filterIconPlacement != SwingConstants.TRAILING) {
            throw new UnsupportedOperationException("The filter icon " +         
                    "placement can only take the values of " +                   
                    "SwingConstants.LEADING or SwingConstants.TRAILING");
        }
        this.filterIconPlacement = filterIconPlacement;
    }


    public void searchTransalator( IObjectToStringTranslator translator ) {
        this.translator = translator;
    }

    public void useTableRenderers( boolean value ) {
        useTableRenderers = value;
    }

    public void apply() {

        // this should not be called and set here
        TableFilterColumnPopup filterPopup = new TableFilterColumnPopup(filter);
        filterPopup.setEnabled(true);
        filterPopup.setActionsVisible(actionsVisible);
        filterPopup.setSearchTranslator(translator);
        filterPopup.setUseTableRenderers( useTableRenderers );

        // setupTableHeader(); // why call this?
        // I moved this code here
        JTable table = filter.getTable();
        

        filter.addChangeListener(new IFilterChangeListener() {

            @Override
            public void filterChanged(ITableFilter<?> filter) {
                table.getTableHeader().repaint();
                table.getModel().getRowCount();
            }
        });
        
        // this below is not my comment
        // make sure that search component is reset after table model changes
        // setupHeaderRenderers(table.getModel(), true ); // why call this?
        // I copied the code here
        
        //JTable table =  filter.getTable(); this is called too many times
        

        FilterTableHeaderRenderer headerRenderer =
                new FilterTableHeaderRenderer(filter, filterIconPlacement);
        //filter.modelChanged( newModel ); 
        filter.modelChanged( table.getModel() ); // this was a passed param

        for( TableColumn c:  Collections.list( table.getColumnModel().getColumns()) ) {
            c.setHeaderRenderer( headerRenderer );
        }

        //if ( !fullSetup ) return;
        boolean fullSetup = true; // this was a passed param
        
        // this code never makes it to the listener
        if ( !fullSetup ) return; 

        table.addPropertyChangeListener("model", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                
                //setupHeaderRenderers( (TableModel) e.getNewValue(), false );
                filter.modelChanged( (TableModel) e.getNewValue() ); // this was a passed param
                
                boolean fullSetup = false; // this was a passed param

                // this makes no sense either
                // after the listener is called which it never is
                // then it says to call it.
                // it seems backwards
                if ( !fullSetup ) return; 
            }
            
        });
        
        //return filter; // why return filter?
    }

}