package com.example.gamecenter._2048.model;

public enum MovementType {
    MOVER(true, false),
    SUMAR(false, true),
    AMBOS(true, true);

    private final boolean seHaMovidoAlgo;
    private final boolean esSumable;

    MovementType(boolean seHaMovidoAlgo, boolean esSumable) {
        this.seHaMovidoAlgo = seHaMovidoAlgo;
        this.esSumable = esSumable;
    }

    public boolean isSeHaMovidoAlgo() {
        return seHaMovidoAlgo;
    }

    public boolean isEsSumable() {
        return esSumable;
    }
    public static MovementType getMovementType(boolean seHaMovidoAlgo, boolean esSumable) {
        if (seHaMovidoAlgo && esSumable) {
            return AMBOS;
        } else if (seHaMovidoAlgo) {
            return MOVER;
        } else if (esSumable) {
            return SUMAR;
        }
        else {
            throw new RuntimeException("No se ha podido determinar el tipo de movimiento");
        }
    }
}
