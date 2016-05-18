/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author User
 */
public class Game implements Serializable {
    // statut des cases
    public final int BLACK = 1;
    public final int WHITE = 2;
    public final int TAKE_BY_WHITE = 3;
    public final int TAKE_BY_BLACK = 4;
    public final int EMPTY = 0;
    public final int OUT_OF_BOUNDS = -1;
    public final int NB_ROWS = 8;
    public final int NB_COLUMNS = 8;
    public final int NB_BOX = 64;
    
    private Game currentGame = this;
    // plateau de 10x10
    private int[][] board = new int[10][10];
	
    // mémorise les coups possibles à jouer
    private boolean[][] possibleHits = new boolean[10][10];
	
    // mémorise le numéro des cases graphiques par rapport aux coordonnées
    private int[][] boxesNumber = new int[10][10];
    
    // mémorise les coordonnées des cases graphiques par rapport à leurs  numéro 
    private int[] rowBoxes = new int[64];
    private int[] columnBoxes = new int[64];
    
    private int rowPlayed;
    private int columnPlayed;
    private int choosenBox;  		// récupère grâce au clic du joueur sur une case
    private int playedBox;
	
    // compteurs des pions présents sur le plateau
    private int blackCounter = 0;
    private int whiteCounter = 0;
	
    // mémorise les coordonnées des cases prises
    private int rowTaken[] = new int[32];
    private int columnTaken[] = new int[32];
    private int testRowTaken[] = new int[32];
    private int testColumnTaken[] = new int[32];
		
    // 1 : Joueur noir  ;  2 : Joueur blanc
    private int currentPlayer = 1; 
    private int choosenCurrentPlayer = 1;
    	
    // statut du plateau en cours
    private int gameStatus[][] = new int[10][10];
	
	
    public Game() { initializeGame(); }
	
    /** PREPARE  LES  INSTANCES  DU  JEU */
    public void prepareInstance() {
        
	resetPossibleHits();
		
        // met à 0 la liste des modèles, le joueur en cours et met à 0 le compteur de modèle
	currentPlayer = choosenCurrentPlayer;
		
        // enregistre les numéros des cases graphique par rapport aux coordonnées
	int cpt = 0;
        for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++) {
		boxesNumber[i][j] = cpt;
		cpt++;
            }
		
	// enregistre les lignes et colonnes par rapport aux coordonnées des cases.
	int row = 1;
	for (int i = 0; i < 64; i++){
            if (i > 7 ) row = 2;
            if (i > 15) row = 3;
            if (i > 23) row = 4;
            if (i > 31) row = 5;
            if (i > 39) row = 6;
            if (i > 47) row = 7;
            if (i > 55) row = 8;
            rowBoxes[i]= row;
        }

        int column = 1;
        for (int i = 0; i < 64; i++) {
            if (column > 8) column = 1;
            columnBoxes[i] = column;
            column++;
	}
	
