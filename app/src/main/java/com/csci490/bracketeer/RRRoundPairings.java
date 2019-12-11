package com.csci490.bracketeer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
        setContentView(R.layout.activity_non_elim_round_pairings);
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
                for(int i = 0; i < topRow.size(); i++){
                    currentPlayers.set(i, topRow.get(i));
                }
                for(int i = 0; i < bottomRow.size(); i++){
                    int reverseIndex = bottomRow.size() - 1 - i;
                    currentPlayers.set((i + bottomRow.size()), bottomRow.get(reverseIndex));
                }
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

    }

    public void end(View view){

    }
}
