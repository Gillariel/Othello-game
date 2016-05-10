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

/**
 *
 * @author User
 */
public class Game {
    // statut des cases
    protected int noir = 1;
    protected int blanc = 2;
    protected int prisParBlanc = 3;
    protected int prisParNoir = 4;
    protected int libre = 0;
    protected int horsGrille = -1;
	
    // plateau de 10x10
    protected int casesPlateau[][] = new int[10][10];
	
    // mémorise les coups possibles à jouer
    protected boolean coupPossibles[][] = new boolean[10][10];
	
    // mémorise le numéro des cases graphiques par rapport aux coordonnées
    protected int numeroCases[][] = new int[10][10];
	
    // mémorise  les coordonnées des cases graphiques par rapport à leurs  numéro 
    protected int ligneCases[] = new int[64];
    protected int colonneCases[] = new int[64];
    protected int sauvegardeChoixIA[] = new int[64];
	
    protected int caseChoisie;  		// récupère grâce au clic du joueur sur une case
    protected int caseJouer;
	
    // compteurs des pions présents sur le plateau
    protected int compteurNoir = 0;
    protected int compteurBlanc = 0;
	
    // mémorise les coordonnées des cases prises
    protected int lignePris[]= new int[32];
    protected int colonnePris[] = new int[32];
    protected int lignePrisTest[]= new int[32];
    protected int colonnePrisTest[] = new int[32];
		
    // 1 : Joueur noir  ;  2 : Joueur blanc
    protected int joueurEnCours = 1; 
    protected int joueurEnCoursChoisi = 1;
    
    /** Instances pour l'historique des coups */	
    // liste des modèles
    protected Game listeModele[] = new Game[64];  
    protected int numeroModele = 0;
	
    // statut du plateau en cours
    protected int statutJeu[][] = new int[10][10];

    // points des différents case pour le choix de la case par l'IA
    protected int[][] pointCase;
	
    // instance de la sauvegarde
    protected String nomFichier = "";
    boolean pasDefichier;
	
	
    /** CONSTANTE  EVALUATION  MIN-MAX   */
    final int MINEVAL= -100000;
    final int MAXEVAL= 100000;
	
	
    public Game() { initialiseJeu(); }
	
    /** PREPARE  LES  INSTANCES  DU  JEU */
    public void prepareInstance() {
        
        // met à false le tableau coupsPossibles
	remiseAZeroCoupPossibles();
		
        // met à 0 la liste des modèles, le joueur en cours et met à 0 le compteur de modèle
	for (int i = 0; i < 61; i++)
            listeModele[i]= null;
	joueurEnCours = joueurEnCoursChoisi;
	numeroModele = 0;
		
        // enregistre les numéros des cases graphique par rapport aux coordonnées
	int cpt = 0;
        for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++) {
		numeroCases[i][j] = cpt;
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
            ligneCases[i]= ligne;		// un décalage de 1 par rapport à l'interface graphique
        }
		
	int colonne = 1;
	for (int i = 0; i < 64; i++) {
            if (colonne > 8) colonne = 1;
            colonneCases[i] = colonne;
            colonne++;
	}
			
