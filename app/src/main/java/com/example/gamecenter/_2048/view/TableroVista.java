package com.example.gamecenter._2048.view;

import android.content.Context;
import android.widget.GridLayout;

public class TableroVista {
    private final GridLayout gridLayout;
    private final Context context;
    private final Block[][] tableroBloques;
    public TableroVista(Context context, GridLayout gridLayout) {
        this.context = context;
        this.gridLayout = gridLayout;
        this.gridLayout.setColumnCount(4);
        this.gridLayout.setRowCount(4);
        this.tableroBloques = new Block[4][4];
    }
    public void inicializarGrid(){}
    public void animarMovimiento(){}
    public void animarSuma(){}
    public void animarGeneracion(){}
}
