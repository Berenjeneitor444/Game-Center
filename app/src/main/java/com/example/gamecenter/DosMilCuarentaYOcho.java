package com.example.gamecenter;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Random;

public class DosMilCuarentaYOcho extends AppCompatActivity {

    private GridLayout grid;
    private final int[][] matrizGrid = new int[4][4];
    private final HashMap<Point, TextView> mapaBloques = new HashMap<>();
    private final Random radint = new Random();
    private GestureDetector gestureDetector;

    private void inicializarGrid(){
        grid = findViewById(R.id.grid_sobrepuesto);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int proporcion = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 74.375f, metrics));
        final int margen = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.5f, metrics));
        // Agrega una fila de Space views en la fila 0
        for (int i = 0; i < grid.getColumnCount(); i++) {
            Space space = new Space(getApplicationContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(0);
            params.columnSpec = GridLayout.spec(i);
            params.height = proporcion;
            params.width = proporcion;
            params.setMargins(0,0,margen,margen);
            grid.addView(space, params);
        }

        // Agrega una columna de Space views en la columna 0
        for (int i = 1; i < grid.getRowCount(); i++) {
            Space space = new Space(getApplicationContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(i);
            params.columnSpec = GridLayout.spec(0);
            params.width = proporcion;
            params.height = proporcion;
            params.setMargins(0,0,margen,margen);
            grid.addView(space, params);
        }
    }
    private Point generarCoordenadasAleatorias(){
        Point coordenadas = new Point();
        do {
            coordenadas.set(radint.nextInt(4),radint.nextInt(4));
        } while (estaOcupado(coordenadas.y, coordenadas.x));
        return coordenadas;
    }
    private boolean estaOcupado(int fila, int columna){
        return matrizGrid[fila][columna] != 0;
    }
    private void sumarBloques(Point posicionBloqueActivo, Point posicionBloquePasivo){
        TextView bloqueActivo = mapaBloques.get(posicionBloqueActivo);
        TextView bloquePasivo = mapaBloques.get(posicionBloquePasivo);
        int numeroBloqueFinal = Integer.parseInt(bloquePasivo.getText().toString() + bloqueActivo.getText().toString());
        grid.removeView(bloquePasivo);
        grid.removeView(bloqueActivo);
        mapaBloques.remove(posicionBloqueActivo);
        mapaBloques.remove(posicionBloquePasivo);
        matrizGrid[posicionBloqueActivo.y][posicionBloqueActivo.x] = 0;
        matrizGrid[posicionBloquePasivo.y][posicionBloquePasivo.x] = 0;
        generarBloque(posicionBloquePasivo, numeroBloqueFinal);
    }
    public boolean moverBloque(Point coordenadasBloque,  int[] direccion){
        boolean seHaMovidoAlgo = false;



        int fila = coordenadasBloque.y;
        int columna = coordenadasBloque.x;
        TextView bloque = mapaBloques.get(coordenadasBloque);
        // crear point sobre el que trabajaremos, conservando el original
        Point coordenadasNuevas = new Point(coordenadasBloque);

        int filaNueva = fila;
        int columnaNueva = columna;
        // si la fila no cambia, asumo que se va a mover horizontalmente
        if (direccion[0] == 0){

            // se mueve a la derecha
            if (direccion[1] > 0){
                for (int i = columna + 1; i < 4; i++){
                    // si ya está ocupado sal del bucle y te quedas con la coordenada anterior
                    if (estaOcupado(fila, i)){

                        break;
                    }
                    else {
                        columnaNueva = i;
                        seHaMovidoAlgo = true;
                    }
                }
            }
            // se mueve a la izquierda
            if (direccion[1] < 0){
                for (int i = columna - 1; i >= 0; i--){
                    if (estaOcupado(fila, i)){
                        break;
                    }
                    else {
                        columnaNueva = i;
                        seHaMovidoAlgo = true;
                    }
                }
            }

        }
        // si la columna no cambia, asumo que se va a mover verticalmente
        if (direccion[1] == 0){
            // se mueve hacia abajo
            if (direccion[0] > 0){
                for (int i = fila + 1; i < 4; i++){
                    if (estaOcupado(i, columna)){
                        break;
                    }
                    else {
                        filaNueva = i;
                        seHaMovidoAlgo = true;
                    }
                }
            }
                // hacia arriba
                if (direccion[0] < 0){
                    for (int i = fila - 1; i >= 0; i--){
                        if (estaOcupado(i, columna)){
                            break;
                        }
                        else {
                            filaNueva = i;
                            seHaMovidoAlgo = true;
                        }
                    }
                }
            }
        if (!seHaMovidoAlgo){
            return seHaMovidoAlgo;
        }
        // crear sus nuevos parametros de posicionamiento nuevo

        GridLayout.LayoutParams params = (GridLayout.LayoutParams)bloque.getLayoutParams();
        params.rowSpec = GridLayout.spec(filaNueva);
        params.columnSpec = GridLayout.spec(columnaNueva);
        // los seteo
        bloque.setLayoutParams(params);
        // actualizo la matriz
        matrizGrid[filaNueva][columnaNueva] = matrizGrid[fila][columna];
        matrizGrid[fila][columna] = 0;

        // actualizo las coordenadas
        coordenadasNuevas.x = columnaNueva;
        coordenadasNuevas.y = filaNueva;
        // actualizo el mapa de bloques
        mapaBloques.remove(coordenadasBloque);
        mapaBloques.put(coordenadasNuevas, bloque);
        return seHaMovidoAlgo;
    }
    public void jugada(int[] direccion) {
        boolean seHaMovidoAlgo = false;

        int movimientoY = direccion[0];
        int movimientoX = direccion[1];
    // necesito adaptar la jugada al tipo de movimiento para que funcione correctamente

        // si el movimiento es hacia arriba o hacia la izquierda
        if (movimientoY == -1 || movimientoX == -1){
            for (int Y = 0; Y < 4; Y++){
                for (int X = 0; X < 4; X++) {
                    if (matrizGrid[Y][X] != 0){
                        Point coordenadasBloque = new Point(X,Y);
                        if (moverBloque(coordenadasBloque, direccion)){
                            seHaMovidoAlgo = true;
                        }
                    }

                }
            }
        }
        // si el movimiento es hacia abajo o hacia la derecha
        else {
            for (int Y = 3; Y > -1; Y--){
                for (int X = 3; X > -1; X--) {
                    if (matrizGrid[Y][X] != 0){
                        Point coordenadasBloque = new Point(X, Y);
                        if (moverBloque(coordenadasBloque, direccion)){
                            seHaMovidoAlgo = true;
                        }
                    }

                }
            }
        }


        // genero otro bloque
        if (seHaMovidoAlgo){
            Point coordenadasNuevas = generarCoordenadasAleatorias();
            generarBloque(coordenadasNuevas, radint.nextBoolean() ? 2 : 4);
        }
        // refresco la vista del grid
        grid.invalidate();
        grid.requestLayout();

    }
// SEGUIR MAÑANA EN ESTE METODO
    public void generarBloque(Point coordenadas, int numero) {
        // crear bloque
        TextView bloque = new TextView(this);
        bloque.setText(String.valueOf(numero));
        switch (numero){
            case 2:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.dos));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_oscuro));
                break;
            case 4:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.cuatro));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_oscuro));
                break;
            case 8:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.ocho));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 16:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.dieciseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;

                case 32:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.treintaydos));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;

                case 64:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.sesentaycuatro));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 128:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.cientoveintiocho));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 256:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.doscientoscincuentayseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 512:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.quinientosdieciseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 1024:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.doscientoscincuentayseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 2048:
                bloque.setBackgroundColor(ContextCompat.getColor(this, R.color.dosmilcuarentayocho));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;

        }


        bloque.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        bloque.setGravity(Gravity.CENTER);
        bloque.setTypeface(null, Typeface.BOLD);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // darle margen
        final int margen = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.5f, metrics));


        // colocarlo

            // especifico la fila y la columna
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(coordenadas.y);
        params.columnSpec = GridLayout.spec(coordenadas.x);
        params.setMargins(0,0,margen, margen);



        // agregarlo al grid
        grid.addView(bloque, params);
        // actualizar matriz para tenerlo localizado
        matrizGrid[coordenadas.y][coordenadas.x] = numero;
        // actualizar mapa de bloques
        mapaBloques.put(coordenadas, bloque);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dosmilcuarentayocho);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // inicializo el grid
        inicializarGrid();
        // genero el primer bloque
        Point coordenadas = generarCoordenadasAleatorias();
        generarBloque(coordenadas, radint.nextBoolean() ? 2 : 4);
        // inicializo el detector de gestos
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
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
        });
        findViewById(R.id.grid).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               return gestureDetector.onTouchEvent(motionEvent);
            }
        });

    }
}

