package com.example.eduar.mislugares;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectorFragment extends Fragment {

    private RecyclerView recyclerView;
    public static AdaptadorLugaresBD adaptador;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState){
        View vista = inflador.inflate(R.layout.fragment_selector,contenedor,false);
        recyclerView = (RecyclerView)vista.findViewById(R.id.recycler_view);
        return vista;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(final Bundle state){
        super.onActivityCreated(state);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);//Retirar se der problema.
        adaptador = new AdaptadorLugaresBD(getContext(),MainActivity.lugares,MainActivity.lugares.extraeCursor());
        adaptador.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                ((MainActivity)getActivity()).muestraLugar(recyclerView.getChildAdapterPosition(v));
                /*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id",(long)recyclerView.getChildAdapterPosition(v));
                startActivity(i);*/
            }
        });
        recyclerView.setAdapter(adaptador);
    }
}
