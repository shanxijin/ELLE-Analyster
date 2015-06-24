package com.elle.analyster.presentation.filter;

import java.math.BigDecimal;

public class DistinctColumnItem implements Comparable<DistinctColumnItem> {

    private final String value;
    private final int row;

    public DistinctColumnItem( String value, int row) {
        this.value = value.toLowerCase();
        this.row = row;
    }

    //@Override
    public String getValue() {
        return value;
    }

    public int getRow() {
    	
        return row;
    }

    @Override
    public String toString() {
        return value == null? "":  value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DistinctColumnItem other = (DistinctColumnItem) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(DistinctColumnItem o) {

        if ( value == null ) {
            return ( o == null || o.value == null )? 0:  -1;
        }
        if ( o == null || o.value == null ) return 1;

        if ( value.getClass() == o.value.getClass() ) {
            if ( value instanceof Comparable) {
                return (value).compareTo(o.value);
            } else {
                return value.toString().compareTo(o.value.toString());
            }
        } else {

            // not sure why they did this but I will cast to object
            if ( (Object)value instanceof Number && (Object)o.value instanceof Number) {
                BigDecimal a = new BigDecimal(value);
                BigDecimal b = new BigDecimal(o.value);
                return a.compareTo(b);
            }

            return value.getClass().getName().compareTo(o.getClass().getName());
        }


    }

}