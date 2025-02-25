package com.example.gamecenter.flappy_birds;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamecenter.R;

import java.util.Random;

public class FlappyBirds extends AppCompatActivity {

    private ImageView pajarraco;
    private boolean juegoIniciado = false;
    private ObjectAnimator gravityAnimator;
    private ObjectAnimator jumpAnimator;
    private int posicionFinal;
    final private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
    final private int fragmentoPantalla = Resources.getSystem().getDisplayMetrics().heightPixels / 10;
    final private int anchoPared = (int) ((float) Resources.getSystem().getDisplayMetrics().widthPixels * 0.16f);
    final private Random random = new Random();
    final private Handler handler = new Handler();
    private boolean gameOver = false;
    final private Runnable timerWall = new Runnable() {
        @Override
        public void run() {
            wallGenerator();
            handler.postDelayed(this, 2000);
        }

    };

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
                if (gameOver) {
                    return;
                }
                if (!juegoIniciado) {
                    juegoIniciado = true;

                    handler.post(timerWall);
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

    @Override
    protected void onResume() {
        super.onResume();
        juegoIniciado = false;
    }


    private void salto() {

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
    private void wallGenerator() {
        // calculo cuantas fracciones de pantalla va a ocupar la primera parte del muro, y con ello calculo el resto
        int primeraParteMuro = random.nextInt(6) + 1;
        int vacio = 3;
        int segundaParteMuro = 10 - primeraParteMuro - vacio;
        ImageView primerLadrillo = brickGenerator(primeraParteMuro, 0);
        ImageView segundoLadrillo = brickGenerator(segundaParteMuro, primeraParteMuro + vacio);
        ConstraintLayout layout = findViewById(R.id.main);
        layout.addView(primerLadrillo);
        layout.addView(segundoLadrillo);
        Rect hitboxPajaro = new Rect();
        Rect hitbox1 = new Rect();
        Rect hitbox2 = new Rect();
        primerLadrillo.animate()
                .x(-anchoPared)
                .setDuration(4000)
                .setInterpolator(null)

                .start();

        segundoLadrillo.animate()
                .x(-anchoPared)
                .setDuration(4000)
                .setInterpolator(null)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeView(primerLadrillo);
                        layout.removeView(segundoLadrillo);
                        segundoLadrillo.destroyDrawingCache();
                        primerLadrillo.destroyDrawingCache();
                    }
                })
                // le asigno un listener que calcule cuando el pajaro este cerca del muro para detectar colisiones

                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        if (pajarraco.getX()+pajarraco.getWidth() >= segundoLadrillo.getX()){
                            if (segundoLadrillo.getX() + anchoPared - pajarraco.getX() < 0){
                                segundoLadrillo.animate().setUpdateListener(null);
                                return;
                            }
                            pajarraco.getGlobalVisibleRect(hitboxPajaro);
                            primerLadrillo.getGlobalVisibleRect(hitbox1);
                            segundoLadrillo.getGlobalVisibleRect(hitbox2);
                            if (colisionDetector(hitboxPajaro, hitbox1, hitbox2)) {
                                Log.d("colision", "colisiono");
                                gameOver();
                            }
                        }
                    }
                })
                .start();

    }

    private ImageView brickGenerator(int fraccion, int index) {
        ImageView brick = new ImageView(this);
        brick.setImageResource(R.drawable.wall);
        brick.setScaleType(ImageView.ScaleType.FIT_XY);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(anchoPared, fraccion * fragmentoPantalla);
        params.leftToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topMargin = index * fragmentoPantalla;
        brick.setX(displayMetrics.widthPixels);
        brick.setLayoutParams(params);
        return brick;
    }

    private boolean colisionDetector(Rect hitboxPajaro, Rect hitbox1, Rect hitbox2) {
        return Rect.intersects(hitboxPajaro, hitbox1) || Rect.intersects(hitboxPajaro, hitbox2);
    }

    private void gameOver() {
        gameOver = true;
        handler.removeCallbacks(timerWall);
        TextView gameOverText = new TextView(this);
        gameOverText.setText(R.string.game_over);
        gameOverText.setTextSize(50);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        gameOverText.setGravity(Gravity.CENTER);
        gameOverText.setLayoutParams(params);
        ConstraintLayout layout = findViewById(R.id.main);

        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof ImageView) {
                view.animate().cancel();
            }
        }
        jumpAnimator.cancel();
        gravityAnimator.start();
        pajarraco.animate()
                .rotation(-180)
                .setDuration(400)
                .start();
        layout.addView(gameOverText);

    }
}
