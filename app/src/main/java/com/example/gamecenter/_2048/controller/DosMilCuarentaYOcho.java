package com.example.gamecenter._2048.controller;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import com.example.gamecenter.R;

import java.util.HashMap;
import java.util.Random;

public class DosMilCuarentaYOcho extends AppCompatActivity {

    private GridLayout grid;
    private int tamañoCelda;
    private int tamañoMargenesCelda;
    private int gridCellHeight;
    private final int[][] matrizGrid = new int[4][4];
    private final HashMap<Point, TextView> mapaBloques = new HashMap<>();
    private final Random radint = new Random();
    private GestureDetector gestureDetector;

    private void inicializarGrid(){
        grid = findViewById(R.id.grid_sobrepuesto);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        tamañoCelda = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 74.375f, metrics));
        tamañoMargenesCelda = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.5f, metrics));
        // Agrega una fila de Space views en la fila 0
        for (int i = 0; i < grid.getColumnCount(); i++) {
            Space space = new Space(getApplicationContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(0);
            params.columnSpec = GridLayout.spec(i);
            params.height = tamañoCelda;
            params.width = tamañoCelda;
            params.setMargins(0,0,tamañoMargenesCelda,tamañoMargenesCelda);
            grid.addView(space, params);
        }

        // Agrega una columna de Space views en la columna 0
        for (int i = 1; i < grid.getRowCount(); i++) {
            Space space = new Space(getApplicationContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(i);
            params.columnSpec = GridLayout.spec(0);
            params.width = tamañoCelda;
            params.height = tamañoCelda;
            params.setMargins(0,0,tamañoMargenesCelda,tamañoMargenesCelda);
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
    private boolean sumarBloques(Point posicionBloqueActivo, Point posicionBloquePasivo){
        TextView bloqueActivo = mapaBloques.get(posicionBloqueActivo);
        TextView bloquePasivo = mapaBloques.get(posicionBloquePasivo);
        assert bloqueActivo != null;
        assert bloquePasivo != null;
        int numeroPasivo = Integer.parseInt(bloquePasivo.getText().toString());
        int numeroActivo = Integer.parseInt(bloqueActivo.getText().toString());
        // si no es igual devuelve false
        if(numeroActivo != numeroPasivo){
            return false;
        }
        // si es igual, lo suma
        int numeroBloqueFinal = numeroActivo + numeroPasivo;
        grid.removeView(bloquePasivo);
        grid.removeView(bloqueActivo);
        mapaBloques.remove(posicionBloqueActivo);
        mapaBloques.remove(posicionBloquePasivo);
        matrizGrid[posicionBloqueActivo.y][posicionBloqueActivo.x] = 0;
        matrizGrid[posicionBloquePasivo.y][posicionBloquePasivo.x] = 0;
        generarBloque(posicionBloquePasivo, numeroBloqueFinal);
        return true;
    }
    public boolean moverBloque(Point coordenadasBloque,  int[] direccion){
        boolean seHaMovidoAlgo = false;
        Boolean movimientoHorizontal = null;
        int fila = coordenadasBloque.y;
        int columna = coordenadasBloque.x;
        TextView bloque = mapaBloques.get(coordenadasBloque);
        // crear point sobre el que trabajaremos, conservando el original
        Point coordenadasNuevas = new Point(coordenadasBloque);

        int filaNueva = fila;
        int columnaNueva = columna;
        // si la fila no cambia, asumo que se va a mover horizontalmente
        if (direccion[0] == 0){
            movimientoHorizontal = true;
            // se mueve a la derecha
            if (direccion[1] > 0){
                for (int i = columna + 1; i < 4; i++){
                    // si ya está ocupado comprueba si se pueden sumar, si se pueden sumar sale del metodo
                    // si no continua con el metodo
                    if (estaOcupado(fila, i)){
                        if (sumarBloques(coordenadasBloque, new Point(i, filaNueva))){
                            seHaMovidoAlgo = true;
                            return seHaMovidoAlgo;
                        }
                        else{
                            break;
                        }
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
                        if (sumarBloques(coordenadasBloque, new Point(i, filaNueva))){
                            seHaMovidoAlgo = true;
                            return seHaMovidoAlgo;
                        }
                        else {
                            break;
                        }
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
            movimientoHorizontal = false;
            // se mueve hacia abajo
            if (direccion[0] > 0){
                for (int i = fila + 1; i < 4; i++){
                    if (estaOcupado(i, columna)){
                        if (sumarBloques(coordenadasBloque, new Point(columnaNueva, i))){
                            seHaMovidoAlgo = true;
                            return seHaMovidoAlgo;
                        }
                        else{
                            break;
                        }
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
                            if(sumarBloques(coordenadasBloque, new Point(columnaNueva, i))){
                                seHaMovidoAlgo = true;
                                return seHaMovidoAlgo;
                            }
                            else {
                                break;
                            }
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

        assert bloque != null;
        // --------------- ANIMACION ---------------------------------
        if (movimientoHorizontal){
            float deltaX = columnaNueva * tamañoCelda - columna * tamañoCelda;

        }
        // --------------- FIN ANIMACION -----------------------------
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
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.ocho));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 16:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.dieciseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;

                case 32:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.treintaydos));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;

                case 64:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.sesentaycuatro));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 128:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.cientoveintiocho));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 256:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.doscientoscincuentayseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 512:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.quinientosdieciseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 1024:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.doscientoscincuentayseis));
                bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_blanco));
                break;
                case 2048:
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.dosmilcuarentayocho));
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

        // ------------------ANIMACION------------------------

        bloque.setScaleX(0f);
        bloque.setScaleY(0f);
        bloque.animate().scaleX(1f).scaleY(1f).setDuration(400).start();

        // -------------------FIN ANIMACION-------------------

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

