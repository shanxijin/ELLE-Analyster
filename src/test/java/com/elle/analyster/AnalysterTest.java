/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author danielabecker
 */
public class AnalysterTest {


    @Test
    public void shouldReturnsqlQuery() {
       Analyster analyster = Analyster.getInstance();
      
       assertEquals("SELECT * FROM Reports ORDER BY symbol ASC", analyster.sqlQuery("Reports"));
       
       assertEquals("SELECT * FROM Assignments ORDER BY symbol ASC", analyster.sqlQuery("Assignments"));
    }

    
}
