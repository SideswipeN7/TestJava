/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StartUpConfig;

import Frame.Admin;
import Frame.Logowanie;
import Frame.User;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author super
 */
public class StartUpConfig extends Application {
    
    @Override
    public void start(Stage primaryStage) throws SQLException {

        Logowanie l = new Logowanie();
         l.setVisible(true);
         
//       User u = new User();
//       u.setVisible(true);
//
//       Admin ad = new Admin();
//        ad.setVisible(true);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
