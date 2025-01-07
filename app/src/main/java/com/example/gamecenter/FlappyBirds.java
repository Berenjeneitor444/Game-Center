package com.example.gamecenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FlappyBirds extends AppCompatActivity {

    private ImageView pajarraco;
    private boolean juegoIniciado = false;
    private ObjectAnimator gravityAnimator;
    private ObjectAnimator jumpAnimator;
    int posicionFinal;
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
                if (gravityAnimator != null && gravityAnimator.isRunning()) {
                    gravityAnimator.cancel();
                }
                if (jumpAnimator != null && jumpAnimator.isRunning()) {
                    jumpAnimator.cancel();
                }
                salto();
            }
        });

    }



    private void salto(){

        posicionFinal = Resources.getSystem().getDisplayMetrics().heightPixels - pajarraco.getHeight();
        jumpAnimator = ObjectAnimator.ofFloat(pajarraco, "y", pajarraco.getY(), pajarraco.getY() - 200);
        jumpAnimator.setDuration(200);


        jumpAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                gravityAnimator = ObjectAnimator.ofFloat(pajarraco, "y", pajarraco.getY(), posicionFinal);
                gravityAnimator.setDuration(1000);
                gravityAnimator.setInterpolator(new AccelerateInterpolator());
                gravityAnimator.start();
            }
        });
        jumpAnimator.start();
    }
}
