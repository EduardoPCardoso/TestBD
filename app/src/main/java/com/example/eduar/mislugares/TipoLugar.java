package com.example.eduar.mislugares;

import android.util.Log;

import static com.example.eduar.mislugares.Lugares.TAG;

public enum TipoLugar {
    OTROS("Otros",R.drawable.otros),
    RESTAURANTE("Restaurante",R.drawable.restaurante),
    BAR("Bar",R.drawable.bar),
    COPAS("Copas",R.drawable.copas),
    ESPETACULO("Espetaculo",R.drawable.espectaculos),
    HOTEL("Hotel",R.drawable.hotel),
    COMPRAS("Compras",R.drawable.compras),
    EDUCACION("Educacion",R.drawable.educacion),
    DEPORTE("Deporte",R.drawable.deporte),
    NATUREZA("Natureza",R.drawable.naturaleza),
    GASOLINERA("Gasolinera",R.drawable.gasolinera);

    private final String texto;
    private final int recurso;

    TipoLugar(String texto, int recurso){
        this.texto=texto;
        this.recurso=recurso;
    }

    public static String[] getNombres() {
        String[] resultado = new String[TipoLugar.values().length];
        for (TipoLugar tipo : TipoLugar.values()){
            resultado[tipo.ordinal()] = tipo.texto;
        }
        return resultado;
    }

    public String getTexto(){
        return texto;
    }
    public int getRecurso(){
        return recurso;
    }
}
