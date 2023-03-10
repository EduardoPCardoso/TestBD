package com.example.eduar.mislugares;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdaptadorLugares extends RecyclerView.Adapter<AdaptadorLugares.ViewHolder> {

    protected Lugares lugares; //Lugares a mostrar
    protected LayoutInflater inflador; //Crea Layouts a partir del XML
    protected Context contexto; //Lo necessitamos para el inflador


    //Para expandir da lista RecyclerView
    protected View.OnClickListener onClickListener;

    //Para expandir da lista recycler view
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AdaptadorLugares(Context contexto, Lugares lugares) {
        this.contexto = contexto;
        this.lugares = lugares;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, direccion;
        public ImageView foto;
        public RatingBar valoracion;
        public TextView distancia;

        public ViewHolder (View itemView){
            super(itemView);
            distancia = (TextView)itemView.findViewById(R.id.distancia);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            direccion = (TextView) itemView.findViewById(R.id.direccion);
            foto = (ImageView) itemView.findViewById(R.id.foto);
            valoracion = (RatingBar) itemView.findViewById(R.id.valoracion);
        }
    }

    // Creamos el ViewHolder con las vistas de um elemento sin personalizar

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_lista, null);
        //Para expandir da lista RecyclerView
        v.setOnClickListener(onClickListener);
        return new ViewHolder (v);
    }

    //Usando como base el ViewHolder y los personalizamos

    @Override
    public void onBindViewHolder (ViewHolder holder, int posicion){
        Lugar lugar = lugares.elemento(posicion);
        personalizaVista(holder, lugar);
    }

    //Personalizamos um ViewHolder a partir de un lugar

    public void personalizaVista(ViewHolder holder, Lugar lugar){
        holder.nombre.setText(lugar.getNombre());
        holder.direccion.setText(lugar.getDireccion());
        int id = R.drawable.otros;
        if (Lugares.posicionActual != null && lugar.getPosicion()!=null){
            int d=(int)Lugares.posicionActual.distancia(lugar.getPosicion());
            if(d<2000){
                holder.distancia.setText(d+" m");}
                else {
                holder.distancia.setText(d/1000 + " Km");
            }
        }
        switch (lugar.getTipo()){
            case RESTAURANTE:id = R.drawable.restaurante; break;
            case BAR:id = R.drawable.bar; break;
            case COPAS:id = R.drawable.copas; break;
            case ESPETACULO:id = R.drawable.espectaculos; break;
            case HOTEL:id = R.drawable.hotel; break;
            case COMPRAS:id = R.drawable.compras; break;
            case EDUCACION:id = R.drawable.educacion; break;
            case DEPORTE:id = R.drawable.deporte; break;
            case NATUREZA:id = R.drawable.naturaleza; break;
            case GASOLINERA:id = R.drawable.gasolinera; break;
        }
        holder.foto.setImageResource(id);
        holder.foto.setScaleType(ImageView.ScaleType.FIT_END);
        holder.valoracion.setRating(lugar.getValoracion());
    }

    //Indicamos el n??mero de elementos de lista
    @Override
    public int getItemCount(){
        return lugares.tamanyo();
    }

}
