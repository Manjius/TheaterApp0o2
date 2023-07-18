package com.manjiusapps.realtheaterapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import Database.AppDatabase;
import Database.Game;

import java.util.concurrent.Executors;

public class AddGameActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "game-database")
                .fallbackToDestructiveMigration()
                .build();

        Button buttonAddGame = findViewById(R.id.btnAdd);
        Button buttonBack = findViewById(R.id.btnBack);
        Spinner complexitySpinner = findViewById(R.id.complexity_spinner);
        Spinner typeSpinner = findViewById(R.id.spType);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> complexityAdapter = ArrayAdapter.createFromResource(this,
                R.array.complexity_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.game_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        complexityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        complexitySpinner.setAdapter(complexityAdapter);
        typeSpinner.setAdapter(typeAdapter);

        buttonAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = findViewById(R.id.etName);
                EditText descriptionEditText = findViewById(R.id.etDescription);
                EditText sizeEditText = findViewById(R.id.etSize);
                EditText durationPerPersonEditText = findViewById(R.id.etDurationPerPerson);

                String name = nameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String type = typeSpinner.getSelectedItem().toString();
                int sizeNum = Integer.parseInt(sizeEditText.getText().toString());
                int durationPerPersonNum = Integer.parseInt(durationPerPersonEditText.getText().toString());
                String complexityStr = complexitySpinner.getSelectedItem().toString();

                Game game = new Game(name, description, type, sizeNum, durationPerPersonNum, complexityStr);

                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        db.gameDao().insert(game);
                    }
                });

                Toast.makeText(AddGameActivity.this, "Game added", Toast.LENGTH_SHORT).show();
            }
        });

        // Add functionality to the back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
