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
    
    private String playerName;
    transient String playerUrlImage;
    String birhtday;
    String amplua;
    private int games;
    private int goal;
    private int assist;
    private int yellowCard;
    private int redCard;

    public Player(String playerName, int games, int goal, int assist, int yellowCard, int redCard, String playerUrlImage) {
        this.playerName = playerName;
        this.games = games;
        this.goal = goal;
        this.assist = assist;
        this.yellowCard = yellowCard;
        this.redCard = redCard;
        this.playerUrlImage = playerUrlImage;
    }

    public String getPlayerName() {
        return playerName;
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
