package com.example.gamecenter.flappy_birds;

import android.content.Context;
import android.widget.ImageView;

public class Brick extends androidx.appcompat.widget.AppCompatImageView {
    private boolean alredyScored = false;
    public Brick(Context context) {
        super(context);
    }

    public void setAlredyScored(boolean alredyScored) {
        this.alredyScored = alredyScored;
    }

    public boolean isAlredyScored() {
        return alredyScored;
    }
}
