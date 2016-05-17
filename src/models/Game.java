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
    
    // mémorise  les coordonnées des cases graphiques par rapport à leurs  numéro 
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
	
	
    public Game() { initialiseJeu(); }
	
    /** PREPARE  LES  INSTANCES  DU  JEU */
    public void prepareInstance() {
        
        // met à false le tableau coupsPossibles
	remiseAZeroCoupPossibles();
		
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
	int ligne = 1;
	for (int i = 0; i < 64; i++){
            if (i > 7 ) ligne = 2;
            if (i > 15) ligne = 3;
            if (i > 23) ligne = 4;
            if (i > 31) ligne = 5;
            if (i > 39) ligne = 6;
            if (i > 47) ligne = 7;
            if (i > 55) ligne = 8;
            rowBoxes[i]= ligne;		// un décalage de 1 par rapport à l'interface graphique
        }
		
	int colonne = 1;
	for (int i = 0; i < 64; i++) {
            if (colonne > 8) colonne = 1;
            columnBoxes[i] = colonne;
            colonne++;
	}
			
	// initialise à zéro le tableau des cases pris.
        for (int i = 0; i < 32; i++){
            rowTaken[i] = 0;
            columnTaken[i] = -0;
	}	
    }	
		
    /** INITIALISE  UNE  PARTIE **/	
    public void initialiseJeu() {
    // déclare les cases qui se trouve hors du plateau de jeu
	for (int i = 0; i < 10; i++) {	
            board[i][0] = OUT_OF_BOUNDS;
            board[i][9] = OUT_OF_BOUNDS;
            board[0][i] = OUT_OF_BOUNDS;
            board[9][i] = OUT_OF_BOUNDS;
	}		
		
	// déclare toutes les cases en statut libre
	for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++)
		board[i][j] = EMPTY;
		
	// positionne les 4 pions de départ sur les case 27, 28, 35 et 36
	board[4][4] = WHITE;
	board[4][5] = BLACK;
	board[5][4] = BLACK;
	board[5][5] = WHITE;
				
	// initialise les compteurs de pions.
	whiteCounter = 2;
	blackCounter = 2;
    }
    
    public int joueurEnCours() { return currentPlayer; }
    public int getLigne(int uneCase) { return rowBoxes[uneCase]; }
    public int getColonne(int uneCase) { return columnBoxes[uneCase]; }
    public int getLignePris(int unIndice) { return rowTaken[unIndice]; }
    public int getColonnePris(int unIndice) { return columnTaken[unIndice]; }
    public int getNumeroCase(int ligne, int colonne) { return boxesNumber[ligne][colonne]; }
    public int getScore(int unJoueur) {
        if (unJoueur == 1) return blackCounter;
        else return whiteCounter; 
    }
    public boolean[][] getCoupPossibles() { return possibleHits; }
    public int getCouleurCase(int ligne, int colonne) { return board[ligne][colonne]; }
    public int choixCase() { return choosenBox; }
    public int getCaseJouer() { return playedBox; }
    public int getRowPlayed() { return rowPlayed; }
    public int getColumnPlayed() { return columnPlayed; }
    public void setRowPlayed(int x) { this.rowPlayed = x; }
    public void setColumnPlayed(int y) { this.columnPlayed = y; }
    public void setCaseJouer(int newOne) { this.playedBox = newOne; }
    public int getBlackCounter() { return blackCounter; }
    public int getWhiteCounter() { return whiteCounter; }
    public void setCurrentPlayer(int player) { this.currentPlayer = player; }
    public void setSpecificBox(int row, int column, int value) { this.board[row][column] = value; }
    public int getCurrentPlayer() { return currentPlayer; }
    
    // véfirie si le placement est correcte
    public boolean placementCorrect(int ligne, int colonne, int couleur, int autreCouleur, boolean joueLeCoup) {	
	int i, j;
	int nbEtape;
	boolean correct = false;
	// si la case est libre
	if (board[ligne][colonne] == EMPTY) {
            int indice = 0;
            for (int a = -1; a < 2; a++)		// permet de savoir dans qu'elle direction on va
		for (int b = -1; b < 2; b++) {	// par rapport à la case choisie
                    nbEtape = 0;				// indique le nombre de pions adverses trouvés
                    do {		
			nbEtape++;
			i = ligne + nbEtape*a;
			j = colonne + nbEtape*b;
                    } 
                    
                    // permet de voir s'il y a un autre pion
                    // d'une autre couleur
                    while ( (i > 0 ) && (i < 9) && (j > 0) && (j < 9) && (board[i][j] == autreCouleur));  
			// si au moins un pion d'une autre couleur est trouvé 
			// et qu'on se trouve dans le plateau 
			// et qu'un pion de même couleur est trouvé 
			if (( i > 0 ) && (i < 9)  && (j > 0) && (j < 9) && (nbEtape > 1) && (board[i][j] == couleur)) {
                            correct = true;
                            // mémorise les coordonnées des pions pris
                            for (int k = 1; k < nbEtape; k++) {
				testRowTaken[indice]= ligne + a*k;
				testColumnTaken[indice] = colonne + b*k;
				indice++;
                            }			
                            
                            // si le coup est vraiment joué, on exécute cette boucle
                            if (joueLeCoup) {	
				for (int m = 1; m < nbEtape; m++) {
                                    // enregistre les cases prises
                                    rowTaken[indice]= ligne + a*m;
                                    columnTaken[indice] = colonne + b*m;
                                    indice++;
                                    // modifie la couleur des pions pris
                                    if (couleur == BLACK)
                                        board[ligne + a*m][colonne + b*m] = TAKE_BY_BLACK;
                                    else if (couleur == WHITE ) 
                                        board[ligne + a*m][colonne + b*m] = TAKE_BY_WHITE;
                                }
                                // la case choisie prend la couleur du joueur en cours
                                board[ligne][colonne] = couleur;		
                            }
			}
		}
	}
	return correct;
    }	
	
    // change le joueur en cours
    public void changeTourJoueur() {
	if (currentPlayer == 1) currentPlayer = 2;
	else currentPlayer = 1;
    }
	
    // remet à 0 le tableau des cases prises
    public void reinitialiseCasesPrises() {
	for (int i = 0; i < 32; i++) {
            rowTaken[i] = 0;
            columnTaken[i] = 0;
	}	
    }
	
    // vérifie si la partie est terminée
    public boolean partieEstFinie() {
        boolean partieFinie = true;	
	for (int y = 1; y < 9; y++ )
            for (int z = 1; z < 9; z++ )
		if ((placementCorrect(y, z, WHITE, BLACK, false)) || (placementCorrect(y, z, BLACK, WHITE, false)))
                    partieFinie = false;
	return partieFinie;
    }
	
    // vérifie si le joueur blanc ne peut plus jouer
    public boolean jbPeutPlusJouer() {
	boolean jbPeutPlusJouer = true;
	for (int y = 1; y < 9; y++ )
            for (int z = 1; z < 9; z++ )		
		if (placementCorrect(y, z, WHITE, BLACK, false))
                    jbPeutPlusJouer = false;			
	return jbPeutPlusJouer;
    }
	
    // vérifie si le joueur noir ne peut plus jouer
    public boolean jnPeutPlusJouer() {
	boolean jnPeutPlusJouer = true;
        for (int y = 1; y < 9; y++ )
            for (int z = 1; z < 9; z++ )		
                if (placementCorrect(y, z, BLACK, WHITE, false))
                    jnPeutPlusJouer = false;			
	return jnPeutPlusJouer;
    }
	
    // permet de montrer les coups possibles à jouer
    public void coupsPossibles() {
	// pour le joueur noir
	if (joueurEnCours() == 1) {
            for (int i = 1 ; i < 9; i++)
		for (int j = 1; j < 9 ; j++)
                    if (placementCorrect(i, j, BLACK, WHITE, false))
			possibleHits[i][j] = true;
        }
		
	// pour le joueur blanc
	else if (joueurEnCours() == 2) {
            for (int m = 1 ; m < 9; m++)
                for (int n = 1; n < 9 ; n++)
                    if (placementCorrect(m, n, WHITE, BLACK, false))
			possibleHits[m][n] = true;
	}
    }
	
    // remise à 0 tableau coupPossibles
    public void remiseAZeroCoupPossibles() {
	for (int i = 1 ; i < 9; i++)
            for (int j = 1; j < 9 ; j++)
		possibleHits[i][j] = false;
    }	
	
    // calcul du nombre de pions de chaque joueur
    public void calculScore() {
	whiteCounter = 0;
	blackCounter = 0;	
	for (int d = 1; d < 9; d++)
            for (int f = 1; f < 9; f++) {
		if (board[d][f] == BLACK || board[d][f] == TAKE_BY_BLACK)
                    blackCounter++;
		if (board[d][f] == WHITE || board[d][f] == TAKE_BY_WHITE)
                    whiteCounter++;
            }
    }
    
    // créer une copie du jeu 
    public Game copier() {
        // crée un nouveau modèle
	Game mod = new Game();
		
	// récupère le statut des  cases
	for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++)
		mod.gameStatus[i][j] = board[i][j];				
	// récupère le joueurs en cours
	mod.currentPlayer = joueurEnCours();
	// récupère les points
	mod.whiteCounter = whiteCounter;
	mod.blackCounter = blackCounter;
	// récupère la case joué
	mod.playedBox = playedBox;	
	return mod;
    }
		
    public void sauvegardeJeux(String nomSauvegarde) {
	int compteur = 0;
	try  {
            FileOutputStream fluxSortieFichier = new FileOutputStream(nomSauvegarde + ".sav");
            ObjectOutputStream fluxSortieObjet= new ObjectOutputStream(fluxSortieFichier);
            try{
		fluxSortieObjet.writeObject(this);
		fluxSortieObjet.flush();		
            } finally {
                try {
                    fluxSortieObjet.close();
		} finally {
                    fluxSortieFichier.close();
                }
            }
	} catch(IOException ioe) {
            ioe.printStackTrace();
	}
    }
	
    public Game chargerUnePartie(String nomFichier) {
        int compteur = 0;
	try {
            FileInputStream fluxEntreeFichier = new FileInputStream(nomFichier+".sav");
            ObjectInputStream fluxEntreeObjet= new ObjectInputStream(fluxEntreeFichier);
            try {		
		// désérialisation : lecture de l'objet depuis le flux d'entrée
                 return (Game)fluxEntreeObjet.readObject();
            } finally {
                // on ferme les flux
		try {
                    fluxEntreeObjet.close();
		} finally {
                    fluxEntreeFichier.close();
		}
            }
	} catch(IOException ioe) {
            ioe.printStackTrace();
	} catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
	}
        return null;
    }	
			
    
    public void jouerUnCoup(int unCase) {			
	int row = getLigne(unCase);		// lit la ligne de la case choisie
	int column = getColonne(unCase);	// lit la colonne  de la case choisie
	int color = 0;							// couleur du joueur en cours
	int otherColor = 0;	

	// récupère la couleur du joueur en cours et celle de l'autre joueur
	if (joueurEnCours() == 1) {
            color = BLACK;
            otherColor = WHITE;
	}else if(joueurEnCours() == 2) {
            color = WHITE;
            otherColor = BLACK; 
	}

	// si le coup est correct
	if (placementCorrect(row, column, color, otherColor, true)) {		
            // fin du tour du joueur : changement de joueur
            changeTourJoueur();
            // remet à zéro le tableau des cases pris.
            reinitialiseCasesPrises();
            // calcul du score 
            calculScore();
            // si un des 2 joueurs ne peut jouer on passe le tour 
            if ((whiteCounter + blackCounter) < 64) {
		if (jbPeutPlusJouer() == true) currentPlayer = 1;
		if (jnPeutPlusJouer() == true) currentPlayer = 2;
            } 
	}
    }
    
    public void randomFirstPlayer() {
        boolean random = (Math.random()*100) >= 50;
        if(random)
            currentPlayer = WHITE;
        else
            currentPlayer = BLACK;
    }
}
