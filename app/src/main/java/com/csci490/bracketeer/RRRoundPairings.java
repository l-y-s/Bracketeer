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
import java.util.List;

public class RRRoundPairings extends AppCompatActivity {
    GameState currentGameState;
    List<Player> currentPlayers;
    List<Player> topRow = new ArrayList<>();
    List<Player> bottomRow = new ArrayList<>();
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

        if(currentPlayers.size() % 2 != 0){
            Player dummy = new Player("NULL");
            currentPlayers.add(dummy);
        }

        createSeeds();
        populateSeeds();
    }

    private void createSeeds(){
        int numPlayers = currentPlayers.size();
        for(int i = 0; i < (numPlayers / 2); i++){
            topRow.add(currentPlayers.get(i));
            bottomRow.add(currentPlayers.get(numPlayers - 1 - i));
        }
        for(int i = 0; i < topRow.size(); i++){
            List<Player> pair = new ArrayList<>();
            pair.add(topRow.get(i));
            pair.add(bottomRow.get(i));
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
            CheckBox leftWinner = new CheckBox(getBaseContext());
            CheckBox rightWinner = new CheckBox(getBaseContext());

            Player left = currentSeed.get(0);
            Player right = currentSeed.get(1);

            if(left.getName().equals("NULL")){
                leftPlayer.setText(R.string.bye);
                leftPlayer.setGravity(Gravity.CENTER_HORIZONTAL);
                rightPlayer.setText(right.getName());
                rightWinner.setTag(right.getName());
                rightWinner.setChecked(true);

                leftSeedRow.addView(leftPlayer);
                rightSeedRow.addView(rightWinner);
                rightSeedRow.addView(rightPlayer);
            }
            else if(right.getName().equals("NULL")){
                leftPlayer.setText(left.getName());
                leftPlayer.setGravity(Gravity.CENTER_HORIZONTAL);
                leftWinner.setTag(left.getName());
                leftWinner.setChecked(true);
                rightPlayer.setText(R.string.bye);

                leftSeedRow.addView(leftWinner);
                leftSeedRow.addView(leftPlayer);
                rightSeedRow.addView(rightPlayer);
            }
            else {
                leftPlayer.setText(left.getName());
                leftPlayer.setGravity(Gravity.CENTER_HORIZONTAL);
                rightPlayer.setText(right.getName());
                rightPlayer.setGravity(Gravity.CENTER_HORIZONTAL);

                leftWinner.setTag(left.getName());
                rightWinner.setTag(right.getName());

                leftSeedRow.addView(leftWinner);
                leftSeedRow.addView(leftPlayer);
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
                currentGameState.setCurrentPlayers(currentPlayers);
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
        LinearLayout leftLayout = findViewById(R.id.leftLayout);
        LinearLayout rightLayout = findViewById(R.id.rightLayout);
        CheckBox leftBox;
        CheckBox rightBox;
        List<Player> newOrder = new ArrayList<>();
        Intent intent;

        for(int i = 0; i < currentPlayers.size(); i++){
            newOrder.add(null);
        }

        for(List<Player> currentSeed : seeds){
            leftBox = leftLayout.findViewWithTag(currentSeed.get(0).getName());
            rightBox = rightLayout.findViewWithTag(currentSeed.get(1).getName());

            if(leftBox != null){
                if(leftBox.isChecked()){
                    currentSeed.get(0).incrementMatchPoints(3);
                }
            }
            if(rightBox != null){
                if(rightBox.isChecked()){
                    currentSeed.get(1).incrementMatchPoints(3);
                }
            }
        }

        if(currentGameState.getCurrentRound() == (currentPlayers.size() - 1)){
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

        else{
            newOrder.set(0, currentPlayers.get(0));
            for(int i = 1; i < currentPlayers.size(); i++){
                if(i == currentPlayers.size() - 1) {
                    newOrder.set(1, currentPlayers.get(i));
                }
                else{
                    newOrder.set(i + 1, currentPlayers.get(i));
                }
            }
            currentGameState.incrementRound();
            currentGameState.setCurrentPlayers(newOrder);

            Bundle bundle = new Bundle();
            bundle.putSerializable("gameState", currentGameState);
            intent = new Intent(this, RRRoundPairings.class);
            intent.putExtras(bundle);
        }

        startActivity(intent);
        finish();
    }
}
