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
    
     String nameDivision;
     int idTour;
     String teamHome;
     int goalHome;
     int goalVisit;
     String teamVisit;
     transient String urlImageHome;
     transient String urlImageGuest;

    public PrevMatches(String nameDivision, int idTour, String teamHome, int goalHome, int goalVisit, String teamVisit) {
        this.nameDivision = nameDivision;
        this.idTour = idTour;
        this.teamHome = teamHome;
        this.goalHome = goalHome;
        this.goalVisit = goalVisit;
        this.teamVisit = teamVisit;
    }

    public PrevMatches(String nameDivision, int idTour, String teamHome, int goalHome, int goalVisit, String teamVisit, String urlImageH, String urlImageG) {
        this.nameDivision = nameDivision;
        this.idTour = idTour;
        this.teamHome = teamHome;
        this.goalHome = goalHome;
        this.goalVisit = goalVisit;
        this.teamVisit = teamVisit;
        this.urlImageHome = urlImageH;
        this.urlImageGuest = urlImageG;
    }

    @Override
    public String toString() {
        return "PrevMatches{" + "nameDivision=" + nameDivision + ", idTour=" + idTour + ", teamHome=" + teamHome + 
                ", goalHome=" + goalHome + ", goalVisit=" + goalVisit + ", teamVisit=" + teamVisit + "}\n";
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

    String getUrlImageHome() {
      return urlImageHome;
    }

    String getUrlImageGuest() {
        return urlImageGuest;
    }
}
