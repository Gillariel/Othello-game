/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import models.Game;
import models.GameController;
import utils.AppInfo;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLGameController implements Initializable {

    private Game game;
    private GameController controller;
    
    private int currentX, currentY, currentCase;
    private SimpleDoubleProperty blackScore;
    private Slider blackSlider;
    private SimpleDoubleProperty whiteScore;
    private Slider whiteSlider;
    
    private final ImageView iconEmpty = new ImageView("/ressources/empty_case.jpg");
    private final ImageView iconBlack = new ImageView("/ressources/black_case.png");
    private final ImageView iconWhite = new ImageView("/ressources/white_case.jpg");
    
    @FXML
    private Pane gamePane;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label leaderLaber;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu optionMenu;
    @FXML
    private MenuItem saveGameMenuItem;
    @FXML
    private MenuItem closeGameMenuItem;
    @FXML
    private Menu aboutMenuItem;
    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem RulesMenuItem;
    @FXML
    private Circle blackPawnsCircle;
    @FXML
    private ProgressBar blackPawnProgressBar;
    @FXML
    private Circle whitePawnsCircle;
    @FXML
    private ProgressBar whitePawnProgressBar;
    @FXML
    private TableView<?/*Contenders??*/> scoreTableView;
    @FXML
    private TableColumn<?/*Contender???*/,/*Integer*/ ?> scoreGamer1Column;
    @FXML
    private TableColumn<?/*Contender???*/,/*Integer*/ ?> scoreGamer2Column;
    
    /**
     * Initializes the controller class.
     */
    
    // Rgb color for Background pics : (10,230,65)
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game = new Game();
        controller = new  GameController();
        game.initialiseJeu();
        game.randomFirstPlayer();
        
        for(int i = 0; i < (int)gridPane.getHeight(); i++)
            for(int j = 0; j < (int)gridPane.getWidth(); j++) {
                gridPane.getItems().get(i,j).add(iconEmpty);
                gridPane.getItems().get(i,j).setOnAction((ActionEvent event) -> {
                    game.jouerUnCoup(i+j);
                });
                iconBlack.setOnMouseClicked((MouseEvent event) -> {
                    game.jouerUnCoup(currentCase);
                });
            }
        
        blackSlider = new Slider(0, 64, game.getScore(1));
        whiteSlider = new Slider(0, 64, game.getScore(2));
        
        
    }    
    
    public Game getGame() {return game; } 

    @FXML
    private void handleSaveGame(ActionEvent event) {
    }

    @FXML
    private void handleCloseGame(ActionEvent event) {
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        AppInfo.showLicence();
    }

    @FXML
    private void handleRules(ActionEvent event) {
          AppInfo.showRules();
    }
}
