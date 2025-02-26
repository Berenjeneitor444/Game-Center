package com.example.gamecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private List<ScoreDTO> scores;

    public ScoreAdapter(List<ScoreDTO> scores) {
        this.scores = scores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        ScoreDTO score = scores.get(position);
        holder.tvPlayer.setText(score.getPlayer().getUserName());
        holder.tvGame.setText(score.getGame().getName());
        holder.tvPoints.setText(String.valueOf(score.getPoints()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public void updateScores(List<ScoreDTO> newScores) {
        this.scores = newScores;
        notifyDataSetChanged();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayer, tvGame, tvPoints;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayer = itemView.findViewById(R.id.tv_player);
            tvGame = itemView.findViewById(R.id.tv_game);
            tvPoints = itemView.findViewById(R.id.tv_points);
        }
    }
}
