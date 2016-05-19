/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datas;

import java.util.List;
//import models.Contender;
import models.Game1;
import models.Member;
//import models.Member;

/**
 *
 * @author User
 */
public class DbEntityToObject {
    public static Member ParticipantParser(String[][] entity) {
        //entity : [0] = pseudo, [1] = firstname, [2] = lastname 
        return (entity != null)? new Member(entity[0][0], entity[0][1], entity[0][2]) : null;
    }
    
    public static Member ParticipantParser(String[] entity) {
        //entity : [0] = pseudo, [1] = password, [2] = firstname, [3] = lastname 
        return (entity != null)? new Member(entity[0], entity[1], entity[2]) : null;
    }
    
    
    /*public static Contender ContenderParser(String[][] entity) {
        //entity : [0] = pseudo
        return (entity != null)? new Contender(entity[0][0]): null;
    }
    
    public static Contender ContenderParser(String[] entity) {
        //entity : [0] = pseudo 
        return (entity != null)? new Contender(entity[0]): null;
    }*/

    static Game1 GameParser(String[] entity) {
        return (entity != null)? new Game1(Integer.parseInt(entity[0]), entity[1], entity[2], Integer.parseInt(entity[3])) : null;
    }
}
