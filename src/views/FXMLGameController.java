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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.OthelloGame;
import models.Game;
import utils.AppInfo;
import utils.MyDialog;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLGameController implements Initializable {
    
    private Game currentGame;
    private Stage stage;
    private OthelloGame game;
    private final ImageView[] imagesFromGrid = new ImageView[64];
    
    private SimpleDoubleProperty blackScore = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty whiteScore = new SimpleDoubleProperty(0);
    
    private final Image iconEmpty = new Image("/ressources/Reversi_Empty.jpg");
    private final Image iconBlack = new Image("/ressources/Reversi_Black.png");
    private final Image iconWhite = new Image("/ressources/Reversi_White.png");
    private final Image iconBlackPossibility = new Image("/ressources/Reversi_BlackOption.png");
    private final Image iconWhitePossibility = new Image("/ressources/Reversi_WhiteOption.png");
    
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
    private ProgressBar blackPawnProgressBar;
    @FXML
    private ProgressBar whitePawnProgressBar;
    @FXML
    private Label labelCurrentPlayer;
    @FXML
    private Label labelBlackScore;
    @FXML
    private Label labelWhiteScore;
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game = new OthelloGame();
        game.initializeGame();
        
        blackPawnProgressBar.setPrefWidth(150); blackPawnProgressBar.setMaxHeight(8);
        whitePawnProgressBar.setPrefWidth(150); whitePawnProgressBar.setMaxHeight(8);
        blackPawnProgressBar.progressProperty().bind(blackScore.divide(64));
        whitePawnProgressBar.progressProperty().bind(whiteScore.divide(64));
        labelBlackScore.textProperty().bind(blackScore.asString());
        labelWhiteScore.textProperty().bind(whiteScore.asString());
        
        labelCurrentPlayer.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        
        int index = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                index = (i * 8) + j;
                if(i == 3 && j == 3 || i == 4 && j == 4){
                    ImageView pic = new ImageView(iconWhite);
                    imagesFromGrid[index] = pic;
                    gridPane.add(new HBox(pic), j, i);
                }
                else if(i == 3 && j == 4 || i == 4 && j == 3){
                    ImageView pic = new ImageView(iconBlack);
                    imagesFromGrid[index] = pic;
                    gridPane.add(new HBox(pic), j, i);
                }
                else {
                    ImageView pic = new ImageView(iconEmpty);
                    if(i == 0) imagesFromGrid[i] = pic;
                    if(j == 0) imagesFromGrid[j] = pic;
                    if(i == 0 && j == 0) imagesFromGrid[0] = pic; 
                    imagesFromGrid[index] = pic;
                    gridPane.add(new HBox(pic), j, i);
                }
            }
        }
        
        showCurrentPlayer();
        showScore();
        
        initializeBoard();
        
        for(Node n : gridPane.getChildren())
            n.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                game.setPlayedBox((GridPane.getRowIndex(n)* 8) +  (GridPane.getColumnIndex(n)));
                PlayHit();                
            });
    }

    /**In-Game Methods */
    private void initializeBoard() {		
        for (int i = 0; i < 64; i++)
            imagesFromGrid[i].setImage(iconEmpty);
	
        imagesFromGrid[27].setImage(iconWhite);
	imagesFromGrid[28].setImage(iconBlack);
	imagesFromGrid[35].setImage(iconBlack);
	imagesFromGrid[36].setImage(iconWhite);
        
	showPossiblesHits();
    }
    
    private void showPossiblesHits() {
        game.calcPossibleHits();
        for(int i = 1; i < 9; i++) 
            for(int j = 1; j < 9; j++)        
                if(game.getPossibleHits()[i][j])
                    if(game.getCurrentPlayer() == 1){
                        imagesFromGrid[game.getBoxNumber(i, j)].setImage(iconBlackPossibility);
                    }else{
                        imagesFromGrid[game.getBoxNumber(i, j)].setImage(iconWhitePossibility);
                    }
    }
    
    private void showCurrentPlayer() {
        labelCurrentPlayer.setText((game.getCurrentPlayer() == 1)? "Black" : "White");
    }
    
    private void showScore() {
        blackScore.set(game.getScore(game.BLACK));
        whiteScore.set(game.getScore(game.WHITE));
    }
    
    private void PlayHit() {		
        //Adapt GUI with the model
        if(game.play()){
            switchPawnsColor();
            showScore();	
            showPossiblesHits();
            showCurrentPlayer();
            if ((game.isFinished()== true) || (getTotalPawnsTaken() == game.NB_BOX) || (getWhiteCounter() == 0) || (getBlackCounter() == 0)) 
                endGame();	
	}
        else {
            if (getTotalPawnsTaken() < game.NB_BOX) MyDialog.warningDialog("Not Allowed", "Please choose a valid position and try again!");
            else {
                endGame();
            }
        }
    }
    
    private void switchPawnsColor() {
	int oneBox = 0;
	for (int i = 1; i < 9; i++)	
            for (int j = 1; j < 9; j++) {
		if (game.getBoxColor(i,j) == game.WHITE) {
                    imagesFromGrid[oneBox].setImage(iconWhite);
                }else if (game.getBoxColor(i,j) == game.BLACK) {
                    imagesFromGrid[oneBox].setImage(iconBlack);
                }else if (game.getBoxColor(i, j) == game.TAKE_BY_BLACK) {
                    game.setSpecificBox(i,j, game.BLACK);
                    imagesFromGrid[oneBox].setImage(iconBlack);
		}else if (game.getBoxColor(i,j) == game.TAKE_BY_WHITE) {
                    game.setSpecificBox(i, j, game.WHITE);
                    imagesFromGrid[oneBox].setImage(iconWhite);
                }else if (game.getBoxColor(i, j) == game.EMPTY)
                    imagesFromGrid[oneBox].setImage(iconEmpty);	
		oneBox++;
            }
    }
    /** End In-Game Methods */
    
    
    /**Game's Manipulation */
    private void newGame() {
        currentGame.setScores(0, 0);
        game.initializeGame();
        initializeBoard();
        blackScore.set(getBlackCounter());
        whiteScore.set(getWhiteCounter());
        if (game.getCurrentPlayer() == game.BLACK)
            MyDialog.dialogWithoutHeader("Game's starting", "Blacks begins!");
        else if (game.getCurrentPlayer() == game.WHITE)
            MyDialog.dialogWithoutHeader("Game's starting", "Whites begins!");
    }
    
    private void endGame() {
        currentGame.setScores(getBlackCounter(), getWhiteCounter());
        TournamentManager provider = new TournamentManager();
        if (getWhiteCounter() > getBlackCounter()) {
            MyDialog.dialog("Eng Game","Thank you for playing.","The game is finished : Whites win with " + getWhiteCounter() + "!");
            provider.updateScore(currentGame);
            launchChooseGame();
        }else if (getWhiteCounter() < getBlackCounter()) {
            MyDialog.dialog("Eng Game","Thank you for playing.","The game is finished : Blacks win with " + getBlackCounter() + "!");
            provider.updateScore(currentGame);
            launchChooseGame();
        }else if (getWhiteCounter() == getBlackCounter()) {
            MyDialog.dialog("Eng Game","Thank you for playing.","The game is finished : Perfect equals (Whites = " + getWhiteCounter() + " & Blacks = " + getBlackCounter() + ")!");
            newGame();
        }
    }
    /**End Game's Manipulation */ 
    
    /** Getters & Setters */
    public OthelloGame getGame() {return game; }
    public void setCurrentGame(Game g) { this.currentGame = g; }
    public int getBlackCounter() { return game.getScore(1); }
    public int getWhiteCounter() { return game.getScore(2); }
    public int getTotalPawnsTaken() { return getBlackCounter() + getWhiteCounter(); }
    public void setStage(Stage s) { this.stage = s; }
    /**End Getters & Setters */
    
    /** Save Management */
    public void save () { game.save(currentGame.getJ1().getPseudo() + "_VS_" + currentGame.getJ2().getPseudo()); }
    public void load() {
        game = game.load(currentGame.getJ1().getPseudo() + "_VS_" + currentGame.getJ2().getPseudo());
        updateFromFile();
    }
    
    private void updateFromFile() {
        switchPawnsColor();
        showCurrentPlayer();
        showPossiblesHits();
        showScore();
    }
    /** End Save Management */
    
    private void launchChooseGame() {
        try{
            FXMLLoader loaderFXML = new FXMLLoader(getClass().getResource("/views/FXMLChooseGame.fxml"));
            Parent root = (Parent) loaderFXML.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/ressources/ProgressBar.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("http://swap.sec.net/annex/icon.png"));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("Othello - Game");
            this.stage.close();
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleSaveGame(ActionEvent event) {
        if(MyDialog.confirmationDialog("Save", "You are saving the game.", "Are you sure you want to save it?"))    
            save();
    }

    @FXML
    private void handleCloseGame(ActionEvent event) {
        if(MyDialog.confirmationDialog("Exit", "You are exiting the game.", "Are you sure you want to quit?"))
            if(getGame().getScore(1) > 2 || getGame().getScore(2) > 2)
                if(MyDialog.confirmationDialog("Save", "The game is not finished!", "Do you want to save it in a specific file?"))
                    save();
            Platform.exit();
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
