
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Markable
 */
public class ServerMain {
    
    static ExecutorService executeIt = Executors.newFixedThreadPool(10);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
      // Scanner scanner = new Scanner(System.in);
       //String serverComand;
       System.out.println("Enabling the server");
       ServerSocket server = new ServerSocket(55555);
       int number = 0;
        try {
            while(!server.isClosed()){
                System.out.println("Waiting for a response from the client");
                Socket fromclient = server.accept();
                executeIt.execute(new ThreadClient(fromclient, number));
                number++;
                //executeIt.shutdown();
            }
            /*Socket fromclient = server.accept(); 
            System.out.println("Клиент приконектился");
            InputStream inu = fromclient.getInputStream();
            OutputStream outu = fromclient.getOutputStream();
            DataInputStream in = new DataInputStream(inu);
            DataOutputStream out = new DataOutputStream(outu);
            String input, output;
            do{
                input = in.readUTF();
                System.out.println("Ожидание сообщения...");
                System.out.println("Стркоа пришла от клиента = " + input);
                out.writeUTF(input.toUpperCase()); //переда клиенту в большом регистре
                out.flush();
            }
            while(!input.equals("Закрыть"));
            in.close();
            out.close();
            fromclient.close();*/
                
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            server.close();
        }
        
    }
    
}

class ThreadClient implements Runnable {

    Socket fromclient;
    
    ArrayList<Teams> teamsArray  = new ArrayList();//лист массив для объктов
    ArrayList<TournamentTable> tournamentArray = new ArrayList<>();//турнирная таблица в виде массива
    ArrayList<PrevMatches> prevMatchesArray = new ArrayList<>();//список прошедшего тура в виде массива
    
    DataInputStream in;
    DataOutputStream outPrevMatches;
    DataOutputStream outTournamentTable;
    
    Gson gson = new Gson();
    
    public ThreadClient(Socket client, int numberUser) throws IOException{
        this.fromclient = client;
        System.out.println(client.getInetAddress() + " connection number = " + numberUser);
        in = new DataInputStream(fromclient.getInputStream());
        outPrevMatches = new DataOutputStream(fromclient.getOutputStream());
        outTournamentTable = new DataOutputStream(fromclient.getOutputStream());
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Сlient is connected");
      
            String input;
            int id_division = 0, id_tour = 0;
            while(!fromclient.isClosed()){
                System.out.println("Wait message..."); 
                input = in.readUTF();
                if(input.equalsIgnoreCase("close")){
                    System.out.println("Client closes the connection");
                    outPrevMatches.writeUTF("Disconnect from the server");
                    //out.flush();
                    break;
                }
                System.out.println("String received from the client = " + input);
                MessageToJson messageToJson = gson.fromJson(input, MessageToJson.class);
                id_division = messageToJson.getId_division();
                id_tour = messageToJson.getId_tour();
                DataBaseQuery baseQuery = new DataBaseQuery(id_division, id_tour);//объект класса с соедиением и запросом к бд
                //out.writeUTF(input.toUpperCase()); //переда клиенту в большом регистре
                
               // teamsArray = baseQuery.getTeamListDivision();//массиву присваевается массив объектов из запроса к бд
                //System.out.println("[1]Массив объектов из бд в JSON");
                //String teamsArrayToJson = gson.toJson(teamsArray);
               // System.out.println(teamsArrayToJson);
                /*System.out.println("Обычный перебор массива из бд");
                for (Teams test : teamsArray) {
                    System.out.println(test.getId() + " " + test.getName() + " " + test.getDate() + " "
                    + test.getIdDivision());
                }*/
                //testOu.writeObject(teamsArray);//передача потока клиенту(массив объектов)
                //out.writeUTF(baseQuery.getQueryOutput());//передача выполненного запроса(строкая переменная)
                //outListTeams.writeUTF(teamsArrayToJson);
                
                tournamentArray = baseQuery.getTournamentTable();
                String tournamentTableToJson = gson.toJson(tournamentArray);
                
                prevMatchesArray = baseQuery.getResultsPrevMatches();
                String prevMatchesToJson = gson.toJson(prevMatchesArray);
                System.out.println("[1]Array of object from DB to JSON");
                System.out.println(tournamentTableToJson);
                System.out.println("[2]Array of object from DB to JSON");
                System.out.println(prevMatchesToJson);
                
                outTournamentTable.writeUTF(tournamentTableToJson);
                outPrevMatches.writeUTF(prevMatchesToJson);
                //out.flush();
            }
            /*do{
                System.out.println("Ожидание сообщения..."); 
                input = in.readUTF();
                if(input.equals("close")){
                    out.flush();
                    break;
                }
                System.out.println("Стркоа пришла от клиента = " + input);
                DataBaseQuery baseQuery = new DataBaseQuery(input);//объект класса с соедиением и запросом к бд
                //out.writeUTF(input.toUpperCase()); //переда клиенту в большом регистре
                out.writeUTF(baseQuery.getQueryOutput());//передача выполненного запроса
                out.flush();
            }
            while(!input.equalsIgnoreCase("Close"));*/
            System.out.println("Disconnect client, close channels....");
            in.close();
            outPrevMatches.close();
            outTournamentTable.close();
            fromclient.close();
        } catch (IOException ex) {
            
        }
    }
    
}