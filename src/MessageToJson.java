/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Markable
 */
public class MessageToJson {
    private String messageLogic;
    private int id_division;
    private int id_team;
    

    public MessageToJson(String messageLogic, int id_division, int id_team) {
        this.messageLogic = messageLogic;
        this.id_division = id_division;
        this.id_team = id_team;
        
    }  

    public String getMessageLogic() {
        return messageLogic;
    }
    
    public int getId_division() {
        return id_division;
    }

    public int getId_team() {
        return id_team;
    }

    @Override
    public String toString() {
        return "MessageToJson{" + "messageLogic=" + messageLogic + ", id_division=" + id_division + ", id_team=" + id_team + '}';
    }
    
}
