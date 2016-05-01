/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Color;
import javax.swing.JOptionPane;
import views.GameWindow;

/**
 *
 * @author User
 */
public class GameWindowController {
    private final int boardState[][];
    private String[] gamesList;
    private GameWindow view = null;
    private boolean gameStarted;

    public GameWindowController() {
        boardState = new int[GameWindow.BOARD_SIZE][GameWindow.BOARD_SIZE];
        gamesList = new String[]{"Game 1", "Game 2", "Game 3", "Game 4", "Game 5"};
    }
    
    /**
     * Sets the view associated with this controller.
     * The controller will subsequently initialize the view components as needed.
     * 
     * @param view a reference to the view object
     */
    public void setView(GameWindow view) {
        this.view = view;
        view.setStatusMessage("Select a game to play in the list...");
        view.setStatusColor(new Color(0, 162, 232));
        view.setGamesList(gamesList);
        view.setSelectedGame(0);
        view.setBlackState("Score of Black is : 0");
        view.setWhiteState("Score of White is : 0");
        view.setSquareState(3, 3, GameWindow.SQUARE_WHITE);
        view.setSquareState(4, 4, GameWindow.SQUARE_WHITE);
        view.setSquareState(3, 4, GameWindow.SQUARE_BLACK);
        view.setSquareState(4, 3, GameWindow.SQUARE_BLACK);
    }
    
    /**
     * Method called by the view when a board square is selected.
     * 
     * @param row square's row index in [0, GameWindow.BOARD_SIZE[
     * @param col square's column index in [0, GameWindow.BOARD_SIZE[
     */
    public void squareClicked(int row, int col) {
        System.out.println("Square (" + row + "," + col + ") clicked.");
        if (gameStarted) {
	        // Rotate square state EMPTY/WHITE/BLACK/WHITE_OPTION/BLQCK_OPTION
	        int newState = (boardState[row][col]+1)%(GameWindow.SQUARE_NUMBER_OF_STATES);
	        boardState[row][col] = newState;
	        view.setSquareState(row, col, newState);
        } else {
        	JOptionPane.showMessageDialog(view.getRootPane(),
        		    "You must select a game, before playing.",
        		    "No Game Seleced",
        		    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Method called by the view when the [Start Game] button is clicked
     */
    public void startGame() {
        System.out.println("Start game clicked.");
        gameStarted = true;
        view.setStatusMessage(gamesList[view.getSelectedGame()]+" started - Black plays");
        view.setStatusColor(new Color(0, 158, 11));
        view.setToDisabled(true);
        view.setSquareState(2, 3, GameWindow.SQUARE_BLACK_OPTION);
        view.setSquareState(3, 2, GameWindow.SQUARE_BLACK_OPTION);
        view.setSquareState(4, 5, GameWindow.SQUARE_BLACK_OPTION);
        view.setSquareState(5, 4, GameWindow.SQUARE_BLACK_OPTION);
    }
    
    /**
     * Method called by the view on application exit
     */
    public void exit() {
    	System.out.println("Application exit");
    	System.exit(0);
    }
}

