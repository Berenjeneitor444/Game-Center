package com.example.gamecenter.dbmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gamecenter.DTO.GameDTO;
import com.example.gamecenter.DTO.ScoreDTO;
import com.example.gamecenter.DTO.UserDTO;

public class ScoreManager {
    private final SQLiteDatabase writableDB;
    private final MyOpenHelper helper;

    public ScoreManager(Context context) {
        helper = new MyOpenHelper(context);
        writableDB = helper.getWritableDatabase();
    }
    // devuelve un cursor con los scores con su usuario y juego
    public void addScore(ScoreDTO scoreDTO) {
        String queryUserId = "SELECT id FROM users WHERE username = ?";
        Cursor cursorUserId = writableDB.rawQuery(queryUserId, new String[]{scoreDTO.getPlayer().getUserName()});
        cursorUserId.moveToFirst();
        int userId = cursorUserId.getInt(0);
        cursorUserId.close();

        String queryGameId = "SELECT id FROM games WHERE name = ?";
        Cursor cursorGameId = writableDB.rawQuery(queryGameId, new String[]{scoreDTO.getGame().getName()});
        cursorGameId.moveToFirst();
        int gameId = cursorGameId.getInt(0);
        cursorGameId.close();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("game_id", gameId);
        values.put("score", scoreDTO.getPoints());
        values.put("time", scoreDTO.getTime());
        values.put("date", String.valueOf(System.currentTimeMillis()));
        writableDB.insert("scores", null, values);
    }
    // polimorfismo para obtener los scores filtrando o sin filtrar
    public Cursor getScores() {
        String query = "SELECT games.name, scores.score, scores.date, users.username " +
                "FROM scores " +
                "JOIN users ON scores.user_id = users.id " +
                "JOIN games ON scores.game_id = games.id " +
                "ORDER BY scores.score DESC";

        return writableDB.rawQuery(query, new String[]{});
    }

    public Cursor getScores(UserDTO user, GameDTO game) {
        String gameName = game.getName();
        String userName = user.getUserName();

        String query = "SELECT games.name, scores.score, scores.date, users.username " +
                "FROM scores " +
                "JOIN users ON scores.user_id = users.id " +
                "JOIN games ON scores.game_id = games.id " +
                "WHERE users.username = ? AND games.name = ?" +
                "ORDER BY scores.score DESC";

        return writableDB.rawQuery(query, new String[]{userName, gameName});
    }
    public Cursor getScores(GameDTO game) {
        String gameName = game.getName();

        String query = "SELECT games.name, scores.score, scores.date, users.username " +
                "FROM scores " +
                "JOIN users ON scores.user_id = users.id " +
                "JOIN games ON scores.game_id = games.id " +
                "WHERE games.name = ?" +
                "ORDER BY scores.score DESC";

        return writableDB.rawQuery(query, new String[]{gameName});
    }
    public Cursor getScores(UserDTO user) {
        String userName = user.getUserName();

        String query = "SELECT games.name, scores.score, scores.date, users.username " +
                "FROM scores " +
                "JOIN users ON scores.user_id = users.id " +
                "JOIN games ON scores.game_id = games.id " +
                "WHERE users.username = ? " +
                "ORDER BY scores.score DESC";

        return writableDB.rawQuery(query, new String[]{userName});
    }

    public void close() {
        writableDB.close();
        helper.close();
    }
}