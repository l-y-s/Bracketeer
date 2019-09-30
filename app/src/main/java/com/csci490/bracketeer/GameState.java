package com.csci490.bracketeer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState implements Serializable {
    private List<String> currentPlayers;
    private String tournamentMode;
    private Map matchPoints = new HashMap();
    private int currentRound = 0;

    GameState(List<String> playerList, String mode) {
        this.currentPlayers = playerList;
        this.tournamentMode = mode;

        for(int i = 0; i < currentPlayers.size(); i++) {
            this.matchPoints.put(currentPlayers.get(i), 0);
        }
    }

    public void incrementRound() {
        currentRound++;
    }

    public int getCurrentRound() {
        return currentRound;
    }
}
