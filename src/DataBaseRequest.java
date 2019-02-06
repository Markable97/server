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
"       goals_scored, \n" +
"       goals_conceded, \n" +
"       sc_con, \n" +
"       points, \n" +
"       logo \n" +
"FROM football_main.v_tournament_table\n" +
"where id_division = ?;";
    private static String sqlPrevMatches = "SELECT name_division, \n" +
"	id_division, \n" +
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
    private static String sqlNextMatches = "SELECT name_division, \n" +
"       id_tour, \n" +
"       team_home, \n" +
"       team_guest, \n" +
"       date_format(m_date,'%d-%m-%y %H:%i') as m_date, \n" +
"       name_stadium, \n" +
"       staff_name\n" +
"FROM football_main.v_matches m\n" +
"where curdate() < m_date and id_division = ?;";
    private static String sqlSquadInfo = "select team_name, \n" +
"	name, \n" +
"       name_amplua,\n" +
"       date_format(birthdate, '%d-%m-%Y') as birthdate,\n" +
"       number,\n" +
"       games,\n" +
"       goal,\n" +
"       penalty,\n" +
"       assist,\n" +
"       yellow_card,\n" +
"       red_card,\n" +
"       photo\n" +
"from v_squad\n" +
"where id_team = ?;";
    //------------------------------------------
    //------Переменные для коннекта-------------
    private static PreparedStatement prTournamentTable;
    private static PreparedStatement prPrevMatches;
    private static PreparedStatement prNextMatches;
    private static PreparedStatement prSquadInfo;
    //------------------------------------------
    //------Переменные для вытаскивание результатат
    private static ResultSet rsTournamnetTable;
    private static ResultSet rsPrevMatches;
    private static ResultSet rsNextMathces;
    private static ResultSet rsSquadInfo;
    //------------------------------------------
    //-----Переменные для массивов объектов-----
    private static ArrayList<TournamentTable> tournamentTable = new ArrayList<TournamentTable>();
    private static ArrayList<PrevMatches> prevMatches = new ArrayList<>();
    private static ArrayList<NextMatches> nextMatches = new ArrayList<>();
    private static ArrayList<Player> squadInfo = new ArrayList<>();
    //------------------------------------------
    public DataBaseRequest(int id) throws SQLException{
        tournamentTable.clear();
        prevMatches.clear();
        nextMatches.clear();
        connection(id);
    }
    
    public DataBaseRequest(String name_team)throws SQLException{
        squadInfo.clear();
        connection(name_team);
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
            //Вытаскиваем будущие матчи
            getPrevMatches(rsPrevMatches);
            prNextMatches = connect.prepareCall(sqlNextMatches);
            prNextMatches.setInt(1, id_div);
            rsNextMathces = prNextMatches.executeQuery();
            getNextMatches(rsNextMathces);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseRequest.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            connect.close();
            rsTournamnetTable.close();
            rsPrevMatches.close();
        }
    }
    
    private static void connection(String name_team){
        try {
            connect = DriverManager.getConnection(url, user, password);
            prSquadInfo = connect.prepareCall(sqlSquadInfo);
            prSquadInfo.setString(1, name_team);
            rsSquadInfo = prSquadInfo.executeQuery();
            getSquadInfo(rsSquadInfo);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseRequest.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private static void getNextMatches(ResultSet result){
        String queryOutput = "";
        try {
            while(result.next()){
                String nameDivision = result.getString("name_division");
                int tour = result.getInt("id_tour");
                String t_home = result.getString("team_home");
                String t_guest = result.getString("team_guest");
                String m_date = result.getString("m_date");
                String stadium = result.getString("name_stadium");
                queryOutput += nameDivision + " " + tour + " " +t_home +  " " + t_guest + " " 
                            + m_date + " " + stadium + "\n";
                nextMatches.add(new NextMatches(nameDivision, tour, t_home, t_guest, m_date, stadium));
            }
            System.out.println("DataBaseRequest getNextMatchrs():output query  from DB:" + queryOutput);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void getSquadInfo(ResultSet result){
        String queryOutput = "";
        try {
            while(result.next()){
                String team_name = result.getString("team_name");
                String name = result.getString("name");
                String amplua = result.getString("name_amplua");
                String birthdate = result.getString("birthdate");
                int number = result.getInt("number");
                int games = result.getInt("gemes");
                int goal = result.getInt("goal");
                int penalty = result.getInt("penalty");
                int assist = result.getInt("assist");
                int yellow = result.getInt("yellow_card");
                int red = result.getInt("red_card");
                String photo = result.getString("photo");
                queryOutput+=team_name + " " + name + " " + amplua + " " + birthdate + " " + number + " " +
                        games + " " + goal + " " + penalty + " " + assist + " " +yellow+ " " + red + " " + photo + "\n";
                squadInfo.add(new Player(team_name,name,amplua,birthdate,number, games, goal, penalty, assist, yellow, red, photo) );
            }
            System.out.println("DataBaseRequest getSquadInfo():output query  from DB:" + queryOutput);
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

    public static ArrayList<NextMatches> getNextMatches() {
        return DataBaseRequest.nextMatches;
    }

    public static ArrayList<Player> getSquadInfo() {
        return squadInfo;
    }
   
    
}
