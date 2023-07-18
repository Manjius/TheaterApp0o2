package com.manjiusapps.realtheaterapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.Random;

import Database.AppDatabase;
import Database.Game;

public class WorkshopActivity extends AppCompatActivity {

    private static final boolean DEBUG_MODE = true;
    private static final String TAG = "WorkshopActivity";

    private AppDatabase db;
    private RecyclerView warmupGamesRecyclerView;
    private RecyclerView mainGamesRecyclerView;
    private GameAdapter warmupGamesAdapter;
    private GameAdapter mainGamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);

        warmupGamesRecyclerView = findViewById(R.id.warmupGamesRecyclerView);
        mainGamesRecyclerView = findViewById(R.id.mainGamesRecyclerView);

        // Initialize the adapters with empty lists
        warmupGamesAdapter = new GameAdapter(new ArrayList<>(), game -> {
            Intent intent = new Intent(WorkshopActivity.this, GameDetailActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
        });

        mainGamesAdapter = new GameAdapter(new ArrayList<>(), game -> {
            Intent intent = new Intent(WorkshopActivity.this, GameDetailActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
        });

        warmupGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        warmupGamesRecyclerView.setAdapter(warmupGamesAdapter);

        mainGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainGamesRecyclerView.setAdapter(mainGamesAdapter);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "game-database").build();

        Executors.newSingleThreadExecutor().execute(() -> {
            int players = getIntent().getIntExtra("players", 0);
            int warmupTime = getIntent().getIntExtra("warmupTime", 0);
            int gameTime = getIntent().getIntExtra("gameTime", 0);
            String selectedType = getIntent().getStringExtra("type");

            boolean repeatGamesx2 = getIntent().getBooleanExtra("repeatGamesx2", false);
            boolean repeatGamesx3 = getIntent().getBooleanExtra("repeatGamesx3", false);

            int multiplier = 1;
            if (repeatGamesx2) {
                multiplier = 2;
            } else if (repeatGamesx3) {
                multiplier = 3;
            }

            List<Game> warmupGamesList = db.gameDao().getAllWarmupGames(selectedType, players);
            List<Game> warmupGames = new ArrayList<>();
            int totalWarmupTime = 0;

            while (!warmupGamesList.isEmpty() && totalWarmupTime < warmupTime) {
                int randomIndex = new Random().nextInt(warmupGamesList.size());
                Game game = warmupGamesList.get(randomIndex);
                int gameDuration = game.getDurationPerPerson() * game.getSize();

                if (totalWarmupTime + gameDuration <= warmupTime) {
                    totalWarmupTime += gameDuration;
                    warmupGames.add(game);
                }
                warmupGamesList.remove(randomIndex);
            }

            List<Game> mainGamesList = db.gameDao().getAllMainGames(selectedType, players);
            List<Game> mainGames = new ArrayList<>();
            int totalMainTime = 0;

            while (!mainGamesList.isEmpty() && totalMainTime < gameTime) {
                int randomIndex = new Random().nextInt(mainGamesList.size());
                Game game = mainGamesList.get(randomIndex);
                multiplier = 1;
                if (repeatGamesx2) {
                    multiplier = 2;
                } else if (repeatGamesx3) {
                    multiplier = 3;
                }
                int gameDuration = game.getDurationPerPerson() * game.getSize() * multiplier;

                if (totalMainTime + gameDuration <= gameTime) {
                    totalMainTime += gameDuration;
                    mainGames.add(game);
                }
                mainGamesList.remove(randomIndex);
            }

            runOnUiThread(() -> {
                warmupGamesAdapter.setGames(warmupGames);
                warmupGamesAdapter.notifyDataSetChanged();
                mainGamesAdapter.setGames(mainGames);
                mainGamesAdapter.notifyDataSetChanged();
            });
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());
    }
}
