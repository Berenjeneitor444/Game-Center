package com.example.gamecenter._2048.controller;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

class GameGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        // si ha sido un deslizamiento horizontal
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // si ha sido hacia la derecha
            if (diffX > 0) {
                jugada(new int[]{0,1});
            }
            // si ha sido hacia la izquierda
            else {
                jugada(new int[]{0,-1});
            }
        }
        // si ha sido un deslizamiento vertical
        else {
            // si ha sido hacia arriba
            if (diffY > 0) {
                jugada(new int[]{1,0});
            }
            // si ha sido hacia abajo
            else {
                jugada(new int[]{-1,0});
            }
        }
        return true;
    }
}
