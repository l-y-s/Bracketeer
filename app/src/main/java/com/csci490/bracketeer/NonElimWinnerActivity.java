package com.csci490.bracketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NonElimWinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_elim_winner);
        Intent intent = getIntent();

        TextView first = findViewById(R.id.firstPlaceLabel);
        TextView second = findViewById(R.id.secondPlaceLabel);
        TextView third = findViewById(R.id.thirdPlaceLabel);

        first.setText(intent.getStringExtra("first"));
        second.setText(intent.getStringExtra("second"));
        third.setText(intent.getStringExtra("third"));
    }

    public void exit(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
