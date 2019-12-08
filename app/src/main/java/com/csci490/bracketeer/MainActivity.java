package com.csci490.bracketeer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
        final File dirFiles = getBaseContext().getFilesDir();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select tournament to load:");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setPadding(2,2,2,2);

        for(String file : dirFiles.list()){
            final TextView fileName = new TextView(this);
            fileName.setText(file);
            fileName.setTextSize(18);
            fileName.setPadding(20,20,20,20);

            fileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    File inFile = new File(dirFiles, (String)fileName.getText());
                    try {
                        FileInputStream fis = new FileInputStream(inFile);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        GameState loadedTournament = (GameState) ois.readObject();
                        ois.close();
                        fis.close();
                        inFile.delete();

                        loadedTournament.setLoaded();
                        String mode = loadedTournament.getMode();
                        Intent intent = null;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("gameState", loadedTournament);

                        switch(mode) {

                            case "Single-Elimination":
                                intent = new Intent(getBaseContext(), SingleElimRoundPairings.class);
                                break;

                            case "Double-Elimination":
                                intent = new Intent(getBaseContext(), DoubleElimRoundPairings.class);
                                break;

                            case "Swiss":
                                intent = new Intent(getBaseContext(), SwissRoundPairings.class);
                                break;

                            case "Round-Robin":
                                intent = new Intent(getBaseContext(), RRRoundPairings.class);
                                break;
                        }

                        intent.putExtras(bundle);
                        startActivity(intent);

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            layout.addView(fileName);
        }

        builder.setView(layout);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
