
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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
            System.out.println("������ �������������");
            InputStream inu = fromclient.getInputStream();
            OutputStream outu = fromclient.getOutputStream();
            DataInputStream in = new DataInputStream(inu);
            DataOutputStream out = new DataOutputStream(outu);
            String input, output;
            do{
                input = in.readUTF();
                System.out.println("�������� ���������...");
                System.out.println("������ ������ �� ������� = " + input);
                out.writeUTF(input.toUpperCase()); //������ ������� � ������� ��������
                out.flush();
            }
            while(!input.equals("�������"));
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
    String id_team;
    
    ArrayList<TournamentTable> tournamentArray;//��������� ������� � ���� �������
    ArrayList<PrevMatches> prevMatchesArray;//������ ���������� ���� � ���� �������
    ArrayList<NextMatches> nextMatchesArray;//������ �� ��������� ����
    ArrayList<Player> playersArray;//������ ������� ����� �������
    //ArrayList<PrevMatches> prevAllMatches;//������ ���� ������ ����� �������
    //ArrayList<PrevMatches> allMatchesArray;//������ ���� ������
    
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
        /*tournamentArray.clear();
        prevMatchesArray.clear();
        nextMatchesArray.clear();
        playersArray.clear();*/
        //outTournamentTable = new DataOutputStream(fromclient.getOutputStream());
    }
    
    @Override
    public void run() {
        try {
            System.out.println("�lient is connected");
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
                        DataBaseRequest baseRequest = new DataBaseRequest(id_division);
                        //DataBaseQuery baseQuery = new DataBaseQuery(id_division, id_team);//������ ������ � ���������� � �������� � ��
                         //out.writeUTF(input.toUpperCase()); //������ ������� � ������� ��������
                
                        // teamsArray = baseQuery.getTeamListDivision();//������� ������������� ������ �������� �� ������� � ��
                         //System.out.println("[1]������ �������� �� �� � JSON");
                        //String teamsArrayToJson = gson.toJson(teamsArray);
                        // System.out.println(teamsArrayToJson);
                        /*System.out.println("������� ������� ������� �� ��");
                         for (Teams test : teamsArray) {
                         System.out.println(test.getId() + " " + test.getName() + " " + test.getDate() + " "
                         + test.getIdDivision());
                        }*/
                         //testOu.writeObject(teamsArray);//�������� ������ �������(������ ��������)
                         //out.writeUTF(baseQuery.getQueryOutput());//�������� ������������ �������(������� ����������)
                         //outListTeams.writeUTF(teamsArrayToJson);
                
                        //tournamentArray = baseQuery.getTournamentTable();
                        tournamentArray = baseRequest.getTournamentTable();
                        String tournamentTableToJson = gson.toJson(tournamentArray);
                
                        //prevMatchesArray = baseQuery.getResultsPrevMatches();
                        prevMatchesArray = baseRequest.getPrevMatches();
                        String prevMatchesToJson = gson.toJson(prevMatchesArray);
                
                        //nextMatchesArray = baseQuery.getCalendar();
                        nextMatchesArray = baseRequest.getNextMatches();
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
                        //������ �����
                        /*System.out.println("�������� ������ ��� ������");
                        String path = "D:\\�����\\������\\�������� ������\\";
                        String pathBig = "D:\\�����\\������\\�������� ������\\BigImage\\"; 
                        out.writeInt(tournamentArray.size());//���-�� �����
                        for(int i = 0; i < tournamentArray.size(); i++){
                            File image = new File(path + tournamentArray.get(i).getUrlImage());
                            File imageBig = new File(pathBig + tournamentArray.get(i).getUrlImage());
                            if(image.exists()){
                                if(imageBig.exists()){
                                    System.out.println("����� ���������� " + image.getName() + " " + imageBig.getName());
                                    String nameImage = tournamentArray.get(i).getUrlImage().replace(".png",""); 
                                    out.writeUTF(nameImage);
                                    byte[] byteArrayBig = new byte[(int)imageBig.length()];
                                    BufferedInputStream streamBig = new BufferedInputStream(new FileInputStream(imageBig));
                                    streamBig.read(byteArrayBig, 0, byteArrayBig.length);
                                    streamBig.close();
                                    out.writeInt(byteArrayBig.length);
                                    out.write(byteArrayBig);
                                    //out.flush();
                                }else{
                                    System.out.println("BIG ���� �� ����������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    }
                            }else{
                                System.out.println("���� �� ����������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }
                        }*/
                        break;
                    case "team":
                        System.out.println("Case team");
                        //DataBaseQuery baseQuery1 = new DataBaseQuery(id_division, id_team);
                        //playersArray = baseQuery1.getPlayerArray();
                        DataBaseRequest baseRequest1 = new DataBaseRequest(id_team,messageLogic);
                        playersArray = baseRequest1.getSquadInfo();
                        String playersToJson = gson.toJson(playersArray);
                        System.out.println("[4]Array of object from DB to JSON");
                        System.out.println(playersToJson);
                        out.writeUTF(playersToJson);
                        /*System.out.println("�������� ������ ��� �������� ����� ������� ");
                        String pathPlayer = "D:\\�����\\������\\����� �������\\";
                        int countImage = 0;
                        for(int i = 0; i < playersArray.size(); i++){
                            File image = new File(pathPlayer + playersArray.get(i).getPlayerTeam() + "\\" +
                                    playersArray.get(i).getPlayerUrlImage());
                            System.out.println(image.getPath());
                            if(image.exists()){
                                System.out.println("���� ���������� " + image.getName());
                                countImage++;
                            }else{
                                System.out.println("���� �� ����������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }
                        }
                        out.writeInt(countImage);//���-�� �����
                        for(int i = 0; i < playersArray.size(); i++){
                            File image = new File(pathPlayer + playersArray.get(i).getPlayerTeam() + "\\" +
                                    playersArray.get(i).getPlayerUrlImage());
                            System.out.println(image.getPath());
                            if(image.exists()){
                                System.out.println("���� ���������� " + image.getName());
                                byte[] byteImagePlayer = new byte[(int)image.length()];
                                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(image));
                                stream.read(byteImagePlayer, 0, byteImagePlayer.length);
                                stream.close();
                                out.writeInt(byteImagePlayer.length);
                                out.write(byteImagePlayer);
                            }else{
                                System.out.println("���� �� ����������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }
                        }*/
                        break;
                    case "player":
                        break;
                    case "matches":
                        System.out.println("Case matches for team");
                        DataBaseRequest dbr = new DataBaseRequest(id_team, messageLogic);
                        //DataBaseQuery baseQueryAllMatches = new DataBaseQuery(id_division, id_team);
                        //allMatchesArray = baseQueryAllMatches.getAllMatches();
                        prevMatchesArray = dbr.getPrevMatches();
                        String prevAllMatchesForTeamToJson = gson.toJson(prevMatchesArray);
                        System.out.println("[5]Array of object from DB to JSON");
                        System.out.println(prevAllMatchesForTeamToJson);
                        out.writeUTF(prevAllMatchesForTeamToJson);
                        /*System.out.println("����� ��� �����");
                        String teamPath = "D:\\�����\\������\\�������� ������\\BigImage\\";
                        ArrayList<String> listImage = new ArrayList<>();
                        File imageStart = new File(teamPath + id_team + ".png");
                        if(imageStart.exists()){
                            System.out.println("ImageStart = " + imageStart.getName() );
                            listImage.add(imageStart.getName());
                        }else{
                            System.out.println("Image not found");
                        }
                        for(int i = 0; i< allMatchesArray.size(); i++){
                            File imH = new File(teamPath + allMatchesArray.get(i).getUrlImageHome());
                            //System.out.println("�������� ����� = " + imH.getName() );
                            File imG = new File(teamPath + allMatchesArray.get(i).getUrlImageGuest());
                            //System.out.println("�������� ����� = " + imG.getName());
                            if(imH.exists()&& imG.exists()){
                                if( imH.getName().equals(imageStart.getName()) == false ){
                                    System.out.println(imH.getName());
                                    listImage.add(imH.getName());
                                }
                                if( imG.getName().equals(imageStart.getName()) == false ){
                                    System.out.println(imG.getName());
                                    listImage.add(imG.getName());
                                }
                            }
                            else{
                                System.out.println("����� �� ����������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }
                        }
                        System.out.println("���-�� ������ = " + listImage.size() + listImage);
                        out.writeInt(listImage.size());
                        if(listImage.size()!=0){
                            for(int i = 0; i < listImage.size(); i++){
                                File file = new File(teamPath+listImage.get(i));
                                if(file.exists()){
                                    System.out.println("���� ���������� " + file.getName());
                                    String nameImage = listImage.get(i).replace(".png","");
                                    out.writeUTF(nameImage);
                                    byte[] byteImageTeam = new byte[(int)file.length()];
                                    BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
                                    stream.read(byteImageTeam, 0, byteImageTeam.length);
                                    stream.close();
                                    out.writeInt(byteImageTeam.length);
                                    out.write(byteImageTeam);
                                }
                            }
                        }
                        else{
                            System.out.println("���� �� ����������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }*/
                        //out.write(listImage.size());
                        
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
            
        } catch (SQLException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}