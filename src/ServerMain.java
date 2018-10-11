
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    
    String input;
    String messageLogic;
    int id_division;
    int id_team;
    
    ArrayList<TournamentTable> tournamentArray;//турнирная таблица в виде массива
    ArrayList<PrevMatches> prevMatchesArray;//список прошедшего тура в виде массива
    ArrayList<NextMatches> nextMatchesArray;//список на следующие игры
    ArrayList<Player> playersArray;//список игроков одной команды
    
    DataInputStream in;
    DataOutputStream out;
    //DataOutputStream outTournamentTable;
    
    MessageToJson messageToJson;
    
    Gson gson = new Gson();
    
    public ThreadClient(Socket client, int numberUser) throws IOException{
        this.fromclient = client;
        System.out.println(client.getInetAddress() + " connection number = " + numberUser);
        in = new DataInputStream(fromclient.getInputStream());
        out = new DataOutputStream(fromclient.getOutputStream());
        //outTournamentTable = new DataOutputStream(fromclient.getOutputStream());
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Сlient is connected");
            exit:
            while(fromclient.isConnected()){
                System.out.println("Wait message..."); 
                input = in.readUTF();
               
                //System.out.println("new branch locig server");
               
                System.out.println("String received from the client = " + input);
                messageToJson = gson.fromJson(input, MessageToJson.class);
                System.out.println(messageToJson.toString());
                id_division = messageToJson.getId_division();
                messageLogic = messageToJson.getMessageLogic();
                id_team = messageToJson.getId_team();
                switch(messageLogic){
                    case "close":
                        System.out.println("Client closes the connection");
                        fromclient.close();
                        break exit;
                    case "division":
                        DataBaseQuery baseQuery = new DataBaseQuery(id_division, id_team);//объект класса с соедиением и запросом к бд
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
                
                        nextMatchesArray = baseQuery.getCalendar();
                        String nextMatchesToJson = gson.toJson(nextMatchesArray);
                
                        System.out.println("[1]Array of object from DB to JSON");
                        System.out.println(tournamentTableToJson);
                        System.out.println("[2]Array of object from DB to JSON");
                        System.out.println(prevMatchesToJson);
                        System.out.println("[3]Array of object from DB to JSON");
                        System.out.println(nextMatchesToJson);
                        System.out.println("New branch");
                        out.writeUTF(tournamentTableToJson);
                        out.writeUTF(prevMatchesToJson);
                        out.writeUTF(nextMatchesToJson);
                        //начало ветки
                        System.out.println("Добавляю потоки для файлов");
                        String path = "D:\\Учеба\\Диплом\\Логотипы команд\\";
                        String pathBig = "D:\\Учеба\\Диплом\\Логотипы команд\\BigImage\\"; 
                        out.writeInt(tournamentArray.size());
                        for(int i = 0; i < tournamentArray.size(); i++){
                            File image = new File(path + tournamentArray.get(i).getUrlImage());
                            File imageBig = new File(pathBig + tournamentArray.get(i).getUrlImage());
                            if(image.exists()){
                                if(imageBig.exists()){
                                    System.out.println("Файлы существует " + image.getName() + " " + imageBig.getName());
                                    String nameImage = tournamentArray.get(i).getUrlImage().replace(".png",""); 
                                    byte[] byteArray = new byte[(int)image.length()];
                                    BufferedInputStream stream = new BufferedInputStream(new FileInputStream(image));
                                    stream.read(byteArray, 0, byteArray.length);
                                    stream.close();
                                    System.out.println("Кол-во байтов " + byteArray.length);
                                    out.writeUTF(nameImage);
                                    out.writeInt(byteArray.length);
                                    out.write(byteArray);
                                    byte[] byteArrayBig = new byte[(int)imageBig.length()];
                                    BufferedInputStream streamBig = new BufferedInputStream(new FileInputStream(imageBig));
                                    streamBig.read(byteArrayBig, 0, byteArrayBig.length);
                                    streamBig.close();
                                    out.writeInt(byteArrayBig.length);
                                    out.write(byteArrayBig);
                                    //out.flush();
                                }else{
                                    System.out.println("BIG Файл не сущуствует!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    }
                            }else{
                                System.out.println("Файл не сущуствует!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }
                        }
                        break;
                    case "team":
                        System.out.println("Case team");
                        DataBaseQuery baseQuery1 = new DataBaseQuery(id_division, id_team);
                        playersArray = baseQuery1.getPlayerArray();
                        String playersToJson = gson.toJson(playersArray);
                        System.out.println("[4]Array of object from DB to JSON");
                        System.out.println(playersToJson);
                        out.writeUTF(playersToJson);
                        break;
                    case "player":
                        break;
                }//case 
            }//while 
           
            System.out.println("Disconnect client, close channels....");
            System.out.println("waiting for a new client*********");
            in.close();
            out.close();
            //outTournamentTable.close();
            fromclient.close();
        } catch (IOException ex) {
            
        }
    }
    
}