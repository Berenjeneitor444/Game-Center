package com.example.gamecenter._2048.model;

public enum Direccion {
    NORTE(0, -1, false),
    SUR(0, 1, true),
    OESTE(1, 0, false),
    ESTE(-1, 0, true);

    private final int dx;
    private final int dy;
    private final boolean reverse;

    Direccion(int dx, int dy, boolean reverse) {
        this.dx = dx;
        this.dy = dy;
        this.reverse = reverse;
    }

    public boolean isReverse() {
        return reverse;
    }


    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
