package com.example.gamecenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GameManager {
    private final SQLiteDatabase db;
    private final MyOpenHelper helper;
    public GameManager(Context context) {
        helper = new MyOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public GameDTO getGame(String gameName) {
        String query = "SELECT * FROM games WHERE name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{gameName});
        cursor.moveToFirst();
        GameDTO gameDTO = new GameDTO(cursor.getString(0), cursor.getString(1));
        cursor.close();
        return gameDTO;
    }
    public void close(){
        db.close();
        helper.close();
    }

}
