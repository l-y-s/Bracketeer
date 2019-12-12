package com.csci490.bracketeer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private int matchPoints;
    private List<String> previousOpponents = new ArrayList<>();
    private List<Integer> winLoss = new ArrayList<>();
    private boolean doubleElimLoss = false;

    Player(String newName){
        name = newName;
        matchPoints = 0;
    }

    public String getName(){
        return name;
    }

    public int getMatchPoints(){
        return matchPoints;
    }

    public void incrementMatchPoints(int points){
        matchPoints += points;
    }

    public String getPreviousOpponent(int round){
        return previousOpponents.get(round - 1);
    }

    public void addPreviousOpponent(String opponent){
        previousOpponents.add(opponent);
    }

    public void addWinLoss(int flag){
        winLoss.add(flag);
    }

    public boolean getLoss(){
        return doubleElimLoss;
    }

    public void setDoubleElimLoss(boolean status){
        doubleElimLoss = status;
    }
}
