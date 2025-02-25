package com.example.gamecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {
    private final SQLiteDatabase db;

    public UserManager(Context context) {
        MyOpenHelper helper = new MyOpenHelper(context);
        db = helper.getWritableDatabase();
        helper.close();

    }
    public boolean addUser(String username, String password) {
        try {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            db.insert("users", null, values);
            return true;
        }
        catch (Exception e){
            // si es invalido
            return false;
        }
    }
    public void deleteUser(String username) {
        db.execSQL("DELETE FROM users WHERE username = ?", new Object[]{username});
    }
    public boolean checkUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }
    public void close(){
        db.close();
    }

}
