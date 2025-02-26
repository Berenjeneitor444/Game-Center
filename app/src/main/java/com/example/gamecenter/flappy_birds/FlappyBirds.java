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
import android.view.View;
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

import com.example.gamecenter.DTO.GameDTO;
import com.example.gamecenter.dbmanagement.GameName;
import com.example.gamecenter.R;
import com.example.gamecenter.DTO.ScoreDTO;
import com.example.gamecenter.dbmanagement.ScoreManager;
import com.example.gamecenter.DTO.UserDTO;

import java.util.Locale;
import java.util.Random;

public class FlappyBirds extends AppCompatActivity implements Runnable {

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
    private int score = 0;
    private UserDTO userPlaying;
    private int chronometer;
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
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.bringToFront();
        TextView chronoText = findViewById(R.id.chronoText);
        chronoText.bringToFront();

        userPlaying = (UserDTO) getIntent().getSerializableExtra("current_user");
        pajarraco = findViewById(R.id.pajarraco);


        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameOver) {
                    return;
                }
                if (!juegoIniciado) {
                    juegoIniciado = true;
                    Thread chronoThread = new Thread(FlappyBirds.this);
                    chronoThread.start();
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
        Brick primerLadrillo = brickGenerator(primeraParteMuro, 0);
        Brick segundoLadrillo = brickGenerator(segundaParteMuro, primeraParteMuro + vacio);
        ConstraintLayout layout = findViewById(R.id.main);
        layout.addView(primerLadrillo, 0);
        layout.addView(segundoLadrillo, 0);

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
                        if (pajarraco.getX() >= segundoLadrillo.getX() + segundoLadrillo.getWidth() &&
                        !segundoLadrillo.isAlredyScored()){
                            score++;
                            TextView scoreText = findViewById(R.id.scoreText);
                            scoreText.setText(getString(R.string.puntuacion, score));
                            segundoLadrillo.setAlredyScored(true);
                        }
                        if (pajarraco.getX() + pajarraco.getWidth() >= segundoLadrillo.getX()) {
                            if (segundoLadrillo.getX() + anchoPared - pajarraco.getX() < 0) {
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

    private Brick brickGenerator(int fraccion, int index) {
        Brick brick = new Brick(this);
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
        // hacerlo mas pequeño
        hitboxPajaro = reduceHitbox(hitboxPajaro, 0.5f);
        return Rect.intersects(hitboxPajaro, hitbox1) || Rect.intersects(hitboxPajaro, hitbox2);
    }

    private void gameOver() {
        gameOver = true;
        handler.removeCallbacks(timerWall);
        // coloco el cartel de game over
        TextView gameOverTextView = findViewById(R.id.game_over);
        gameOverTextView.bringToFront();
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(gameOverTextView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.start();

        // Cambiar la visibilidad a VISIBLE después de la animación para asegurar que se vea
        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                gameOverTextView.setVisibility(View.VISIBLE);
            }
        });
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
        saveScore();

    }

    public Rect reduceHitbox(Rect originalHitbox, float factor) {
        // Calcular el centro del rectángulo original
        int centerX = (originalHitbox.left + originalHitbox.right) / 2;
        int centerY = (originalHitbox.top + originalHitbox.bottom) / 2;

        // Calcular las nuevas dimensiones, ajustadas por el factor
        int newWidth = (int) (originalHitbox.width() * factor);
        int newHeight = (int) (originalHitbox.height() * factor);

        // Calcular las nuevas coordenadas para mantener el centro
        int left = centerX - newWidth / 2;
        int top = centerY - newHeight / 2;
        int right = centerX + newWidth / 2;
        int bottom = centerY + newHeight / 2;

        // Devolver el nuevo rectángulo
        return new Rect(left, top, right, bottom);
    }

    @Override
    public void run() {
        chronometer = 0;
        while (!gameOver) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            chronometer++;

            int finalChronometer = chronometer;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView chronoText = findViewById(R.id.chronoText);
                    chronoText.setText(String.format(Locale.getDefault(), "%02d:%02d", finalChronometer / 60, finalChronometer % 60));
                }
            });
        }
    }

    private void saveScore() {
        ScoreManager scoreManager = new ScoreManager(this);
        scoreManager.addScore(new ScoreDTO(getGameDTO(), userPlaying, score, chronometer));
        scoreManager.close();
    }
    private GameDTO getGameDTO(){
        return new GameDTO(GameName.FLAPPYBIRDS.toString(), "Juego de Flappy Bird");
    }
}
