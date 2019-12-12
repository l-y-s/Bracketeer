package com.csci490.bracketeer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
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

        if(currentGameState.getCurrentRound() == 1 && !currentGameState.getLoaded()) {
            Collections.shuffle(currentPlayers);
            createInitalSeeds();
        }
        else {
            createSeeds();
        }
        populateSeeds();
    }

    private void createInitalSeeds(){
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

    private void createSeeds(){
        List<Player> playerQueue = new ArrayList<>();
        playerQueue.addAll(currentPlayers);

        while(playerQueue.size() != 0) {
            List<Player> pair = new ArrayList<>();
            if(playerQueue.size() == 1){
                pair.add(playerQueue.get(0));
                playerQueue.remove(0);
            }
            else {
                Player first = playerQueue.get(0);
                Player second = playerQueue.get(1);
                for (int i = 2; i < playerQueue.size(); i++) {
                    if (playerQueue.get(i).getMatchPoints() > first.getMatchPoints()) {
                        second = first;
                        first = playerQueue.get(i);
                    } else if (playerQueue.get(i).getMatchPoints() > second.getMatchPoints()) {
                        second = playerQueue.get(i);
                    } else {
                        continue;
                    }
                }
                pair.add(first);
                pair.add(second);
                playerQueue.remove(first);
                playerQueue.remove(second);
            }
            seeds.add(pair);
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
                rightPlayer.setText(R.string.bye);
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

    public void save(View view){
        final EditText input = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name for tournament:");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentGameState.setName(input.getText().toString());
                currentGameState.setSerialVersionUID(ObjectStreamClass.lookup(currentGameState.getClass()).getSerialVersionUID());
                String filename = currentGameState.getName();
                try {
                    File dirFile = new File(getFilesDir(), filename);
                    FileOutputStream fos = new FileOutputStream(dirFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(currentGameState);
                    oos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void nextRound(View view){
        int numPlayers = currentPlayers.size();
        double totalRounds = Math.log(numPlayers) / Math.log(2);
        totalRounds = Math.ceil(totalRounds);
        LinearLayout layout = findViewById(R.id.leftLayout);
        CheckBox box;
        Intent intent = null;

        for(List<Player> currentSeed : seeds){
            if(currentSeed.size() == 1){
                currentSeed.get(0).incrementMatchPoints(3);
                currentSeed.get(0).addWinLoss(1);
                currentSeed.get(0).addPreviousOpponent("Bye");
            }
            else {
                box = layout.findViewWithTag(currentSeed.get(0).getName());
                if (box.isChecked()) {
                    currentSeed.get(0).incrementMatchPoints(3);
                    currentSeed.get(0).addWinLoss(1);
                    currentSeed.get(1).addWinLoss(0);
                } else {
                    currentSeed.get(1).incrementMatchPoints(3);
                    currentSeed.get(1).addWinLoss(1);
                    currentSeed.get(0).addWinLoss(0);
                }
                currentSeed.get(0).addPreviousOpponent(currentSeed.get(1).getName());
                currentSeed.get(1).addPreviousOpponent(currentSeed.get(0).getName());
            }
        }

        if(currentGameState.getCurrentRound() == totalRounds){
            Player first = currentPlayers.get(0);
            Player second = currentPlayers.get(1);
            Player third = currentPlayers.get(2);

            for(int i = 3; i < currentPlayers.size(); i++){
                Player player = currentPlayers.get(i);
                if(player.getMatchPoints() > first.getMatchPoints()){
                    third = second;
                    second = first;
                    first = player;
                }
                else if(player.getMatchPoints() > second.getMatchPoints()){
                    third = second;
                    second = player;
                }
                else if(player.getMatchPoints() > third.getMatchPoints()){
                    third = player;
                }
                else {
                    continue;
                }
            }
            intent = new Intent(this, NonElimWinnerActivity.class);
            intent.putExtra("first", first.getName());
            intent.putExtra("second", second.getName());
            intent.putExtra("third", third.getName());
        }

        else {
            currentGameState.incrementRound();
            Bundle bundle = new Bundle();
            bundle.putSerializable("gameState", currentGameState);

            intent = new Intent(this, SwissRoundPairings.class);
            intent.putExtras(bundle);
        }

        startActivity(intent);
        finish();
    }
}
