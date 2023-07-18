package com.manjiusapps.realtheaterapp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import Database.AppDatabase;
import Database.Game;
import Database.GameDao;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final boolean DEBUG_MODE = true;
    private static final String TAG = "MainActivity";

    private AppDatabase db;
    private EditText etPlayers, etWarmupTime, etGameTime;
    private String selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "game-database")
                .fallbackToDestructiveMigration()
                .build();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.clearAllTables();
                loadGamesFromJson();
            }
        });

        etPlayers = findViewById(R.id.editNumberOfPlayers);
        etWarmupTime = findViewById(R.id.editWarmupTime);
        etGameTime = findViewById(R.id.editGamesTime);
        Spinner spinner = findViewById(R.id.spinnerStyle);
        selectedType = String.valueOf(spinner.getSelectedItem());

        Button btnGenerateWorkshop = findViewById(R.id.btnGenerateWorkshop);
        CheckBox checkBoxRepeatGamesx2 = findViewById(R.id.checkBoxRepeatGamesx2);
        CheckBox checkBoxRepeatGamesx3 = findViewById(R.id.checkBoxRepeatGamesx3);

        btnGenerateWorkshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int players = 0;
                int warmupTime = 0;
                int gameTime = 0;
                try {
                    players = Integer.parseInt(etPlayers.getText().toString());
                    warmupTime = Integer.parseInt(etWarmupTime.getText().toString());
                    gameTime = Integer.parseInt(etGameTime.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid input. Please enter valid numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (players < 2 || players > 20) {
                    Toast.makeText(MainActivity.this, "Number of players should be between 2 and 20", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (warmupTime < 0 || warmupTime > 120) {
                    Toast.makeText(MainActivity.this, "Warmup time should be between 0 and 120 minutes", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gameTime < 0 || gameTime > 120) {
                    Toast.makeText(MainActivity.this, "Game time should be between 0 and 120 minutes", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean repeatGamesx2 = checkBoxRepeatGamesx2.isChecked();
                boolean repeatGamesx3 = checkBoxRepeatGamesx3.isChecked();

                if(repeatGamesx2 && repeatGamesx3) {
                    Toast.makeText(MainActivity.this, "Cannot select both Repeat Games x2 and x3 options", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, WorkshopActivity.class);
                intent.putExtra("players", players);
                intent.putExtra("warmupTime", warmupTime);
                intent.putExtra("gameTime", gameTime);
                intent.putExtra("type", selectedType);
                intent.putExtra("repeatGamesx2", repeatGamesx2);
                intent.putExtra("repeatGamesx3", repeatGamesx3);
                startActivity(intent);

            }
        });

        Button btnAddGame = findViewById(R.id.btnAddGame);
        btnAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadGamesFromJson() {
        String jsonFileString = getJsonFromAssets("games.json");
        if (jsonFileString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonFileString);
                GameDao gameDao = db.gameDao();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Game game = new Game();
                    game.setName(jsonObject.getString("name"));
                    game.setDescription(jsonObject.getString("description"));
                    game.setType(jsonObject.getString("type"));
                    game.setSize(jsonObject.getInt("size"));
                    game.setDurationPerPerson(jsonObject.getInt("durationPerPerson"));
                    game.setComplexity(jsonObject.getString("complexity"));

                    // Log before insertion
                    if(DEBUG_MODE) {
                        Log.d(TAG, "Inserting game: " + game.getName());
                    }

                    gameDao.insert(game);

                    // Log after insertion
                    if(DEBUG_MODE) {
                        Log.d(TAG, "Inserted game: " + game.getName());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getJsonFromAssets(String fileName) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
