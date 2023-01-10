package com.example.eduar.mislugares;

public interface Lugares {
    Lugar elemento(int id); //Devolve o elemento dado o seu id.
    void anyade(Lugar lugar); // Adiciona o elemento indicado.
    int nuevo(); // Adiciona um elemento em branco e devolve seu id.
    void borrar(int id); //Elimina o elemento com o id indicado.
    int tamanyo(); //Retorna o número de elementos.
    void actualiza(int id, Lugar lugar); //Substituí o elemento.
    final static String TAG = "MisLugares";
    static GeoPunto posicionActual = new GeoPunto(0,0);
}
