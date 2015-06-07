package com.elle.analyster;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: danielabecker
 * Date: 5/25/15
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(Application.class).headless(false).run(args);
        //System.out.println("Enter the main");
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
            //</editor-fold>
        /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Analyster analyster = applicationContext.getBean(Analyster.class);
//                    analyster.setVisible(true);
//                    new LoginWindow(analyster).setVisible(true);
                }
            });
    }

}
