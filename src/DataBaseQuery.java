import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Markable
 */
public class DataBaseQuery {
    
    private static int queryDivision;
    private static int queryTour;
    private static String queryOutput="";
    
    private static String user = "root";
    private static String password = "7913194";
    private static String url = "jdbc:mysql://localhost:3306/footbal_database";
    
    private static Connection connect;
    private static Statement statment;
    
    private static PreparedStatement prepStateTable;
    private static PreparedStatement prepStateResultsPrevMatches;
    private static PreparedStatement prepStateCalendarNextMatches;
    
    private static ResultSet resultTournamnetTable; 
    private static ResultSet resultsPrevMatches; 
    private static ResultSet calendarNextMatches;
    
    private static ArrayList<Teams> teamsAllArray  = new ArrayList<Teams>();
    private static ArrayList<TournamentTable> tournamentArray = new ArrayList<TournamentTable>();
    private static ArrayList<PrevMatches> prevMatchesArray = new ArrayList<>();
    //private static String outputQuery = "";

    
    public DataBaseQuery(int dataDivision, int dataTour){
        queryDivision = dataDivision;
        queryTour = dataTour;
        teamsAllArray.clear();
        tournamentArray.clear();
        prevMatchesArray.clear();
       //queryOutput="";
        connection(queryDivision, queryTour);
    }
    
    
   ArrayList<TournamentTable> getTournamentTable(){
       return this.tournamentArray;
   }
   
   ArrayList<PrevMatches> getResultsPrevMatches(){
       return this.prevMatchesArray;
   }
   
    ArrayList<Teams> getTeamListDivision(){
        //return queryOutput;
        return this.teamsAllArray;
    }
    String getQueryOutput(){
        return queryOutput;
    }
    
   
    
    private static void connection(int qDiv, int qTour){
        try {
            connect = DriverManager.getConnection(url, user, password);
            statment = connect.createStatement();
            //results = statment.executeQuery(query);
            prepStateTable = connect.prepareStatement("SELECT  name_division, team_name, games,point,wins,draws,losses\n" +
"FROM footbal_database.tournament_table j\n" +
"join divisions d on id_division = j.divisions_id_division\n" +
"join teams t on id_team = teams_id_team\n" +
"where j.divisions_id_division = ?\n" +
"order by point desc;");
            prepStateTable.setInt(1, qDiv);
            resultTournamnetTable = prepStateTable.executeQuery();
            //Teams team;
            tournamentTableByDivision(resultTournamnetTable);//
            
            prepStateResultsPrevMatches = connect.prepareStatement("SELECT name_division,id_tour,h.team_name,goal_home,goal_visitor, g.team_name, date\n" +
"FROM footbal_database.matches m\n" +
"join teams h on teams_id_teamHome = h.id_team\n" +
"join teams g on teams_id_teamVisitor = g.id_team\n" +
"join divisions d on m.divisions_id_division = id_division\n" +
"where id_tour = ? and m.divisions_id_division = ?;");
            prepStateResultsPrevMatches.setInt(1, qTour);
            prepStateResultsPrevMatches.setInt(2, qDiv);
            resultsPrevMatches = prepStateResultsPrevMatches.executeQuery();
            resultsPrevMatchesByDivision(resultsPrevMatches);
            /*while(results.next()){
                int id = results.getInt("id_team");
                String team_name = results.getString("team_name");
                String date = results.getString("creation_date");
                int id_division = results.getInt("divisions_id_division");
                teamsAllArray.add(new Teams(id, team_name, date, id_division));
                //int number = result.getInt(6);
               // team = new Teams();
                queryOutput += id + " " + team_name + " " + date + " " + id_division +"\n";
            }
            System.out.println("[1]Вывод запроса из класса БД \n" + queryOutput);*/
            //System.out.println("[2]Вывод запроса из класса БД \n" + outputQuery);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                connect.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                statment.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                resultTournamnetTable.close();
                //results.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //функция заполнения массива турнирной таблицы
    private static void tournamentTableByDivision(ResultSet result){
        String queryOutput = "";
        
        try {
            while(result.next()){
                String nameDivision = result.getString("name_division");
                String teamName = result.getString("team_name");
                int games = result.getInt("games");
                int point = result.getInt("point");
                int wins = result.getInt("wins");
                int draws = result.getInt("draws");
                int losses = result.getInt("losses");
                tournamentArray.add(new TournamentTable(nameDivision, teamName, games, point, wins, draws, losses));
                queryOutput += nameDivision + " " + teamName + " " + games + " " + point + " " + wins + " "  + draws + " "
                        + losses + "\n";
            }
           System.out.println("[1]Вывод запроса из класса БД \n" + queryOutput);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void resultsPrevMatchesByDivision(ResultSet result) {
       String queryOutput = "";
        try {
            while(result.next()){
                String nameDivision = result.getString("name_division");
                int idTour = result.getInt("id_tour");
                String teamHome = result.getString("h.team_name");
                int goalHome = result.getInt("goal_home");
                int goalVisit = result.getInt("goal_visitor");
                String teamVisit = result.getString("g.team_name");
                prevMatchesArray.add(new PrevMatches(nameDivision, idTour, teamHome, goalHome, goalVisit, teamVisit));
                queryOutput += nameDivision + " " + idTour + " " +teamHome + " " + goalHome 
                        + " " + goalVisit + " " + teamVisit + "\n";
            }
            //DataBaseQuery.queryOutput = queryOutput;
            System.out.println("[2]Вывод запроса из класса БД \n" + queryOutput);
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
