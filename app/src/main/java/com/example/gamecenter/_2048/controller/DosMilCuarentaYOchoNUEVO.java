//package com.example.gamecenter._2048.controller;
//
//import android.graphics.Point;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.gamecenter._2048.model.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DosMilCuarentaYOchoNUEVO extends AppCompatActivity {
//    private final List<MovementTransferDTO> registroMovimientos = new ArrayList<>();
//    private final GameBoard gameBoard = new GameBoard(4);
//    public void aplicarMovimiento(MovementTransferDTO dto){}
//    public void generarBloque(Point coordenadas, int numero){
//    }
//    public void aplicarJugada(MovementTransferDTO[] arrayDTO) {
//        for (MovementTransferDTO dto : arrayDTO) {
//            aplicarMovimiento(dto);
//        }
//    }
//    public Point generarCoordenadasAleatorias(){
//        Point coordenadas = new Point();
//        coordenadas.x = (int) (Math.random() * 4);
//        coordenadas.y = (int) (Math.random() * 4);
//        return coordenadas;
//    }
//    public void jugada(Direccion direccion) {
//        boolean seHaMovidoAlgo = false;
//
//        int[] sequence = direccion.isReverse() ?
//                new int[]{3, 2, 1, 0} : new int[]{0, 1, 2, 3};
//
//        for (int i : sequence) {
//            for (int j : sequence) {
//                Point current = new Point(j, i);
//                if (gameBoard.estaOcupado(current.x, current.y)) {
//                    MovementTransferDTO dto = gameBoard.mover(current, direccion);
//                    if (dto != null) {
//                        seHaMovidoAlgo = true;
//                        registroMovimientos.add(dto);
//                    }
//                }
//                if (seHaMovidoAlgo) {
//                    aplicarJugada(registroMovimientos.toArray(new MovementTransferDTO[0]));
//                    registroMovimientos.clear();
//                    generarBloque(generarCoordenadasAleatorias(), 2);
//                }
//            }
//        }
//    }
//}
