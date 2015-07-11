package com.elle.analyster.presentation.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.*;

public class CheckListRenderer extends JCheckBox implements ListCellRenderer, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Border NO_FOCUS_BORDER      = new EmptyBorder(1, 1, 1, 1);
    private static final Border SAFE_NO_FOCUS_BORDER = NO_FOCUS_BORDER; // may change in the feature

    /**
     * Constructs a default renderer object for an item in a list.
     */
    public CheckListRenderer() {
        super();
        setOpaque(true);
        setBorder(getNoFocusBorder());
    }

    private static Border getNoFocusBorder() {
        if (System.getSecurityManager() != null) {
            return SAFE_NO_FOCUS_BORDER;
        } else {
            return NO_FOCUS_BORDER;
        }
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {

            bg = UIManager.getColor("List.dropCellBackground");
            fg = UIManager.getColor("List.dropCellForeground");

            isSelected = true;
        }

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof Icon) {
            setIcon((Icon) value);
            setText("");
        } else {
            setIcon(null);
            setText(getObjectAsText(value));
        }

        setSelected( isChecked(list, index));

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }
        setBorder(border);

        return this;
    }

    protected String getObjectAsText(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    private boolean isChecked(JList list, int index) {

        if (list.getModel() instanceof ICheckListModel<?>) {
            return ((ICheckListModel<?>) list.getModel()).isCheckedIndex(index);
        } else {
            return false;
        }
        
    }

    /**
     * @return true if the background is opaque and differs from the JList's background; false otherwise
     */
    @Override
    public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();
        if (p != null) {
            p = p.getParent();
        }
        // p should now be the JList.
        boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
        return !colorMatch && super.isOpaque();
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        
        if ("text".equals(propertyName) || 
           (("font".equals(propertyName) || "foreground".equals(propertyName)) && 
             oldValue != newValue && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)) {

            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    // Methods below are overridden for performance reasons.

    @Override
    public void validate() {
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void repaint() {
    }

    @Override
    public void revalidate() {
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    @Override
    public void repaint(Rectangle r) {
    }

    @Override
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }
    
    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }
    
    @SuppressWarnings("serial")
    public static class UIResource extends DefaultListCellRenderer implements javax.swing.plaf.UIResource {
    }

}