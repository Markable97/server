
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
    
    ArrayList<Teams> teamsArray  = new ArrayList();//���� ������ ��� �������
    ArrayList<TournamentTable> tournamentArray = new ArrayList<>();//��������� ������� � ���� �������
    ArrayList<PrevMatches> prevMatchesArray = new ArrayList<>();//������ ���������� ���� � ���� �������
    
    DataInputStream in;
    DataOutputStream out;
    //DataOutputStream outTournamentTable;
    
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
            System.out.println("�lient is connected");
      
            String input;
            int id_division = 0, id_tour = 0;
            while(fromclient.isConnected()){
                System.out.println("Wait message..."); 
                input = in.readUTF();
                if(input.equalsIgnoreCase("close")){
                    System.out.println("Client closes the connection");
                    out.writeUTF("Disconnect from the server");
                    //out.flush();
                    break;
                }
                
                
                System.out.println("String received from the client = " + input);
                MessageToJson messageToJson = gson.fromJson(input, MessageToJson.class);
                id_division = messageToJson.getId_division();
                id_tour = messageToJson.getId_tour();
                DataBaseQuery baseQuery = new DataBaseQuery(id_division, id_tour);//������ ������ � ���������� � �������� � ��
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
                
                tournamentArray = baseQuery.getTournamentTable();
                String tournamentTableToJson = gson.toJson(tournamentArray);
                
                prevMatchesArray = baseQuery.getResultsPrevMatches();
                String prevMatchesToJson = gson.toJson(prevMatchesArray);
                System.out.println("[1]Array of object from DB to JSON");
                System.out.println(tournamentTableToJson);
                System.out.println("[2]Array of object from DB to JSON");
                System.out.println(prevMatchesToJson);
                
                out.writeUTF(tournamentTableToJson);
                out.writeUTF(prevMatchesToJson);
                
                System.out.println("�������� ������ ��� ������");
                String path = "D:\\�����\\������\\�������� ������\\";
                out.writeInt(tournamentArray.size());
                for(int i = 0;i < tournamentArray.size(); i++){
                   File image = new File(path + tournamentArray.get(i).getUrlImage());
                   if(image.exists()){
                       System.out.println("���� ���������� " + image.getName());
                       byte[] byteArray = new byte[(int)image.length()];
                       BufferedInputStream stream = new BufferedInputStream(new FileInputStream(image));
                       stream.read(byteArray, 0, byteArray.length);
                       stream.close();
                       System.out.println("���-�� ������ " + byteArray.length);
                       out.writeInt(byteArray.length);
                       out.write(byteArray);
                       //out.flush();
                   }else{
                       System.out.println("���� �� ����������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                   }
                }
                
                //out.flush();
            }
            /*do{
                System.out.println("�������� ���������..."); 
                input = in.readUTF();
                if(input.equals("close")){
                    out.flush();
                    break;
                }
                System.out.println("������ ������ �� ������� = " + input);
                DataBaseQuery baseQuery = new DataBaseQuery(input);//������ ������ � ���������� � �������� � ��
                //out.writeUTF(input.toUpperCase()); //������ ������� � ������� ��������
                out.writeUTF(baseQuery.getQueryOutput());//�������� ������������ �������
                out.flush();
            }
            while(!input.equalsIgnoreCase("Close"));*/
            System.out.println("Disconnect client, close channels....");
            in.close();
            out.close();
            //outTournamentTable.close();
            fromclient.close();
        } catch (IOException ex) {
            
        }
    }
    
}