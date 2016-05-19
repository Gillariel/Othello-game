/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import datas.TournamentManager;
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
import javafx.util.Pair;
import models.Game1;
import utils.Log;
import utils.MyDialog;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLChooseGameController implements Initializable {

    private Game1 game;
    
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
                    provider.generateNextTurn();
    }    

    @FXML
    private void handle_btn_play(ActionEvent event) {
        if(comboBoxGames.getSelectionModel().isEmpty()){
            MyDialog.warningDialog("Warning", "You need to chosse one game before playing !");
            try{
                Parent root = FXMLLoader.load(getClass().getResource("/views/FXMLGame.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/ressources/ProgressBar.css").toExternalForm());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image("http://swap.sec.net/annex/icon.png"));
                stage.centerOnScreen();
                stage.setResizable(true);
                stage.setTitle("Othello - Game");
                stage.show();
            }catch(IOException e) {
                MyDialog.warningDialog("Warning", "Error while loading the game window, please try again.");
            }
        
        }else{
            TournamentManager provider = new TournamentManager();
            Game1 game = provider.selectGame(comboBoxGames.getSelectionModel().getSelectedItem().getKey());
            try{
                FXMLLoader loaderFXML = new FXMLLoader(getClass().getResource("/views/FXMLGame.fxml"));
                Parent root = (Parent) loaderFXML.load();
                FXMLGameController controller = loaderFXML.getController();
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
                controller.setStage(stage);
                stage.show();
                this.stage.close();
            }catch(IOException e) {
                MyDialog.warningDialog("Warning", "Error while loading the game window, please try again.");
            }
        }
    }

    public void setStage(Stage s) { this.stage = s; }
}
