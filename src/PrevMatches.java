/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Markable
 */
public class PrevMatches {
    
    private String nameDivision;
    private int idTour;
    private String teamHome;
    private int goalHome;
    private int goalVisit;
    private String teamVisit;

    public PrevMatches(String nameDivision, int idTour, String teamHome, int goalHome, int goalVisit, String teamVisit) {
        this.nameDivision = nameDivision;
        this.idTour = idTour;
        this.teamHome = teamHome;
        this.goalHome = goalHome;
        this.goalVisit = goalVisit;
        this.teamVisit = teamVisit;
    }

    
    
    public String getNameDivision() {
        return nameDivision;
    }

    public int getIdTour() {
        return idTour;
    }

    public String getTeamHome() {
        return teamHome;
    }

    public int getGoalHome() {
        return goalHome;
    }

    public int getGoalVisit() {
        return goalVisit;
    }

    public String getTeamVisit() {
        return teamVisit;
    }
    
    
}
