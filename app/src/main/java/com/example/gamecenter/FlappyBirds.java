package com.example.gamecenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.dynamicanimation.animation.FlingAnimation;

public class FlappyBirds extends AppCompatActivity {

    private ImageView pajarraco;
    private boolean juegoIniciado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flappy_birds);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pajarraco = findViewById(R.id.pajarraco);
        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!juegoIniciado){
                    empezarPartida();
                    juegoIniciado = true;
                }
            }
        });
    }
    private void empezarPartida(){
        FlingAnimation fling = new FlingAnimation(pajarraco, FlingAnimation.TRANSLATION_Y);
        fling.setStartVelocity(1000);
        fling.setFriction(1);
        fling.start();
    }
}
