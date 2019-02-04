import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markable
 */
public class DataBaseRequest {
    
    private static String user = "root";
    private static String password = "7913194";
    private static String url = "jdbc:mysql://localhost:3306/football_main";
    
    private static Connection connect;
    
    //---------Переменны для sql запросов--------
    private static String sqlTournamentTable = "SELECT name_division, \n" +
"	team_name, \n" +
"       games, \n" +
"       wins,\n" +
"       draws, \n" +
"       losses, \n" +
"       goal_scored, \n" +
"       goal_conceded, \n" +
"       sc_con, \n" +
"       points, \n" +
"       logo \n" +
"FROM football_main.v_tournament_table\n" +
"where id_division = ?;";
    private static String sqlPrevMatches = "SELECT name_division, \n" +
"	   id_division, \n" +
"       id_tour, \n" +
"       team_home, \n" +
"       goal_home, \n" +
"       goal_guest, \n" +
"       team_guest, \n" +
"       date_format(m_date,'%d-%m-%y %H:%i') as m_date, \n" +
"       name_stadium, \n" +
"       staff_name,\n" +
"       logo_home,\n" +
"       logo_guest\n" +
"FROM football_main.v_matches \n" +
"where (to_days(curdate()) - to_days(m_date) ) >= 0 and (to_days(curdate()) - to_days(m_date)) < 8\n" +
"and id_division = ?;";
    //------------------------------------------
    //------Переменные для коннекта-------------
    private static PreparedStatement prTournamentTable;
    private static PreparedStatement prPrevMatches;
    //------------------------------------------
    //------Переменные для вытаскивание результатат
    private static ResultSet rsTournamnetTable;
    private static ResultSet rsPrevMatches;
    //------------------------------------------
    //-----Переменные для массивов объектов-----
    private static ArrayList<TournamentTable> tournamentTable = new ArrayList<TournamentTable>();
    private static ArrayList<PrevMatches> prevMatches = new ArrayList<>();
    //------------------------------------------
    public DataBaseRequest(int id_div) throws SQLException{
        connection(id_div);
    }
    
    private static void connection(int id_div) throws SQLException{
        try {
            connect = DriverManager.getConnection(url, user, password);
            //Вытаскивает турнирную таблицу
            prTournamentTable = connect.prepareCall(sqlTournamentTable);
            prTournamentTable.setInt(1, id_div);
            rsTournamnetTable = prTournamentTable.executeQuery();
            getTournamentTable(rsTournamnetTable);
            //Вытаскивает сыгранные матчи
            prPrevMatches = connect.prepareCall(sqlPrevMatches);
            prPrevMatches.setInt(1, id_div);
            rsPrevMatches = prPrevMatches.executeQuery();
            getPrevMatches(rsPrevMatches);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseRequest.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            connect.close();
            rsTournamnetTable.close();
            rsPrevMatches.close();
        }
    }
    
    private static void getTournamentTable(ResultSet result){
        String queryOutput = "";
        try {
            while(result.next()){
                String nameDivision = result.getString("name_division");
                String teamName = result.getString("team_name");
                int games = result.getInt("games");
                int wins = result.getInt("wins");
                int draws = result.getInt("draws");
                int losses = result.getInt("losses");
                int goals_scored = result.getInt("goals_scored");
                int goals_conceded = result.getInt("goals_conceded");
                int sc_con = result.getInt("sc_con");
                int points = result.getInt("points");
                String logo = result.getString("logo");
                queryOutput += nameDivision + " " + teamName + " " + games  + " " + wins + " "  + draws + " "
                        + losses + " " + goals_scored + " " + goals_conceded + " "
                        + sc_con + " " + points + " " + logo + "\n";
                tournamentTable.add(new TournamentTable(nameDivision, teamName, games, wins, draws, losses, 
                        goals_scored, goals_conceded, sc_con, points, logo));
            }
            System.out.println("DataBaseRequest getTournamentTable(): output query from DB: \n" + queryOutput);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getPrevMatches(ResultSet result){
        String queryOutput = "";
        try {
            while(result.next()){
                String nameDivision = result.getString("name_division");
                int tour = result.getInt("id_tour");
                String teamHome = result.getString("team_home");
                int goalHome = result.getInt("goal_home");
                int goalGuest = result.getInt("goal_guest");
                String teamGuest = result.getString("team_guest");
                String mDate = result.getString("m_date");
                String stadium = result.getString("name_stadium");
                String logoHome = result.getString("logo_home");
                String logoGuest = result.getString("logo_guest");
                queryOutput += nameDivision + " " + tour + " " + teamHome + " " + goalHome + " " +
                        goalGuest + " " + teamGuest + " " + mDate + " " + stadium + " " + logoHome + " " + logoGuest + "\n";
                prevMatches.add(new PrevMatches(nameDivision, tour, teamHome, goalHome, goalGuest, teamGuest, logoHome, logoGuest));
            }
            System.out.println("DataBaseRequest getPrevMatches(): output query  from DB:" + queryOutput);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList<TournamentTable> getTournamentTable() {
        return DataBaseRequest.tournamentTable;
    }
    
   public static ArrayList<PrevMatches> getPrevMatches(){
       return DataBaseRequest.prevMatches;
   }
    
}
