package com.example.gamecenter.dbmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gamecenter.DTO.UserDTO;

public class UserManager {
    private final SQLiteDatabase db;
    private final MyOpenHelper helper;

    public UserManager(Context context) {
        helper = new MyOpenHelper(context);
        db = helper.getWritableDatabase();

    }
    public boolean addUser(UserDTO userDTO) {
        ContentValues values = new ContentValues();
        values.put("username", userDTO.getUserName());
        values.put("password", userDTO.getPasswd());
        long a = db.insert("users", null, values);
        if (a == -1) {
            return false;
        }
        return true;

    }
    public boolean checkUser(UserDTO userDTO) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userDTO.getUserName(), userDTO.getPasswd()});

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }
    public void close(){
        db.close();
        helper.close();
    }

}
