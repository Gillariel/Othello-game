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
public class Case {
    private int x, y;
    private boolean isBlack;

    public Case() {
    }
    
    public Case(int x, int y, boolean isBlack) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isIsBlack() {
        return isBlack;
    }

    public void setIsBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }
    
    
}
