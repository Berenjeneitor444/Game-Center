package com.example.gamecenter._2048;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gamecenter.MyOpenHelper;

public class ScoreManager {
    private final SQLiteDatabase writableDB;
    private final MyOpenHelper helper;

    public ScoreManager(Context context) {
        helper = new MyOpenHelper(context);
        writableDB = helper.getWritableDatabase();
        helper.close();
    }

    public void addScore(String username, String gameName, int score, int time) {
        String queryUserId = "SELECT id FROM users WHERE username = ?";
        Cursor cursorUserId = writableDB.rawQuery(queryUserId, new String[]{username});
        cursorUserId.moveToFirst();
        int userId = cursorUserId.getInt(0);
        cursorUserId.close();

        String queryGameId = "SELECT id FROM games WHERE name = ?";
        Cursor cursorGameId = writableDB.rawQuery(queryGameId, new String[]{gameName});
        cursorGameId.moveToFirst();
        int gameId = cursorGameId.getInt(0);
        cursorGameId.close();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("game_id", gameId);
        values.put("score", score);
        values.put("time", time);
        values.put("date", String.valueOf(System.currentTimeMillis()));
        writableDB.insert("scores", null, values);
    }

    public Cursor getScores(String username) {
        String query = "SELECT games.name, scores.score, scores.date " +
                "FROM scores " +
                "JOIN users ON scores.user_id = users.id " +
                "JOIN games ON scores.game_id = games.id " +
                "WHERE users.username = ? " +
                "ORDER BY scores.date DESC";

        return writableDB.rawQuery(query, new String[]{username});
    }

    public void close() {
        writableDB.close();
        helper.close();
    }
}