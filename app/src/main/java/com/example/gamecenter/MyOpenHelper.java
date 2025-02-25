package com.example.gamecenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "game_center.db";
    private static final int DATABASE_VERSION = 1;
    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tablas
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";

        String createGamesTable = "CREATE TABLE IF NOT EXISTS games (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE, " +
                "description TEXT)";

        String createScoresTable = "CREATE TABLE IF NOT EXISTS scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "game_id INTEGER NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "time INTEGER NOT NULL, " +
                "date TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(game_id) REFERENCES games(id) ON DELETE CASCADE)";

        db.execSQL(createUsersTable);
        db.execSQL(createGamesTable);
        db.execSQL(createScoresTable);

        // Harcodear datos de los juegos:

        String insertGames = "INSERT INTO games (name, description) VALUES " +
                "('2048', 'Juego de 2048'), " +
                "('Flappy Bird', 'Juego de Flappy Bird')";

        db.execSQL(insertGames);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS games");
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }
}
