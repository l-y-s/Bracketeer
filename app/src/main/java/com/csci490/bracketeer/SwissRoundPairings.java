package com.csci490.bracketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwissRoundPairings extends AppCompatActivity {
    GameState currentGameState;
    List<Player> currentPlayers;
    List<List<Player>> seeds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_pairings);
        currentGameState = (GameState) getIntent().getExtras().getSerializable("gameState");
        currentPlayers = currentGameState.getCurrentPlayers();
        TextView round = findViewById(R.id.roundLabel);
        String roundLabel = "Round " + currentGameState.getCurrentRound();
        round.setText(roundLabel);

        if(!currentGameState.getLoaded()) {
            Collections.shuffle(currentPlayers);
        }
        //createSeeds();
        //populateSeeds();
    }
}
