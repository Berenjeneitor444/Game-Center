package com.example.gamecenter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

public class DosMilCuarentaYOcho extends AppCompatActivity {

    private GridLayout grid;
    private final int[][] matrizGrid = new int[4][4];
    private final int[] coordenadasBloque = new int[2];

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
    // CONTINUAR CON LOGICA MAÑANA
    public void moverBloque(TextView bloque, final int DIRECCION){
        int fila = coordenadasBloque[0];
        int columna = coordenadasBloque[1];
        int filaNueva = fila + DIRECCION;
        int columnaNueva = columna + DIRECCION;
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
    }
    public void jugada(final int DIRECCION) {
        boolean seHaMovidoAlgo = false;


        // vuelve si esta out of bounds
//        if (filaNueva < 0 || filaNueva >= 4 || columnaNueva < 0 || columnaNueva >= 4) {
//            return;
//        }
        // mover los bloques
        for (int i = 7; i < grid.getChildCount(); i++){
            moverBloque((TextView) grid.getChildAt(i), DIRECCION);
        }


        // refresco la vista del grid
        grid.invalidate();
        grid.requestLayout();

    }

    public void generarBloque(int fila, int columna, int numero) {
        // si no está ocupada la posicion indicada, genero y coloco un bloque dentro
        if (matrizGrid[fila][columna] == 0) {
            // crear bloque
            TextView bloque = new TextView(this);
            bloque.setText(String.valueOf(numero));
            bloque.setBackground(ContextCompat.getDrawable(this, R.drawable.dos));
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
        inicializarGrid();

        generarBloque(0,0,2);
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
                        jugada(0);
                    }
                    // si ha sido hacia la izquierda
                    else {
                        jugada(1);
                    }
                }
                // si ha sido un deslizamiento vertical
                else {
                    // si ha sido hacia arriba
                    if (diffY > 0) {
                        jugada(2);
                    }
                    // si ha sido hacia abajo
                    else {
                        jugada(3);
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

