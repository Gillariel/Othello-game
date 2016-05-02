/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello_game;

import controllers.GameWindowController;
import views.GameWindow;

/**
 *
 * @author User
 */
public class Othello_Game {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameWindowController controller = new GameWindowController();
        GameWindow gameWindow = new GameWindow("Othello Game Manager", controller);
        controller.setView(gameWindow);
        gameWindow.setVisible(true);
        
    }
}
