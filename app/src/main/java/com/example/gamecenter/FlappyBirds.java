package com.example.gamecenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class FlappyBirds extends AppCompatActivity {

    private ImageView pajarraco;
    private boolean juegoIniciado = false;
    private ObjectAnimator gravityAnimator;
    private ObjectAnimator jumpAnimator;
    int posicionFinal;
    final int fragmentoPantalla = Resources.getSystem().getDisplayMetrics().heightPixels / 10;
    final int anchoPared = (int) ((float)Resources.getSystem().getDisplayMetrics().widthPixels * 0.16f);
    final Random random = new Random();
    final private Handler handler = new Handler();

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
                if (!juegoIniciado) {
                    juegoIniciado = true;
                    Runnable TimerWall = new Runnable() {
                        @Override
                        public void run() {
                            wallGenerator();
                            handler.postDelayed(this, 2000);
                        }

                    };
                    handler.post(TimerWall);
                }
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
    // lograr generar espacios vacios del 30% de la altura jugable en sitios aleatorios
    // divido el espacio jugable en 10 partes iguales, el tamaño de esta parte lo calculo
    // segun la dimension del dispositivo
    private void wallGenerator(){
        // calculo cuantas fracciones de pantalla va a ocupar la primera parte del muro, y con ello calculo el resto
        int primeraParteMuro = random.nextInt(6) + 1;
        int vacio = 3;
        int segundaParteMuro = 10 - primeraParteMuro - vacio;
        ImageView primerLadrillo = brickGenerator(primeraParteMuro,0);
        ImageView segundoLadrillo = brickGenerator(segundaParteMuro,primeraParteMuro + vacio);
        ConstraintLayout layout = findViewById(R.id.main);
        layout.addView(primerLadrillo);
        layout.addView(segundoLadrillo);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(primerLadrillo, "x", Resources.getSystem().getDisplayMetrics().widthPixels, 0f - anchoPared);
        animator1.setDuration(4000);
        animator1.setInterpolator(null);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(segundoLadrillo, "x", Resources.getSystem().getDisplayMetrics().widthPixels, 0f - anchoPared);
        animator2.setDuration(4000);
        animator2.setInterpolator(null);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.start();

    }
    private ImageView brickGenerator(int fraccion, int index){
        ImageView brick = new ImageView(this);
        brick.setImageResource(R.drawable.wall);
        brick.setScaleType(ImageView.ScaleType.FIT_XY);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(anchoPared, fraccion * fragmentoPantalla);
        params.leftToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topMargin = index * fragmentoPantalla;
        brick.setLayoutParams(params);
        return brick;
    }
}
