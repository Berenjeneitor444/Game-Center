package com.example.gamecenter.app_interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamecenter.DTO.UserDTO;
import com.example.gamecenter.R;
import com.example.gamecenter._2048.DosMilCuarentaYOcho;
import com.example.gamecenter.flappy_birds.FlappyBirds;

public class Menu extends AppCompatActivity {
    private UserDTO currentUser;

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
        currentUser = (UserDTO) getIntent().getSerializableExtra("current_user");
    }

    public void changeActivity(View view) {
        Intent intent = null;
        if (view.getId() == R.id.button_2048) {
            intent = new Intent(this, DosMilCuarentaYOcho.class);
        } else if (view.getId() == R.id.button_flappy) {
            intent = new Intent(this, FlappyBirds.class);
        }
        else if (view.getId() == R.id.button_scores) intent = new Intent(this, ScoreDisplayer.class);
        else return;
        intent.putExtra("current_user", currentUser);
        startActivity(intent);
    }
}