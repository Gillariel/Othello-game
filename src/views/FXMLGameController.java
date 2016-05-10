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
import javafx.scene.Node;
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
import javafx.scene.shape.Circle;
import models.Game;
import models.GameController;
import utils.AppInfo;
import utils.MyDialog;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLGameController implements Initializable {

    private Game game;
    private GameController controller;
    private final ImageView[] imagesFromGrid = new ImageView[64];;
    
    private SimpleDoubleProperty blackScore = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty whiteScore = new SimpleDoubleProperty(0);
    
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
    private Label labelCurrentPlayer;
    @FXML
    private Label labelBlackScore;
    @FXML
    private Label labelWhiteScore;
    
    /**
     * Initializes the controller class.
     */
    
    // Rgb color for Background pics : (10,230,65)
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game = new Game();
        controller = new  GameController();
        game.prepareInstance();
        game.initialiseJeu();
        
        blackPawnProgressBar.setPrefWidth(150); blackPawnProgressBar.setMaxHeight(8);
        whitePawnProgressBar.setPrefWidth(150); whitePawnProgressBar.setMaxHeight(8);
        blackPawnProgressBar.progressProperty().bind(blackScore.divide(64));
        whitePawnProgressBar.progressProperty().bind(whiteScore.divide(64));
        labelBlackScore.textProperty().bind(blackScore.asString());
        labelWhiteScore.textProperty().bind(whiteScore.asString());
        
        
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i == 3 && j == 3 || i == 4 && j == 4){
                    ImageView img = new ImageView(iconWhite);
                    imagesFromGrid[i*j] = img;
                    gridPane.add(new HBox(img), j, i);
                }
                else if(i == 3 && j == 4 || i == 4 && j == 3){
                    ImageView img = new ImageView(iconBlack);
                    imagesFromGrid[i*j] = img;
                    gridPane.add(new HBox(img), j, i);
                }
                else {
                    ImageView img = new ImageView(iconEmpty);
                    if(i == 0) imagesFromGrid[j] = img;
                    if(j == 0) imagesFromGrid[i] = img;
                    if(i == 0 && j == 0) imagesFromGrid[0] = img; 
                    gridPane.add(new HBox(img), j, i);
                }
            }
        }
        
        /*showPossiblesHits();
        showCurrentPlayer();
        showScore();*/
        
        for(Node n : gridPane.getChildren())
            n.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                game.setCaseJouer(GridPane.getRowIndex(n) + GridPane.getColumnIndex(n));
                PlayHit();
        });
        initializeBoard();
    }

    /**In-Game Methods */
    public void initializeBoard() {		
        for (int i = 0; i < 64; i++)
            imagesFromGrid[i].setImage(iconEmpty);
	
        imagesFromGrid[27].setImage(iconWhite);
	imagesFromGrid[28].setImage(iconBlack);
	imagesFromGrid[35].setImage(iconBlack);
	imagesFromGrid[36].setImage(iconWhite);
        
	// récupère et affiche les coups possibles
	game.coupsPossibles();
	for (int j = 1; j <  9; j++)
            for (int k = 1; k < 9; k++)
                if (game.getCoupPossibles()[j][k] == true)
			imagesFromGrid[game.getNumeroCase(j,k)].setImage(iconWhite);
    }
    
    public void showPossiblesHits() {
        game.coupsPossibles();
        for(int i = 1; i < 9; i++) 
            for(int j = 1; j < 9 ; j++)        
                if(game.getCoupPossibles()[i][j])
                    imagesFromGrid[i+j].setImage(iconWhite);
    }
    
    public void showCurrentPlayer() {
        labelCurrentPlayer.setText((game.joueurEnCours() == 1)? "Black" : "White");
	MyDialog.dialogWithoutHeader("Turn Change", "Player " + labelCurrentPlayer.getText() + ", it's your turn !");
    }
    
    public void showScore() {
        blackScore.set(game.getScore(1));
        whiteScore.set(game.getScore(2));
    }
    
    public void update() {
        if(!game.partieEstFinie()){
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    switch(game.getCouleurCase(i, j)){
                        case 0 : 
                            imagesFromGrid[i*j].setImage(iconEmpty);
                            break;
                        case 1 :
                            imagesFromGrid[i*j].setImage(iconBlack);
                            break;
                        case 2 :
                            imagesFromGrid[i*j].setImage(iconWhite);
                            break;
                    }
                }    
            }
            showCurrentPlayer();
            showPossiblesHits();
            showScore();
        }
    }
    
    public void PlayHit() {			
	if (game.joueurEnCours() == 1)
            game.setCaseJouer(game.choixCase());
			
	int row = game.getLigne(game.getCaseJouer());		// lit la ligne de la case choisie
	int column = game.getColonne(game.getCaseJouer());	// lit la colonne  de la case choisie
	int color;							// couleur du joueur en cours
	int otherColor;	
			// récupère la couleur et le texte du joueur en cours et celle de l'autre joueur
	if (game.joueurEnCours() == 1) {
            color = game.BLACK;
            otherColor = game.WHITE;
            //txt = "NOIR";
        }else{
            color = game.WHITE;
            otherColor = game.BLACK;
            //txt = "BLANC";
        }
		
	// si le coup est correct
	if (game.placementCorrect(row, column, color, otherColor, true) == true) {		
            // change les couleurs en interface graphique
            switchPawnsColor();
            
            if (game.joueurEnCours() == game.BLACK) imagesFromGrid[game.getCaseJouer()].setImage(iconBlack);
            if (game.joueurEnCours() == game.WHITE) imagesFromGrid[game.getCaseJouer()].setImage(iconWhite);
	
            // fin du tour du joueur : changement de joueur
            game.changeTourJoueur();
	
            // remet à zéro le tableau des cases pris.
            game.reinitialiseCasesPrises();
			
            // calcul du score 
            game.calculScore();
			
            // affiche le score
            showScore();
			
            // si un des 2 joueurs ne peut jouer on passe son tour 
            if ((game.getWhiteCounter() + game.getBlackCounter()) < game.NB_BOX) {
                if (game.jbPeutPlusJouer() == true) game.setCurrentPlayer(game.BLACK);
		if (game.jnPeutPlusJouer() == true) game.setCurrentPlayer(game.WHITE);
            }

            // réinitialise le tableau des coups possibles
            game.remiseAZeroCoupPossibles();
			
            // récupère les cases possibles à jouer
            game.coupsPossibles();
			
            // affiche les cases jouables sur l'interface
            showPossiblesHits();
			
            // affiche en bas de l'écran le joueur en cours
            showCurrentPlayer();
			
            // termine la partie si l'une des conditions est vraie.
            if ((game.partieEstFinie()== true) || (game.getWhiteCounter() + game.getBlackCounter() == game.NB_BOX) || (game.getWhiteCounter() == 0) || (game.getBlackCounter() == 0)) 
		endGame();
            			
			
            // affiche les coordonnées de la case joué sur l'interface
            //afficheCoodCaseJouer();
			
	}
	// si mauvaise case choisie : demande au joueurs de recommencer
	else {
            if ((game.getWhiteCounter() + game.getBlackCounter()) < game.NB_BOX) MyDialog.warningDialog("Not Allowed", "Please choose a valid position and try again!");
            else MyDialog.confirmationDialog("End Game", "It's Over!", "Do you want to play the following game?");

            //coordCoupJoue.setText("L: ?  ||  C: ? ");
	}
    }
    
    public void switchPawnsColor() {
	int uneCase = 0;
	for (int i = 1; i < 9; i++)	
            for (int j = 1; j < 9; j++) {
                // remet les cases en couleur normale
		if (game.getCouleurCase(i,j) == game.WHITE) 
                    imagesFromGrid[uneCase].setImage(iconWhite);
                else if (game.getCouleurCase(i,j) == game.BLACK) 
                    imagesFromGrid[uneCase].setImage(iconBlack);
		else if (game.getCouleurCase(i, j) == game.TAKE_BY_BLACK) {
                    imagesFromGrid[uneCase].setImage(iconBlack);
                    game.setSpecificBox(i, j, game.BLACK);
		}else if (game.getCouleurCase(i,j) == game.TAKE_BY_WHITE) {
                    imagesFromGrid[uneCase].setImage(iconWhite);
                    game.setSpecificBox(i, j, game.WHITE);
                }else if (game.getCouleurCase(i, j) == game.EMPTY)
                    imagesFromGrid[uneCase].setImage(iconEmpty);
				
		uneCase++;
            }
    }
    /** End In-Game Methods */
    
    
    /**Game's Manipulation */
    public void newGame() {
	game.prepareInstance();
	game.initialiseJeu();
	initializeBoard();
	blackScore.set(game.getBlackCounter());
	whiteScore.set(game.getWhiteCounter());
	if (game.getCurrentPlayer() == game.BLACK)
            MyDialog.dialogWithoutHeader("Game's starting", "Blacks begins!");
	else if (game.getCurrentPlayer() == game.WHITE)
            MyDialog.dialogWithoutHeader("Game's starting", "Whites begins!");
    }
    
    public void endGame() {
        if (game.getWhiteCounter() > game.getBlackCounter()) {
            MyDialog.dialog("Eng Game","Thank you for playing.","The game is finished : Whites win with " + game.getWhiteCounter() + "!"); 
        }else if (game.getWhiteCounter() < game.getBlackCounter()) {
            MyDialog.dialog("Eng Game","Thank you for playing.","The game is finished : Blacks win with " + game.getBlackCounter() + "!"); 
        }else if (game.getWhiteCounter() == game.getBlackCounter()) {
            MyDialog.dialog("Eng Game","Thank you for playing.","The game is finished : Perfect equals (Whites = " + game.getWhiteCounter() + " & Blacks = " + game.getBlackCounter() + ")!");  
        }
    }
    /**End Game's Manipulation */ 
    
    /** Getters & Setters */
    public Game getGame() {return game; }
    /**End Getters & Setters */
    
    
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