	// initialise à zéro le tableau des cases pris.
        for (int i = 0; i < 32; i++){
            lignePris[i] = 0;
            colonnePris[i] = -0;
	}	
    }	
		
    /** INITIALISE  UNE  PARTIE **/	
    public void initialiseJeu() {
    // déclare les cases qui se trouve hors du plateau de jeu
	for (int i = 0; i < 10; i++) {	
            casesPlateau[i][0] = horsGrille;
            casesPlateau[i][9] = horsGrille;
            casesPlateau[0][i] = horsGrille;
            casesPlateau[9][i] = horsGrille;
	}		
		
	// déclare toutes les cases en statut libre
	for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++)
		casesPlateau[i][j] = libre;
		
	// positionne les 4 pions de départ sur les case 27, 28, 35 et 36
	casesPlateau[4][4] = blanc;
	casesPlateau[4][5] = noir;
	casesPlateau[5][4] = noir;
	casesPlateau[5][5] = blanc;
				
	// initialise les compteurs de pions.
	compteurBlanc = 2;
	compteurNoir = 2;
    }
    
    public int joueurEnCours() { return joueurEnCours; }
    public int getLigne(int uneCase) { return ligneCases[uneCase]; }
    public int getColonne(int uneCase) { return colonneCases[uneCase]; }
    public int getLignePris(int unIndice) { return lignePris[unIndice]; }
    public  int getColonnePris(int unIndice) { return colonnePris[unIndice]; }
    public int getNumeroCase(int ligne, int colonne) { return numeroCases[ligne][colonne]; }
    public int getScore(int unJoueur) {
        if (unJoueur == 1) return compteurNoir;
        else return compteurBlanc; 
    }		
    public int getCouleurCase(int ligne, int colonne) { return casesPlateau[ligne][colonne]; }
    public int choixCase() { return caseChoisie; }
	
	
    // véfirie si le placement est correcte
    public boolean placementCorrect(int ligne, int colonne, int couleur, int autreCouleur, boolean joueLeCoup) {	
	int i, j;
	int nbEtape;
	boolean correct = false;
	// si la case est libre
	if (casesPlateau[ligne][colonne] == libre) {
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
                    while ( (i > 0 ) && (i < 9) && (j > 0) && (j < 9) && (casesPlateau[i][j] == autreCouleur));  
			// si au moins un pion d'une autre couleur est trouvé 
			// et qu'on se trouve dans le plateau 
			// et qu'un pion de même couleur est trouvé 
			if (( i > 0 ) && (i < 9)  && (j > 0) && (j < 9) && (nbEtape > 1) && (casesPlateau[i][j] == couleur)) {
                            correct = true;
                            // mémorise les coordonnées des pions pris
                            for (int k = 1; k < nbEtape; k++) {
				lignePrisTest[indice]= ligne + a*k;
				colonnePrisTest[indice] = colonne + b*k;
				indice++;
                            }			
                            
                            // si le coup est vraiment joué, on exécute cette boucle
                            if (joueLeCoup) {	
				for (int m = 1; m < nbEtape; m++) {
                                    // enregistre les cases prises
                                    lignePris[indice]= ligne + a*m;
                                    colonnePris[indice] = colonne + b*m;
                                    indice++;
                                    // modifie la couleur des pions pris
                                    if (couleur == noir)
                                        casesPlateau[ligne + a*m][colonne + b*m] = prisParNoir;
                                    else if ( couleur == blanc ) 
                                        casesPlateau[ligne + a*m][colonne + b*m] = prisParBlanc;
                                }
                                // la case choisie prend la couleur du joueur en cours
                                casesPlateau[ligne][colonne] = couleur;		
                            }
			}
		}
	}
	return correct;
    }	
	
    // change le joueur en cours
    public void changeTourJoueur() {
	if (joueurEnCours == 1) joueurEnCours = 2;
	else joueurEnCours = 1;
    }
	
    // remet à 0 le tableau des cases prises
    public void reinitialiseCasesPrises() {
	for (int i = 0; i < 32; i++) {
            lignePris[i] = 0;
            colonnePris[i] = 0;
	}	
    }
	
    // vérifie si la partie est terminée
    public boolean partieEstFinie() {
        boolean partieFinie = true;	
	for (int y = 1; y < 9; y++ )
            for (int z = 1; z < 9; z++ )
		if ((placementCorrect(y, z, blanc, noir, false)) || (placementCorrect(y, z, noir, blanc, false)))
                    partieFinie = false;
	return partieFinie;
    }
	
    // vérifie si le joueur blanc ne peut plus jouer
    public boolean jbPeutPlusJouer() {
	boolean jbPeutPlusJouer = true;
	for (int y = 1; y < 9; y++ )
            for (int z = 1; z < 9; z++ )		
		if (placementCorrect(y, z, blanc, noir, false))
                    jbPeutPlusJouer = false;			
	return jbPeutPlusJouer;
    }
	
    // vérifie si le joueur noir ne peut plus jouer
    public boolean jnPeutPlusJouer() {
	boolean jnPeutPlusJouer = true;
        for (int y = 1; y < 9; y++ )
            for (int z = 1; z < 9; z++ )		
                if (placementCorrect(y, z, noir, blanc, false))
                    jnPeutPlusJouer = false;			
	return jnPeutPlusJouer;
    }
	
    // permet de montrer les coups possibles à jouer
    public void coupsPossibles() {
	// pour le joueur noir
	if (joueurEnCours() == 1) {
            for (int i = 1 ; i < 9; i++)
		for (int j = 1; j < 9 ; j++)
                    if (placementCorrect(i, j, noir, blanc, false))
			coupPossibles[i][j] = true;
        }
		
	// pour le joueur blanc
	else if (joueurEnCours() == 2) {
            for (int m = 1 ; m < 9; m++)
                for (int n = 1; n < 9 ; n++)
                    if (placementCorrect(m, n, blanc, noir, false))
			coupPossibles[m][n] = true;
	}
    }
	
    // remise à 0 tableau coupPossibles
    public void remiseAZeroCoupPossibles() {
	for (int i = 1 ; i < 9; i++)
            for (int j = 1; j < 9 ; j++)
		coupPossibles[i][j] = false;
    }	
	
    // calcul du nombre de pions de chaque joueur
    public void calculScore() {
	compteurBlanc = 0;
	compteurNoir = 0;	
	for (int d = 1; d < 9; d++)
            for (int f = 1; f < 9; f++) {
		if (casesPlateau[d][f] == noir || casesPlateau[d][f] == prisParNoir)
                    compteurNoir++;
		if (casesPlateau[d][f] == blanc || casesPlateau[d][f] == prisParBlanc)
                    compteurBlanc++;
            }
    }
    
    // créer une copie du jeu 
    public Game copier() {
        // crée un nouveau modèle
	Game mod = new Game();
		
	// récupère le statut des  cases
	for (int i = 1; i < 9; i++)
            for (int j = 1; j < 9; j++)
		mod.statutJeu[i][j] = casesPlateau[i][j];				
	// récupère le joueurs en cours
	mod.joueurEnCours = joueurEnCours();
	// récupère les points
	mod.compteurBlanc = compteurBlanc;
	mod.compteurNoir = compteurNoir;
	// récupère la case joué
	mod.caseJouer = caseJouer;	
	return mod;
    }
		
    public void sauvegardeJeux(String nomSauvegarde) {
	int compteur = 0;
	try  {
            FileOutputStream fluxSortieFichier = new FileOutputStream("save/" + nomSauvegarde + ".sav");
            ObjectOutputStream fluxSortieObjet= new ObjectOutputStream(fluxSortieFichier);
            try {
		while (compteur < 61) {
                    fluxSortieObjet.writeObject(listeModele[compteur]); 
                    compteur++;
		}
		fluxSortieObjet.write(numeroModele);
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
	
    public void chargerUnePartie(String nomFichier) {
        int compteur = 0;
	try {
            // ouverture d'un flux d'entrée depuis le fichier "personne.serial"
            FileInputStream fluxEntreeFichier = new FileInputStream("sauvegarde/"+nomFichier+".sav");
            // création d'un "flux objet" avec le flux fichier
            ObjectInputStream fluxEntreeObjet= new ObjectInputStream(fluxEntreeFichier);
            try {		
		// désérialisation : lecture de l'objet depuis le flux d'entrée
		while (compteur < 61) {	
                    listeModele[compteur] = (Game) fluxEntreeObjet.readObject(); 
                    compteur++;
		}
		numeroModele = fluxEntreeObjet.read();
            } finally {
                // on ferme les flux
		try {
                    fluxEntreeObjet.close();
		} finally {
                    fluxEntreeFichier.close();
		}
            }
	} catch(IOException ioe) {
            pasDefichier = true;
	} catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
	}
    }	
			
    
    public void jouerUnCoup(int unCase) {			
	int ligne = getLigne(unCase);		// lit la ligne de la case choisie
	int colonne = getColonne(unCase);	// lit la colonne  de la case choisie
	int couleur = 0;							// couleur du joueur en cours
	int autreCouleur = 0;	

	// récupère la couleur du joueur en cours et celle de l'autre joueur
	if (joueurEnCours() == 1) {
            couleur = noir;
            autreCouleur = blanc;
	}else if(joueurEnCours() == 2) {
            couleur = blanc;
            autreCouleur = noir; 
	}

	// si le coup est correct
	if (placementCorrect(ligne, colonne, couleur, autreCouleur, true)) {		
            // fin du tour du joueur : changement de joueur
            changeTourJoueur();
            // remet à zéro le tableau des cases pris.
            reinitialiseCasesPrises();
            // calcul du score 
            calculScore();
            // si un des 2 joueurs ne peut jouer on passe le tour 
            if ((compteurBlanc + compteurNoir) < 64) {
		if (jbPeutPlusJouer() == true) joueurEnCours = 1;
		if (jnPeutPlusJouer() == true) joueurEnCours = 2;
            } 
	}
    }
    
    public void randomFirstPlayer() {
        boolean random = (Math.random()*100) >= 50;
        if(random)
            joueurEnCours = blanc;
        else
            joueurEnCours = noir;
    }
}
