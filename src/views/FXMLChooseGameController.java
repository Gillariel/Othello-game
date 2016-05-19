/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import datas.TournamentManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import models.Game;
import utils.Log;
import utils.MyDialog;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLChooseGameController implements Initializable {
    
    private Stage stage;
    
    @FXML
    private ComboBox<Pair<String,String>> comboBoxGames;
    @FXML
    private Button btn_play;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TournamentManager provider = new TournamentManager();
            for(Pair<String,String> p : provider.selectAllVsContenders())
                if(p != null)
                    comboBoxGames.getItems().add(p);
                else
                    if(MyDialog.confirmationDialog("Next Turn", "No games left", "Do you want ti generate the next turn?"))
                        provider.generateNextTurn();
                    else
                        Platform.exit();
    }    

    @FXML
    private void handle_btn_play(ActionEvent event) {
        if(comboBoxGames.getSelectionModel().isEmpty())
            MyDialog.warningDialog("Warning", "You need to chosse one game before playing !");
        else{
            TournamentManager provider = new TournamentManager();
            Game game = provider.selectGame(comboBoxGames.getSelectionModel().getSelectedItem().getKey());
            try{
                FXMLLoader loaderFXML = new FXMLLoader(getClass().getResource("/views/FXMLGame.fxml"));
                Parent root = (Parent) loaderFXML.load();
                FXMLGameController controller = loaderFXML.getController();
                if(saveFileExist(game.getJ1().getPseudo() + "_VS_" + game.getJ2().getPseudo())){
                    if(MyDialog.confirmationDialog("Load", "you are loading an old game.", "A save from an old game is in your directory, do you want to load it?"))
                        controller.load();
                }
                controller.setCurrentGame(game);
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/ressources/ProgressBar.css").toExternalForm());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image("http://swap.sec.net/annex/icon.png"));
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.setFullScreen(true);
                stage.setTitle("Othello - Game");
                setOnCloseRequest(stage, controller);
                controller.setStage(stage);
                stage.show();
                this.stage.close();
            }catch(IOException e) {
                MyDialog.warningDialog("Warning", "Error while loading the game window, please try again.");
            }
        }
    }

    public void setStage(Stage s) { this.stage = s; }
    
    private boolean saveFileExist(String name) {
        File file = new File(name + ".sav");
        return file.exists();
    }
    
    private void setOnCloseRequest(Stage stage, FXMLGameController controller) {
        stage.setOnCloseRequest((WindowEvent e) -> {
            if(controller.getGame().getScore(1) > 2 || controller.getGame().getScore(2) > 2)
                if(MyDialog.confirmationDialog("Save", "The game is not finished!", "Do you want to save it in a specific file?"))
                    controller.save();
        });
    }
}