        for (int i = 0; i < 32; i++){
            rowTaken[i] = 0;
            columnTaken[i] = 0;
	}	
    }	
		
    /** INITIALISE  UNE  PARTIE **/	
    public void initializeGame() {
        for (int i = 0; i < 10; i++) {	
            board[i][0] = OUT_OF_BOUNDS;
            board[i][9] = OUT_OF_BOUNDS;
            board[0][i] = OUT_OF_BOUNDS;
            board[9][i] = OUT_OF_BOUNDS;
        }		
        
	for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++)
		board[i][j] = EMPTY;
		
        board[4][4] = WHITE;
        board[4][5] = BLACK;
        board[5][4] = BLACK;
        board[5][5] = WHITE;

        whiteCounter = 2;
        blackCounter = 2;
    }
    
    public int getRow(int boxNumber) { return rowBoxes[boxNumber]; }
    public int getColumn(int boxNumber) { return columnBoxes[boxNumber]; }
    public int getRowTaken(int index) { return rowTaken[index]; }
    public int getColumnTaken(int index) { return columnTaken[index]; }
    public int getBoxNumber(int row, int column) { return boxesNumber[row][column]; }
    public boolean[][] getPossibleHits() { return possibleHits; }
    public int getBoxColor(int row, int column) { return board[row][column]; }
    public int getPlayedBox() { return playedBox; }
    public int getRowPlayed() { return rowPlayed; }
    public int getColumnPlayed() { return columnPlayed; }
    public int getCurrentPlayer() { return currentPlayer; }
    public int getScore(int playerNumber) { return (playerNumber == 1)? blackCounter : whiteCounter;  }
    public void setRowPlayed(int x) { this.rowPlayed = x; }
    public void setColumnPlayed(int y) { this.columnPlayed = y; }
    public void setPlayedBox(int newOne) { this.playedBox = newOne; }
    public void setCurrentPlayer(int player) { this.currentPlayer = player; }
    public void setSpecificBox(int row, int column, int value) { this.board[row][column] = value; }

    
    // véfirie si le placement est correcte
    public boolean isCorrectPosition(int row, int column, int color, int otherColor, boolean hitNeedToBePlay) {	
	int i, j;
        //step represent the number of pawn taken in the same range
	int step;
	boolean result = false;
	if (board[row][column] == EMPTY) {
            int index = 0;
            for (int k = -1; k < 2; k++){	
		for (int l = -1; l < 2; l++) {
                    step = 0;
                    do {		
			step++;
			i = row + (step * k);
			j = column + (step * l);
                    }while ((i > 0 ) && (i < 9) && (j > 0) && (j < 9) && (board[i][j] == otherColor));  
                        //test if we're in the board and if there is a pawn of the same color
                    if (( i > 0 ) && (i < 9)  && (j > 0) && (j < 9) && (step > 1) && (board[i][j] == color)) {
                        result = true;
                        for (int m = 1; m < step; m++) {
                            testRowTaken[index]= row + m*m;
                            testColumnTaken[index] = column + l*m;
                            index++;
                        }			
                            
                        //execute the hit if it's said
                        if (hitNeedToBePlay) {
                            for (int n = 1; n < step; n++) {
                                // enregistre les cases prises
                                rowTaken[index] = row + (k * n);
                                columnTaken[index] = column + (l * n);
                                index++;
                                //change the color of the taken's pawns
                                board[row + (k * n)][column + (l * n)] = (color == BLACK) ? TAKE_BY_BLACK : TAKE_BY_WHITE;
                                /*    board[row + k*n][column + l*n] = ;
                                else if (color == WHITE ) 
                                    board[row + k*n][column + l*n] = ;
                                }*/
                                board[row][column] = color;		
                            }
			}
                    }
		}
            }
	}
	return result;
    }	
	
    public void switchPlayer() { currentPlayer = (currentPlayer == 1)? 2 : 1; }
	
    public void resetTakenBoxes() {
	for (int i = 0; i < 32; i++) {
            rowTaken[i] = 0;
            columnTaken[i] = 0;
	}	
    }
	
    public boolean isFinished() {
        boolean partieFinie = true;	
	for (int i = 1; i < 9; i++ )
            for (int j = 1; j < 9; j++ )
		if ((isCorrectPosition(i, j, WHITE, BLACK, false)) || (isCorrectPosition(i, j, BLACK, WHITE, false)))
                    partieFinie = false;
	return partieFinie;
    }
	
    public boolean IsWhiteUnableToPlay() {
	boolean result = true;
	for (int i = 1; i < 9; i++ )
            for (int j = 1; j < 9; j++ )		
		if (isCorrectPosition(i, j, WHITE, BLACK, false))
                    result = false;			
	return result;
    }
	
    public boolean IsBlackUnableToPlay() {
	boolean result = true;
        for (int i = 1; i < 9; i++ )
            for (int j = 1; j < 9; j++ )		
                if (isCorrectPosition(i, j, BLACK, WHITE, false))
                    result = false;			
	return result;
    }
	
    public void calcPossibleHits() {
        if (getCurrentPlayer()== 1) {
            for (int i = 1 ; i < 9; i++)
                for (int j = 1; j < 9 ; j++)
                    if (isCorrectPosition(i, j, BLACK, WHITE, false))
                        possibleHits[i][j] = true;
        }
        else if (getCurrentPlayer() == 2) {
            for (int i = 1 ; i < 9; i++)
                for (int j = 1; j < 9 ; j++)
                    if (isCorrectPosition(i, j, WHITE, BLACK, false))
                        possibleHits[i][j] = true;
        }
    }
    
    public void resetPossibleHits() {
        for (int i = 1 ; i < 9; i++)
            for (int j = 1; j < 9 ; j++)
                possibleHits[i][j] = false;
    }
	
    public void calcScore() {
        whiteCounter = 0;
        blackCounter = 0;	
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
		if (board[i][j] == BLACK || board[i][j] == TAKE_BY_BLACK)
                    blackCounter++;
                if (board[i][j] == WHITE || board[i][j] == TAKE_BY_WHITE)
                    whiteCounter++;
            }
        }
    }
    
    public void save(String name) {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(name + ".sav"))) {
            stream.writeObject(this);
            stream.flush();
        }catch(IOException e) { e.printStackTrace(); }
    }
	
    public Game load(String name) {
	try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(name + ".sav"))) {
            return (Game)stream.readObject();
	}catch(IOException e) { e.printStackTrace(); }
        catch(ClassNotFoundException e2) { e2.printStackTrace(); }
        return null;
    }	

    public void play(int box) {			
        int row = getRow(box);
        int column = getColumn(box);
        int color = 0, otherColor = 0;	
        
        if (getCurrentPlayer() == 1) {
            color = BLACK;
            otherColor = WHITE;
        }else if(getCurrentPlayer() == 2) {
            color = WHITE;
            otherColor = BLACK; 
        }
        
        if (isCorrectPosition(row, column, color, otherColor, true)) {		
            switchPlayer();
            resetTakenBoxes(); 
            calcScore();
             
            if ((whiteCounter + blackCounter) < 64) {
		if (IsWhiteUnableToPlay() == true) currentPlayer = 1;
		if (IsBlackUnableToPlay() == true) currentPlayer = 2;
            } 
        }
    }
}
