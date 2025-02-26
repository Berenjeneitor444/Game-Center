package com.example.gamecenter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.UserActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // 2 segundos
    private ImageView ivLogo;
    private TextView tvAppName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Ocultar la barra de acción si está presente
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar vistas
        ivLogo = findViewById(R.id.iv_logo);
        tvAppName = findViewById(R.id.tv_app_name);
        progressBar = findViewById(R.id.progressBar);

        // Iniciar animaciones
        startAnimations();

        // Navegar a la siguiente actividad después del delay
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToNextScreen, SPLASH_DELAY);
    }

    private void startAnimations() {
        // Animación para el logo (escala)
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivLogo, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivLogo, View.SCALE_Y, 0.2f, 1f);

        // Animación para el logo (rotación)
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(ivLogo, View.ROTATION, 0f, 360f);

        // Combinar animaciones del logo
        AnimatorSet logoAnimatorSet = new AnimatorSet();
        logoAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotateAnimator);
        logoAnimatorSet.setDuration(800);
        logoAnimatorSet.setInterpolator(new AnticipateOvershootInterpolator());

        // Animación para el texto
        ObjectAnimator textAlphaAnimator = ObjectAnimator.ofFloat(tvAppName, View.ALPHA, 0f, 1f);
        textAlphaAnimator.setDuration(500);
        textAlphaAnimator.setStartDelay(600);

        // Animación para la barra de progreso
        ObjectAnimator progressBarAlphaAnimator = ObjectAnimator.ofFloat(progressBar, View.ALPHA, 0f, 1f);
        progressBarAlphaAnimator.setDuration(400);
        progressBarAlphaAnimator.setStartDelay(900);

        // Ejecutar todas las animaciones
        AnimatorSet finalAnimatorSet = new AnimatorSet();
        finalAnimatorSet.playTogether(logoAnimatorSet, textAlphaAnimator, progressBarAlphaAnimator);
        finalAnimatorSet.start();
    }

    private void navigateToNextScreen() {
        Intent intent = new Intent(SplashActivity.this, UserActivity.class);
        startActivity(intent);

        // Agregar una transición suave
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Cerrar esta actividad
        finish();
    }
}