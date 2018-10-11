/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Markable
 */
public class Player {
    
    private int idPlayer;
    private String playerTeam;
    private String playerName;
    transient String playerUrlImage;
    String birhtday;
    String amplua;
    private int number;
    private int games;
    private int goal;
    private int assist;
    private int yellowCard;
    private int redCard;

    public Player(int idPlayer,String playerTeam, String playerName, String amplua, String birhtday, int number, 
            int games, int goal, int assist, int yellowCard, int redCard,String playerUrlImage) {
        this.idPlayer = idPlayer;
        this.playerTeam = playerTeam;
        this.playerName = playerName;
        this.playerUrlImage = playerUrlImage;
        this.birhtday = birhtday;
        this.amplua = amplua;
        this.number = number;
        this.games = games;
        this.goal = goal;
        this.assist = assist;
        this.yellowCard = yellowCard;
        this.redCard = redCard;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public String getPlayerTeam() {
        return playerTeam;
    }

    
    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerUrlImage() {
        return playerUrlImage;
    }

    public String getBirhtday() {
        return birhtday;
    }

    public String getAmplua() {
        return amplua;
    }

    public int getNumber() {
        return number;
    }

    
    public int getGoal() {
        return goal;
    }

    public int getGames() {
        return games;
    }

    public int getAssist() {
        return assist;
    }

    public int getYellowCard() {
        return yellowCard;
    }

    public int getRedCard() {
        return redCard;
    }
    
}
