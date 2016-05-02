/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author nathan
 */
public class AppInfo {
    
    public static void showRules() { MyDialog.dialog("Règles", "Explication du fonctionnement d'Othello - Admin", getRules()); }
    public static String getRules(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Othello - Admin \n");
        stringBuilder.append("\tEst une application réservée à l'administrateur d'un groupe de joueur du jeu Othello.\n");
        stringBuilder.append("\tElle fonctionne en relation avec l'application Othello - Game (installé automatiquement avec Othello - Admin.\n");
        stringBuilder.append("\tSi celle-ci n'est pas installée ou a été déplacée, vous ne pourrez utiliser les données créées ici.\n\n");
        stringBuilder.append("Othello - Admin propose 3 fonctionnalités principales :\n");
        stringBuilder.append("\t•Une gestion de membre comprenant ajout, modification et suppression de ceux-ci.\n");
        stringBuilder.append("\t•Une interface de création de tournoi sur base des membres inscrit avec génération pseudo-alétoire des rencontres.\n");
        stringBuilder.append("\t•Lancer le programme Othello - Game.\n\n");
        stringBuilder.append("Bon amusement !");
        return stringBuilder.toString();
    }
    
    public static void showLicence() { MyDialog.dialog("Licence", "Licence d'Animator.", getLicence()); }
    public static String getLicence(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Powered by Foerster Nathan and Michiels Severine\n");
        stringBuilder.append("\tAll images are free of using\n");
        stringBuilder.append("\tOthello is free : if you had paid for it, contact me.\n");
        stringBuilder.append("\t@contact : n.foerster@helmo.student.be\n");
        stringBuilder.append("\t                  s.michiels@helmo.student.be\n\n");
        stringBuilder.append("\tApp version : 1.1\n");
        return stringBuilder.toString();
    }
}
