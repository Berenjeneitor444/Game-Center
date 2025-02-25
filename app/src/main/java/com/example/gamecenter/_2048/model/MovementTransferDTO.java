package com.example.gamecenter._2048.model;

import android.graphics.Point;

public class MovementTransferDTO {
    private Point origen;
    private Point destino;
    private MovementType movementType;
    private int valorSiSumable;

    public MovementTransferDTO(Point origen, Point destino, MovementType movementType, Integer valorSiSumable) {
        this.origen = origen;
        this.destino = destino;
        this.movementType = movementType;
        this.valorSiSumable = valorSiSumable;
    }

    public Point getOrigen() {
        return origen;
    }

    public Point getDestino() {
        return destino;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public int getValorSiSumable() {
        return valorSiSumable;
    }

    public void setValorSiSumable(int valorSiSumable) {
        this.valorSiSumable = valorSiSumable;
    }
}
