package com.csci490.bracketeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
        }
        createSeeds();
        populateSeeds();
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

    private void populateSeeds(){
        LinearLayout leftParent = findViewById(R.id.leftLayout);
        LinearLayout rightParent = findViewById(R.id.rightLayout);
        leftParent.setGravity(Gravity.CENTER_VERTICAL);
        rightParent.setGravity(Gravity.CENTER_VERTICAL);

        for(List<Player> currentSeed : seeds){
            LinearLayout leftSeedRow = new LinearLayout(getBaseContext());
            LinearLayout rightSeedRow = new LinearLayout(getBaseContext());
            leftSeedRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rightSeedRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            leftSeedRow.setGravity(Gravity.CENTER_HORIZONTAL);
            rightSeedRow.setGravity(Gravity.CENTER_HORIZONTAL);
            leftSeedRow.setPadding(20, 20, 20, 20);
            rightSeedRow.setPadding(20,20,20,20);

            leftParent.addView(leftSeedRow);
            rightParent.addView(rightSeedRow);

            TextView leftPlayer = new TextView(getBaseContext());
            TextView rightPlayer = new TextView(getBaseContext());
            Typeface face = ResourcesCompat.getFont(this, R.font.aldrich);
            leftPlayer.setTypeface(face);
            rightPlayer.setTypeface(face);

            leftPlayer.setText(currentSeed.get(0).getName());
            leftPlayer.setGravity(Gravity.CENTER_HORIZONTAL);

            CheckBox leftWinner = new CheckBox(getBaseContext());
            leftWinner.setTag(currentSeed.get(0).getName());

            leftSeedRow.addView(leftWinner);
            leftSeedRow.addView(leftPlayer);

            if(currentSeed.size() == 1){
                rightPlayer.setText("Bye");
                rightPlayer.setGravity(Gravity.CENTER_HORIZONTAL);
                leftWinner.setChecked(true);
                rightSeedRow.addView(rightPlayer);
            }
            else {
                rightPlayer.setText(currentSeed.get(1).getName());
                rightPlayer.setGravity(Gravity.CENTER_HORIZONTAL);
                CheckBox rightWinner = new CheckBox(getBaseContext());
                rightWinner.setTag(currentSeed.get(1).getName());
                rightSeedRow.addView(rightWinner);
                rightSeedRow.addView(rightPlayer);
            }
        }
    }

    public void nextRound(View view){
        LinearLayout layout = findViewById(R.id.leftLayout);
        CheckBox box;
        List<Player> winners = new ArrayList<>();
        Intent intent;

        for(List<Player> currentSeed : seeds){
            if(currentSeed.size() == 1){
                winners.add(currentSeed.get(0));
            }
            else {
                box = layout.findViewWithTag(currentSeed.get(0).getName());
                if (box.isChecked()) {
                    winners.add(currentSeed.get(0));
                } else {
                    winners.add(currentSeed.get(1));
                }
            }
        }

        currentGameState.incrementRound();
        currentGameState.setCurrentPlayers(winners);

        if(winners.size() == 1){
            intent = new Intent(this, WinnerActivity.class);
            intent.putExtra("winner", winners.get(0).getName());
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("gameState", currentGameState);

            intent = new Intent(this, SingleElimRoundPairings.class);
            intent.putExtras(bundle);
        }

        startActivity(intent);
    }
}
