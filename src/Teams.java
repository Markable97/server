
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Markable
 */
public class Teams implements Serializable{
    private int id;
    private String name;
    private String creation_date;
    private int id_division;
  
    public Teams(int id, String name, String date, int id_division){
        setId(id);
        setName(name);
        setDate(date);
        setIdDivision(id_division);
    }
    
    private void setId(int id){
        this.id = id;
    }
    private void setName(String name){
        this.name = name;
    }
    private void setDate(String date){
        this.creation_date = date;
    }
    private void setIdDivision(int id){
        this.id_division = id;
    }
    
    int getId(){
        return this.id;
    }
    String getName(){
        return this.name;
    }
    String getDate(){
        return this.creation_date;
    }
    int getIdDivision(){
        return this.id_division;
    }
    }
