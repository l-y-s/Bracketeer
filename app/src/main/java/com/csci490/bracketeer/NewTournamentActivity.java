package com.csci490.bracketeer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class NewTournamentActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    ArrayList<String> playerList = new ArrayList<>();
    GameState newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tournament);

        RecyclerView recyclerView = findViewById(R.id.playerListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, playerList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void onItemClick(View view, int position) {
        final int itemPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove player?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerList.remove(adapter.getItem(itemPosition));
                adapter.notifyDataSetChanged();
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

    public void updateList(View view) {
        EditText playerField = findViewById(R.id.playerInput);
        String playerName = playerField.getText().toString();
        if(playerName.length() > 0) {
            playerList.add(playerName);
            adapter.notifyDataSetChanged();
            playerField.getText().clear();
        }
        else {
            Toast.makeText(this, "Please enter a player name", Toast.LENGTH_SHORT).show();
        }
    }

    public void startGame(View view) {
        Intent intent = null;
        Spinner modeSpinner = findViewById(R.id.typeList);
        String mode = modeSpinner.getSelectedItem().toString();
        newGame = new GameState(playerList, mode);

        switch(mode) {

            case "Single-Elimination":
                intent = new Intent(this, SingleElimRoundPairings.class);
                break;

            case "Double-Elimination":
                intent = new Intent(this, DoubleElimRoundPairings.class);
                break;

            case "Swiss":
                intent = new Intent(this, SwissRoundPairings.class);
                break;

            case "Round-Robin":
                intent = new Intent(this, RRRoundPairings.class);
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("gameState", newGame);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
