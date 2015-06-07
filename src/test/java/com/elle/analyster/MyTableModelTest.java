/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author danielabecker
 */
public class MyTableModelTest {

    @Test
    public void shouldisCellEditable() {

        MyTableModel myTableModel = new MyTableModel();
        myTableModel.isCellEditable(0, 1);
        assertEquals(true, myTableModel.isCellEditable(0, 1));
        assertEquals(false, myTableModel.isCellEditable(1, 0));
    }

}
