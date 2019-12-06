package com.csci490.bracketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SingleElimRoundPairings extends AppCompatActivity {

    GameState currentGameState;
    List<Player> currentPlayers;
    List<List<Player>> seeds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_elim_round_pairings);
        currentGameState = (GameState) getIntent().getExtras().getSerializable("gameState");
        currentPlayers = currentGameState.getCurrentPlayers();
        TextView round = findViewById(R.id.roundLabel);
        String roundLabel = "Round " + Integer.toString(currentGameState.getCurrentRound());
        round.setText(roundLabel);

        if(!currentGameState.getLoaded()) {
            Collections.shuffle(currentPlayers);
            createSeeds();
        }
    }

    private void createSeeds(){
        int numPlayers = currentPlayers.size();
        for(int i = 0; i < (numPlayers / 2); i++){
            List<Player> pair = new ArrayList<>();
            pair.add(currentPlayers.get(i));
            pair.add(currentPlayers.get(numPlayers - 1 - i));
            seeds.add(pair);
        }

        boolean presentFlag = false;
        for(Player currentPlayer : currentPlayers){
            for(List<Player> seed : seeds){
                if(seed.contains(currentPlayer)) {
                    presentFlag = true;
                    break;
                }
            }
            if(!presentFlag){
                List<Player> pair = new ArrayList<>();
                pair.add(currentPlayer);
                seeds.add(pair);
            }
            presentFlag = false;
        }
    }
}
