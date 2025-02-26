package com.example.gamecenter;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ScoreDisplayer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScoreAdapter adapter;
    private Spinner spinnerGames;
    private SwitchCompat switchMyScores;
    private ScoreManager scoreManager;
    private UserDTO currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_displayer);

        // Inicializar vistas
        recyclerView = findViewById(R.id.recycler_scores);
        spinnerGames = findViewById(R.id.spinner_games);
        switchMyScores = findViewById(R.id.switch_my_scores);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Inicializar ScoreManager
        scoreManager = new ScoreManager(this);

        // Simular usuario actual (esto debería venir de sesión/login)
        currentUser = (UserDTO) getIntent().getSerializableExtra("current_user");

        loadGames();
    }

    private void loadGames() {
        List<String> games = new ArrayList<>();
        games.add(GameName.GAME_2048.toString());
        games.add(GameName.FLAPPYBIRDS.toString());
        games.add("Todos");

        Cursor cursor = scoreManager.getScores();
        while (cursor.moveToNext()) {
            String gameName = cursor.getString(0);
            if (!games.contains(gameName)) {
                games.add(gameName);
            }
        }
        cursor.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, games);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGames.setAdapter(spinnerAdapter);

        spinnerGames.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                filterScores();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        switchMyScores.setOnCheckedChangeListener((buttonView, isChecked) -> filterScores());
    }

    private void filterScores() {
        String selectedGame = spinnerGames.getSelectedItem().toString();
        boolean onlyMyScores = switchMyScores.isChecked();

        Cursor cursor;
        if (onlyMyScores && selectedGame.equals("Todos")) {
            cursor = scoreManager.getScores(currentUser);
        } else if (onlyMyScores) {
            cursor = scoreManager.getScores(currentUser, new GameDTO(selectedGame, GameName.valueOf(selectedGame).getDescription()));
        } else if (!selectedGame.equals("Todos")) {
            cursor = scoreManager.getScores(new GameDTO(selectedGame, GameName.valueOf(selectedGame).getDescription()));
        } else {
            cursor = scoreManager.getScores();
        }

        List<ScoreDTO> filteredScores = new ArrayList<>();
        while (cursor.moveToNext()) {
            String gameName = cursor.getString(0);
            int points = cursor.getInt(1);
            int date = cursor.getInt(2);
            filteredScores.add(new ScoreDTO(new GameDTO(selectedGame,
                    GameName.valueOf(gameName).getDescription()), currentUser, points, date));
        }
        cursor.close();

        adapter.updateScores(filteredScores);
    }
}

