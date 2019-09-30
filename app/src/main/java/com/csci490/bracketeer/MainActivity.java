package com.csci490.bracketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Called when the new tournament button is pressed
    public void createNewTournament(View view) {
        Intent intent = new Intent(this, NewTournamentActivity.class);
        startActivity(intent);
    }

    public void loadTournament(View view) {
        Toast.makeText(this, "You clicked Load Tournament. This feature will be implemented soon", Toast.LENGTH_SHORT).show();
    }
}
