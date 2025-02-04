package com.csci490.bracketeer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GameState implements Serializable {
    private long serialVersionUID;

    private String tournamentName;
    private List<Player> currentPlayers = new ArrayList<>();
    private String tournamentMode;
    private int currentRound = 1;
    private boolean loaded = false;

    GameState(List<String> playerList, String mode) {
        this.tournamentMode = mode;
        for(int i = 0; i < playerList.size(); i++){
            String tmpName = playerList.get(i);
            Player tmpPlayer = new Player(tmpName);
            currentPlayers.add(tmpPlayer);
        }
    }

    public void incrementRound() {
        currentRound++;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public List<Player> getCurrentPlayers(){
        return currentPlayers;
    }

    public void setCurrentPlayers(List<Player> winners){
        currentPlayers = winners;
    }

    public boolean getLoaded(){ return loaded; }

    public void setLoaded(){
        loaded = true;
    }

    public String getName(){
        return tournamentName;
    }

    public void setName(String name){
        tournamentName = name;
    }

    public String getMode(){
        return tournamentMode;
    }

    public void setSerialVersionUID(long uid){
        serialVersionUID = uid;
    }
}
