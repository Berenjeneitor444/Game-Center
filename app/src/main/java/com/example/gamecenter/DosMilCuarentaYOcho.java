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

    public void jugada(int[] cambioPosicion) {
        int fila = coordenadasBloque[0];
        int columna = coordenadasBloque[1];
        int filaNueva = fila + cambioPosicion[0];
        int columnaNueva = columna + cambioPosicion[1];
        // vuelve si esta out of bounds
        if (filaNueva < 0 || filaNueva >= 4 || columnaNueva < 0 || columnaNueva >= 4) {
            return;
        }
        // obtener el bloque
        TextView bloque = (TextView) grid.getChildAt(0);
        // crear sus nuevos parametros de posicionamiento nuevo
        GridLayout.LayoutParams params = (GridLayout.LayoutParams)bloque.getLayoutParams();
        params.rowSpec = GridLayout.spec(filaNueva);
        params.columnSpec = GridLayout.spec(columnaNueva);
        Log.d("fila", String.valueOf(filaNueva));
        Log.d("columna", String.valueOf(columnaNueva));
        Log.d("bolque", bloque.getText().toString());
        // los seteo
        bloque.setLayoutParams(params);
        // refresco la vista del grid
        grid.invalidate();
        grid.requestLayout();
        // actualizo la matriz
        matrizGrid[filaNueva][columnaNueva] = matrizGrid[fila][columna];
        matrizGrid[fila][columna] = 0;

        // actualizo las coordenadas
        coordenadasBloque[0] = filaNueva;
        coordenadasBloque[1] = columnaNueva;

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
            int margen = (int) (10.5f * metrics.density + 0.5f);


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

        grid = findViewById(R.id.grid_sobrepuesto);

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
                Handler handler = new Handler();

                // si ha sido un deslizamiento horizontal
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    // si ha sido hacia la derecha
                    if (diffX > 0) {
                        handler.postDelayed(() -> jugada(new int[]{0, 1}), 200); // Movimiento hacia la derecha
                    }
                    // si ha sido hacia la izquierda
                    else {
                        handler.postDelayed(() -> jugada(new int[]{0, -1}), 200); // Movimiento hacia la izquierda
                    }
                }
                // si ha sido un deslizamiento vertical
                else {
                    // si ha sido hacia arriba
                    if (diffY > 0) {
                        handler.postDelayed(() -> jugada(new int[]{1, 0}), 200); // Movimiento hacia arriba
                    }
                    // si ha sido hacia abajo
                    else {
                        handler.postDelayed(() -> jugada(new int[]{-1, 0}), 200); // Movimiento hacia abajo
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

