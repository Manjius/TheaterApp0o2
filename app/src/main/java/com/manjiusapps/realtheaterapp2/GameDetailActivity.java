package com.manjiusapps.realtheaterapp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.manjiusapps.realtheaterapp2.R;

import Database.Game;


public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        TextView gameNameTextView = findViewById(R.id.gameNameTextView);
        TextView gameDescriptionTextView = findViewById(R.id.gameDescriptionTextView);

        Intent intent = getIntent();
        Game game = (Game) intent.getSerializableExtra("game");

        gameNameTextView.setText(game.getName());
        gameDescriptionTextView.setText(game.getDescription());

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());
    }
}
