package com.example.gamecenter._2048.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Block extends androidx.appcompat.widget.AppCompatTextView {

    private int number;
    private Color color;

    public Block(@NonNull Context context, int number, Color color) {
        super(context);
    }
}
