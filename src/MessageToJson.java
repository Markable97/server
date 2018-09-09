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
    private int id_division;
    private int id_tour;

    public MessageToJson(int id_division, int id_tour) {
        this.id_division = id_division;
        this.id_tour = id_tour;
    }  
    
    public int getId_division() {
        return id_division;
    }

    public int getId_tour() {
        return id_tour;
    }
}
