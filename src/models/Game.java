/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 * @author User
 */
public class Game implements Comparable<Game>{
    private final long ID;
    private int priority;
    private Gamer J1;
    private Gamer J2;

    /**
    *@param
    */
    public Game(String id1, String id2, int priority) {
        ID = System.currentTimeMillis();
        J1 = new Gamer(id1);
        J2 = new Gamer(id2);
        this.priority = priority;
    }

    public Game(long ID, String id1, String id2, int priority) {
        this.ID = ID;
        J1 = new Gamer(id1);
        J2 = new Gamer(id2);
        this.priority = priority;
    }
    
    public Game(long ID, String id1, String id2, long score1, long score2, int priority) {
        this.ID = ID;
        J1 = new Gamer(id1);
        J2 = new Gamer(id2);
        J1.setScore(score1);
        J2.setScore(score2);
        this.priority = priority;
    }
    
    public long getId() { return ID; }
    public Gamer getJ1() { return J1; }
    public Gamer getJ2() { return J2; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setScoreJ1(int score) { J1.setScore(score); }
    public void setScoreJ2(int score) { J2.setScore(score); }
    public void setScores(int s1, int s2) {
        setScoreJ1(s1);
        setScoreJ2(s2);
    }
    
    public String getWinner(){ return (J1.getScore() > J2.getScore())? J1.getPseudo() : J2.getPseudo(); }
    
    @Override
    public int compareTo(Game o) {
        return this.getPriority() - o.priority;
    }
}
