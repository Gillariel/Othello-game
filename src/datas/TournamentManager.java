/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datas;
 
import helmo.nhpack.NHDatabaseSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import models.Game;
import models.Member;
import utils.MyDialog;

/**
 *
 * @author Sev
 */
public class TournamentManager extends DbConnect {

    public TournamentManager() {super(); }
    
    public List<Game> selectAllGames() {
        List<Game> games = new ArrayList<>();
        try (NHDatabaseSession session = getDb()){
            String[][] result = session.createStatement("SELECT id, leftContender, rightContender, _priority "
                    + "FROM Games "
                    + "WHERE _priority = (SELECT _priority "
                                      + "FROM Games "
                                      + "WHERE leftContender != '?' AND winner IS NULL);")
                .executeQuery();
            for(String[] game : result) 
                games.add(DbEntityToObject.GameParser(game));
            return games;
        }catch (Exception e) {
            return null;
        }
    }
    
    public List<Pair<String, String>> selectAllVsContenders() {
        List<Pair<String,String>> games = new ArrayList<>();
        try (NHDatabaseSession session = getDb()){
            String[][] result = session.createStatement("SELECT leftContender, rightContender "
                    + "FROM Games "
                    + "WHERE winner IS NULL AND _priority = (SELECT Min(_priority) "
                                      + "FROM Games "
                                      + "WHERE winner IS NULL);")
                    .executeQuery();
            for(String[] g : result) 
                games.add(new Pair<String,String>(g[0], g[1]));
            return games;
        }catch (Exception e) {
            return null;
        }
    }
    
    public Game selectGame(String J1) {
        try (NHDatabaseSession session = getDb()){
            String[][] result = session.createStatement("SELECT id, leftContender, rightContender, _priority "
                    + "FROM Games "
                    + "WHERE leftContender = @J1;")
                    .bindParameter("@J1", J1)
                    .executeQuery(); 
            return new Game(Long.parseLong(result[0][0]), result[0][1], result[0][2], Integer.parseInt(result[0][3]));
        }catch (Exception e) {
            return null;
        }
    }
    
    public int updateScore(Game id) {
        try(NHDatabaseSession session = getDb()){
            int result = 0;
                result = session.createStatement("UPDATE Games "
                    + "SET leftContenderScore = @scoreJ1,rightContenderScore = @scoreJ2 "
                    + "WHERE id like @id")
                    .bindParameter("@scoreJ1",id.getJ1().getScore())
                    .bindParameter("@scoreJ2",id.getJ2().getScore())
                    .bindParameter("@id",id.getId())
                    .executeUpdate();
           
            return result;
        }catch(Exception e){
            return -1;
        }
    }
    
    
    public List<Member> selectParticipantsScore() {
        List<Member> list = new ArrayList<>();
            try(NHDatabaseSession session = getDb()){
            String[][] result = session.createStatement("select m.pseudo,sum(g.leftContenderScore) as Total " 
                    + "from Members m " 
                    + "join Games g on g.leftContender = m.pseudo" 
                    + "group by m.pseudo " 
                    + "UNION " 
                    + "select m.pseudo,sum(g.rightContenderScore) " 
                    + "from Members m " 
                    + "join Games g on g.rightContender = m.pseudo " 
                    + "group by m.pseudo " 
                    + "order by sum(leftContenderScore) desc ;")
                    .executeQuery();
            for(String[] participant : result)
                list.add(DbEntityToObject.ParticipantParser(participant));
            return list;
        }catch(Exception e) {
            return null;
        }
    }
    
    public int selectCurrentPriority(){
        try (NHDatabaseSession session = getDb()){
            String[][] result = session.createStatement("SELECT _priority "
                    + "FROM Games "
                    + "WHERE leftContender != '?'")
                    .executeQuery();
            return Integer.parseInt(result[0][0]);
        }catch (Exception e) {
            return -1;
        }
    }
    
    public int generateNextTurn() {
        try (NHDatabaseSession session = getDb()){
            session.openTransaction();
            
            int turn = selectCurrentPriority();
            
            List<Game> futureGames = new ArrayList<>();
            String[][] result = session.createStatement("SELECT id, leftContender, rightContender, _priority "
                    + "FROM Games "
                    + "WHERE _priority = @priority; ")
                    .bindParameter("@priority", turn + 1)
                    .executeQuery();
            for(String[] g : result)
                futureGames.add(new Game(Long.parseLong(g[0]), g[1], g[2], Integer.parseInt(g[3])));
            
            List<Game> lastGames = new ArrayList<>();
            String[][] result2 = session.createStatement("SELECT id, leftContender, rightContender, leftContenderScore, rightContenderScore, _priority "
                    + "FROM Games "
                    + "WHERE _priority = @priority; ")
                    .bindParameter("@priority", turn)
                    .executeQuery();
            for(String[] ga : result){
                lastGames.add(new Game(Long.parseLong(ga[0]), ga[1], ga[2], Long.parseLong(ga[3]), Long.parseLong(ga[4]), Integer.parseInt(ga[5])));
            }
            String currentLeft = "", currentRight = "";
            long id_futureGame = 0;
            int resultFromUpdate = 0;
            
            for(int i = 0; i < lastGames.size(); i++) {
                if(i%2 == 0){
                    currentLeft = lastGames.get(i).getWinner();
                }else{
                    currentRight = lastGames.get(i).getWinner();
                }
                if(currentLeft != "" && currentRight != ""){
                    id_futureGame = futureGames.get(i/2).getId();
                    resultFromUpdate += updateGame(id_futureGame, currentLeft, currentRight);
                    currentLeft = ""; currentRight = "";
                }
            }
            
            if(resultFromUpdate > 0){
                getDb().rollback();
            }else{
                session.commit();
                System.out.println(session.getLastError());
            }
            return resultFromUpdate;
        }catch (Exception e) {
            
            return -1;
        }
    }

    private int updateGame(long id, String currentLeft, String currentRight) {
        try(NHDatabaseSession session = getDb()){
            int result = session.createStatement("UPDATE Games "
                    + "SET leftContender = @J1, rightContender = @J2 "
                    + "WHERE id = @id; ")
                    .bindParameter("@J1", currentLeft)
                    .bindParameter("@J2", currentRight)
                    .bindParameter("@id", id)
                    .executeUpdate();
            return result;
       }catch(Exception e){
           return -1;
       }
    }
}
