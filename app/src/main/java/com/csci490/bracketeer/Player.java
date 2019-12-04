package com.csci490.bracketeer;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private int matchPoints;
    private List<String> previousOpponents;
    private List<Integer> winLoss;

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

    public String getPreviousOpponent(int round){
        return previousOpponents.get(round - 1);
    }

}
