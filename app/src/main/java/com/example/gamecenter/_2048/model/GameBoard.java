//package com.example.gamecenter._2048.model;
//
//import android.graphics.Point;
//import android.widget.GridLayout;
//
//public class GameBoard {
//    private final int[][] grid;
//    private final int size;
//
//    public GameBoard(int size) {
//        this.size = size;
//        this.grid = new int[size][size];
//        // initialize board
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                grid[i][j] = 0;
//            }
//        }
//    }
//    public int getValueAt(int x, int y){
//        return grid[y][x];
//    }
//    public void setValueAt(int x, int y, int value){
//        if (estaOcupado(x, y)) {
//            grid[y][x] = value;
//        }
//    }
//    public boolean estaOcupado(int x, int y){
//        return grid[y][x] == 0;
//    }
//    public boolean estaLleno(){
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (grid[i][j] == 0) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    public MovementTransferDTO mover(Point coordenadasBloque, Direccion direccion){
//        int numeroBloque = getValueAt(coordenadasBloque.x, coordenadasBloque.y);
//        int x = coordenadasBloque.x;
//        int y = coordenadasBloque.y;
//        boolean seHaMovidoAlgo = false;
//        boolean esSumable = false;
//        // creamos uno nuevo donde trabajaremos, conservando el original
//        Point nuevaPosicion = new Point(x, y);
//        int destination = direccion.isReverse() ? 0 : 3;
//        if (direccion == Direccion.NORTE || direccion == Direccion.SUR) {
//                while (nuevaPosicion.y != destination) {
//                    nuevaPosicion.y += direccion.getDy();
//                    if(estaOcupado(nuevaPosicion.x, nuevaPosicion.y)) {
//                        int numeroBloquePasivo = getValueAt(nuevaPosicion.x, nuevaPosicion.y);
//                        if (numeroBloquePasivo == numeroBloque) {
//                            esSumable = true;
//                        }
//                        return new MovementTransferDTO(nuevaPosicion, new Point(nuevaPosicion.x, nuevaPosicion.y + direccion.getDy()), MovementType.getMovementType(seHaMovidoAlgo, esSumable), numeroBloquePasivo);
//                    }
//
//            }
//        }
//
//        return MovementTransferDTO();
//    }
//    public MovementTransferDTO sumarBloques(Point BloqueActivo,Point BloquePasivo, Direccion direccion){
//
//    }
//}
