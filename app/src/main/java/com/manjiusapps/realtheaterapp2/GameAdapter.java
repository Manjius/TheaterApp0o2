package com.manjiusapps.realtheaterapp2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.function.Consumer;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Database.Game;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private List<Game> gameList;
    private Consumer<Game> onGameClickListener;

    public GameAdapter(List<Game> gameList, Consumer<Game> onGameClickListener) {
        this.gameList = gameList;
        this.onGameClickListener = onGameClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new ViewHolder(v, onGameClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(gameList.get(position));
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    // Add this in your GameAdapter class

    public void setGames(List<Game> games) {
        this.gameList = games;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView gameNameTextView;
        private Consumer<Game> onGameClickListener;

        public ViewHolder(View itemView, Consumer<Game> onGameClickListener) {
            super(itemView);
            gameNameTextView = itemView.findViewById(R.id.gameNameTextView);
            this.onGameClickListener = onGameClickListener;
        }

        public void bind(Game game) {
            gameNameTextView.setText(game.getName());
            itemView.setOnClickListener(v -> onGameClickListener.accept(game));
        }
    }
}
