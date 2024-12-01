package com.example.gamecenter;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DosMilCuarentaYOcho extends AppCompatActivity {

    private GridLayout grid;

    public void crearGridSobrepuesto(Context context){
        grid = new GridLayout(context);
        grid.setColumnCount(4);
        grid.setRowCount(4);

// seguir generando grid sobrepuesto, asignarlo al atributo y ponerlo en el layout, con sus atributos de layout, luego generar bloques de numeros y terminar por el movimiento. hasta mañána UWU

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

    }
}