/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello_game;

import java.io.File;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.MyDialog;
import views.FXMLChooseGameController;
import views.FXMLGameController;

/**
 *
 * @author User
 */
public class Othello_Game_FXMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loaderFXML = new FXMLLoader(getClass().getResource("/views/FXMLChooseGame.fxml"));
        Parent root = (Parent) loaderFXML.load();
        FXMLChooseGameController controller = loaderFXML.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/ressources/ProgressBar.css").toExternalForm());
        stage.setScene(scene);
        stage.getIcons().add(new Image("http://swap.sec.net/annex/icon.png"));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Othello - Game");
        
        /*stage.setOnShowing((WindowEvent event) -> {
            if(controllerFileExists("game"))
                if(MyDialog.confirmationDialog("Load", "blablabla", "A save from an old game is in your directory, do you want to load it?")) {
                    controller.load();
                    controller.updateFromFile();
                }
        });
        
        stage.setOnCloseRequest((WindowEvent event) -> {
            if(controller.getGame().getBlackCounter() > 2 || controller.getGame().getWhiteCounter() > 2)
                if(MyDialog.confirmationDialog("Save", "Game not finished!", "Do you want to save the game?"))
                    controller.save();
        });*/
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private boolean controllerFileExists(String name){
        File file = new File(name + ".sav");
        return file.exists();
    }
}
