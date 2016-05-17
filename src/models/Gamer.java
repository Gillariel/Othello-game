/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author User
 */
public class Gamer {
    private String pseudo;
    private long score;

    public Gamer(String pseudo) {
        this.pseudo = pseudo;
        this.score = 0;
    }

    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public long getScore() { return score; }
    public void setScore(long score) { this.score = score; }
    
    @Override
    public String toString() { return pseudo; }
}
