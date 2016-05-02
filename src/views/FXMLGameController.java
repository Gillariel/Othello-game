/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private SimpleDoubleProperty whiteScore;
    
    private final Image iconEmpty = new Image("/ressources/Reversi_Empty.png");
    private final Image iconBlack = new Image("/ressources/Reversi_Black.png");
    private final Image iconWhite = new Image("/ressources/Reversi_White.png");
    
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
    private TableColumn<?/*Contender???*/,/*Integer*/ ?> scoreJoueur1Column;
    @FXML
    private TableColumn<?/*Contender???*/,/*Integer*/ ?> scoreJoueur2Column;
    
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
        
        blackPawnProgressBar.setPrefWidth(150); blackPawnProgressBar.setMaxHeight(8);
        whitePawnProgressBar.setPrefWidth(150); whitePawnProgressBar.setMaxHeight(8);
                
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i == 3 && j == 3 || i == 4 && j == 4)
                    gridPane.add(new HBox(new ImageView(iconBlack)), i, j);
                else if(i == 3 && j == 4 || i == 4 && j == 3)
                    gridPane.add(new HBox(new ImageView(iconWhite)), i, j);
                else
                    gridPane.add(new HBox(new ImageView(iconEmpty)), i, j);
                
                /*getNodeByRowColumnIndex(i, j, gridPane).setOnMouseClicked((MouseEvent event) -> {
                    game.jouerUnCoup(getCurrentX() + getCurrentY());
                });*/
            }
        }
        
        for(Node n : gridPane.getChildren())
            n.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> System.out.println( "Node: " + n + " at " + GridPane.getRowIndex(n) + "/" + GridPane.getColumnIndex(n))); 
        
    }    
    
    public Game getGame() {return game; }
    public int getCurrentX() { return this.currentX; }
    public int getCurrentY() { return this.currentY; }
    public void setCurrentX(int x) { this.currentX = x; }
    public void setCurrentY(int y) { this.currentY = y; }
    
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
    
    private Node getNodeByRowColumnIndex(final int row,final int column,GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for(Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}
