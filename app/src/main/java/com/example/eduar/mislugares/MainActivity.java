package com.example.eduar.mislugares;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/*import android.widget.Button;*/

public class MainActivity extends AppCompatActivity implements LocationListener{
    /*private Button bAcercade;*/
    public static LugaresBD lugares;
    //private RecyclerView recyclerView;
    //public static AdaptadorLugaresBD adaptador;
    //private RecyclerView.LayoutManager layoutManager;
    private LocationManager manejador;
    private Location mejorLocaliz;
    static final int RESULTADO_PREFERENCIAS = 0;
    private VistaLugarFragment fragmentVista;


    //Declarando arquivo de audio (.mid, de menor tamanho)

    /*@Override protected void onSaveInstanceState(Bundle estadoGuardado){
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null){
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }*/

    /*@Override protected void onRestoreInstanceState(Bundle estadoGuardado){
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null){
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lugares = new LugaresBD(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentVista = (VistaLugarFragment) getSupportFragmentManager().findFragmentById(R.id.vista_lugar_fragment);
        //Para verificar métodos do ciclo de vida da atividade (Didatico)
        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT);

        /*mp = MediaPlayer.create(this,R.raw.audio);
        mp.start();*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            long _id = lugares.nuevo();
            Intent i = new Intent(MainActivity.this, EdicionLugarActivity.class);
            i.putExtra("_id", _id);
            startActivity(i);
            }
        });

        /*recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        adaptador = new AdaptadorLugaresBD(this,lugares, lugares.extraeCursor());
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Para expandir da Recyclerview
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, VistaLugarActivity.class);
                i.putExtra("id", (long) recyclerView.getChildAdapterPosition(v));
                startActivity(i);
            }
        });*/

        manejador = (LocationManager)getSystemService(LOCATION_SERVICE);
        ultimaLocalizazion();



    }

    public void muestraLugar(long id){
        if (fragmentVista != null){
            fragmentVista.actualizarVistas(id);
        } else {
            Intent intent = new Intent(this, VistaLugarActivity.class);
            intent.putExtra("id",id);
            startActivityForResult(intent,0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        if(id == R.id.menu_buscar){
            lanzarVistaLugar(null);
            return true;
        }

        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }

        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }

        if (id == R.id.menu_mapa){
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);

    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercadeActivtity.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view){
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivityForResult(i, RESULTADO_PREFERENCIAS);
    }

    public void lanzarVistaLugar(View view){
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        new AlertDialog.Builder(this)
                .setTitle("Seleção do lugar")
                .setMessage("Indicar a id:")
                .setView(entrada)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                        long id = Long.parseLong(entrada.getText().toString());
                        Intent i = new Intent(MainActivity.this, VistaLugarActivity.class);
                        i.putExtra("id", id);
                        startActivity(i);
                    }})
                .setNegativeButton("Cancelar",null)
                .show();
    }

    void ultimaLocalizazion(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
            if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        }
        else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,"Sem a sua permissão não posso mostrar a sua localização"+
            " aos lugares.", SOLICITUDE_PERMISO_LOCALIZATION, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == SOLICITUDE_PERMISO_LOCALIZATION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                ultimaLocalizazion();
                activarProveedores();
                //adaptador.notifyDataSetChanged();
            }
        }
    }

    private static final int SOLICITUDE_PERMISO_LOCALIZATION = 0;

    public static void solicitarPermiso(final String permiso, String justificacion,
                                        final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }})
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        activarProveedores();
        /*
        if (fragmentVista != null && SelectorFragment.adaptador.getItemCount()>0){
            fragmentVista.actualizarVistas(0);
        }*/
    }

    public void activarProveedores() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20*1000, 5, this);
            }
            if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10*1000, 10, this);
            }
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sem a sua permissão, não posso mostrar a sua localização" + " aos lugares.", SOLICITUDE_PERMISO_LOCALIZATION, this);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            manejador.removeUpdates(this);
        }
    }

    @Override public void onLocationChanged(Location location){
        Log.d(Lugares.TAG,"Nova localização: "+location);
        actualizaMejorLocaliz(location);
        //adaptador.notifyDataSetChanged();
    }

    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras){
        Log.d(Lugares.TAG, "Mudar estado: "+proveedor);
        activarProveedores();
    }
    @Override
    public void onProviderEnabled(String proveedor) {
        Log.d(Lugares.TAG, "Se desabilita: "+proveedor);
        activarProveedores();
    }

    @Override
    public void onProviderDisabled(String proveedor) {
        Log.d(Lugares.TAG, "Se desabilita: "+proveedor);
        activarProveedores();
    }

    private static final long DOS_MINUTOS = 2*60*1000;
    private void actualizaMejorLocaliz(Location localiz){
        if (localiz != null && (mejorLocaliz == null
        || localiz.getAccuracy()<2*mejorLocaliz.getAccuracy()
        || localiz.getTime()-mejorLocaliz.getTime()>DOS_MINUTOS)){
            Log.d(Lugares.TAG, "Nova melhor localização");
            mejorLocaliz = localiz;
            Lugares.posicionActual.setLatitud(localiz.getLatitude());
            Lugares.posicionActual.setLongitud(localiz.getLongitude());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == RESULTADO_PREFERENCIAS){
            SelectorFragment.adaptador.setCursor(MainActivity.lugares.extraeCursor());
            SelectorFragment.adaptador.notifyDataSetChanged();
        }
    }
}

