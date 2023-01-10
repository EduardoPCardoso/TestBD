package com.example.eduar.mislugares;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Date;

public class EdicionLugarActivity extends AppCompatActivity {

    private long id;
    private long _id;
    private Lugar lugar;
    private Uri uriFoto;
    private ImageView imageView;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = (ImageView) findViewById(R.id.foto);
        setContentView(R.layout.edicion_lugar);
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id"); //Adicionado o valor default. Se der problema, manter apenas a chave.
        _id = extras.getLong("_id");
        if (_id != -1) {
            lugar = MainActivity.lugares.elemento((int) _id);
        } else {
            lugar = SelectorFragment.adaptador.lugarPosicion((int) id);
        }
        lugar = MainActivity.lugares.elemento((int) id);
        nombre = (EditText) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        direccion = (EditText) findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());
        telefono = (EditText) findViewById(R.id.telefono);
        telefono.setText(Integer.toString(lugar.getTelefono()));
        lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
        url = (EditText) findViewById(R.id.url);
        url.setText(lugar.getUrl());
        comentario = (EditText) findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());
        tipo = (Spinner) findViewById(R.id.tipo); //Não está absorvendo valores do tipo. está vindo NULL
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edicion_lugar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int _id = SelectorFragment.adaptador.idPosicion((int) id);
        MainActivity.lugares.actualiza(_id, lugar);
        SelectorFragment.adaptador.setCursor(MainActivity.lugares.extraeCursor());
        SelectorFragment.adaptador.notifyDataSetChanged();

        if (id == R.id.gravar) {
            lugar.setNombre(nombre.getText().toString());
            lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
            lugar.setDireccion(direccion.getText().toString());
            lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
            lugar.setUrl(url.getText().toString());
            lugar.setComentario(comentario.getText().toString());
            MainActivity.lugares.actualiza((int) id, lugar);
            if (_id == -1) {
                _id = SelectorFragment.adaptador.idPosicion((int) id);
            }
            MainActivity.lugares.actualiza((int) _id, lugar);
            SelectorFragment.adaptador.setCursor(MainActivity.lugares.extraeCursor());
            if (id != -1){
                SelectorFragment.adaptador.notifyItemChanged((int) id);
            } else {
                SelectorFragment.adaptador.notifyDataSetChanged();
            }
            finish();
            return true;
        }

        if (id == R.id.cancelar){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
