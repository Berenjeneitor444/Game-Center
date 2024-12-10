package com.example.gamecenter;

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

import java.util.Random;

public class DosMilCuarentaYOcho extends AppCompatActivity {

    private GridLayout grid;
    private final int[][] matrizGrid = new int[4][4];
    private final int[] coordenadasBloque = new int[2];
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
    private int[] generarCoordenadasAleatorias(){
        int[] coordenadas = new int[2];
        coordenadas[0] = radint.nextInt(4);
        coordenadas[1] = radint.nextInt(4);
        while (estaOcupado(coordenadas[0], coordenadas[1])){
            coordenadas[0] = radint.nextInt(4);
            coordenadas[1] = radint.nextInt(4);
        }
        return coordenadas;
    }
    private boolean estaOcupado(int fila, int columna){
        return matrizGrid[fila][columna] != 0;
    }

    public boolean moverBloque(TextView bloque,  int[] direccion){
        // IMPORTANTE CAMBIAR METODO DE RECOGIDA DE COORDENADAS
        boolean seHaMovidoAlgo = false;
        int fila = coordenadasBloque[0];
        int columna = coordenadasBloque[1];
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
        coordenadasBloque[0] = filaNueva;
        coordenadasBloque[1] = columnaNueva;
        return seHaMovidoAlgo;
    }
    public void jugada(int[] direccion) {
        boolean seHaMovidoAlgo = false;

        // mover los bloques
        for (int i = 7; i < grid.getChildCount(); i++){
            if (moverBloque((TextView) grid.getChildAt(i), direccion)){
                seHaMovidoAlgo = true;
            }
        }

        // genero otro bloque
        if (seHaMovidoAlgo){
            int[] coordenadasNuevas = generarCoordenadasAleatorias();
            generarBloque(coordenadasNuevas[0], coordenadasNuevas[1], radint.nextBoolean() ? 2 : 4);
        }
        // refresco la vista del grid
        grid.invalidate();
        grid.requestLayout();

    }
// SEGUIR MAÑANA EN ESTE METODO
    public void generarBloque(int fila, int columna, int numero) {
        // crear bloque
        TextView bloque = new TextView(this);
        bloque.setText(String.valueOf(numero));
            if(numero == 2){
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.dos));
            }
            else if (numero == 4){
                bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.cuatro));
            }

        bloque.setTextColor(ContextCompat.getColor(this, R.color.numero_oscuro));
        bloque.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        bloque.setGravity(Gravity.CENTER);
        bloque.setTypeface(null, Typeface.BOLD);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // darle margen
        final int margen = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.5f, metrics));


        // colocarlo

            // especifico la fila y la columna
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(fila);
        params.columnSpec = GridLayout.spec(columna);
        params.setMargins(0,0,margen, margen);



        // agregarlo al grid
        grid.addView(bloque, params);
        // actualizar matriz para tenerlo localizado
        matrizGrid[fila][columna] = numero;
        coordenadasBloque[0] = fila;
        coordenadasBloque[1] = columna;

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
        int [] coordenadas = generarCoordenadasAleatorias();
        generarBloque(coordenadas[0], coordenadas[1], radint.nextBoolean() ? 2 : 4);
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

