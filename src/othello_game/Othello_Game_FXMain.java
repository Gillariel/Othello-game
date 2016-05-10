/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import views.FXMLGameController;

/**
 *
 * @author User
 */
public class Othello_Game_FXMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loaderFXML = new FXMLLoader(getClass().getResource("/views/FXMLGame.fxml"));
        Parent root = (Parent) loaderFXML.load();
        FXMLGameController controller = loaderFXML.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/ressources/ProgressBar.css").toExternalForm());
        stage.setScene(scene);
        stage.getIcons().add(new Image("http://swap.sec.net/annex/icon.png"));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setTitle("Othello - Game");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
