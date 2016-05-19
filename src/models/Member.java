/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import utils.SHA256;

/**
 *
 * @author User
 */
public class Member {
    private String pseudo;
    private String password;
    private String firstname;
    private String lastname;

    public Member() {
        pseudo = "";
        firstname = "";
        lastname = "";
    }

    public Member(String pseudo, String firstname, String lastname) {
        this.pseudo = pseudo;
        this.password = "";
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public Member(String pseudo, String password, String firstname, String lastname) {
        this.pseudo = pseudo;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public void cryptPassword() { password = SHA256.encode(password); }
    
    public String getPseudo() { return pseudo; }
    public String getPassword() { return password; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }

    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    
    public void setAll(String pseudo, String password, String firstname, String lastname){
        this.pseudo = pseudo;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
