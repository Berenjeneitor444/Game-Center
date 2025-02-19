package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamecenter._2048.controller.DosMilCuarentaYOcho;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void changeActivity(View view) {
        Intent intent = null;
        if (view.getId() == R.id.button_2048) {
            intent = new Intent(this, DosMilCuarentaYOcho.class);
        } else if (view.getId() == R.id.button_flappy) {
            intent = new Intent(this, FlappyBirds.class);
        }
        else return;
        startActivity(intent);
    }
}