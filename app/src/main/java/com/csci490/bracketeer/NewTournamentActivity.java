package com.csci490.bracketeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public void updateList(View view) {
        EditText playerField = findViewById(R.id.playerInput);
        String playerName = playerField.getText().toString();
        if(playerName.length() > 0) {
            playerList.add(playerName);
            adapter.notifyDataSetChanged();
            playerField.getText().clear();
        }
    }

    public void startGame(View view) {
        Spinner modeSpinner = findViewById(R.id.typeList);
        newGame = new GameState(playerList, modeSpinner.getSelectedItem().toString());

        Intent intent = new Intent(this, RoundPairings.class);
        intent.putExtra("gameState", newGame);
        startActivity(intent);
    }
}
