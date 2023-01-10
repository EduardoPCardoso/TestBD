package com.example.eduar.mislugares;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

    public class VistaLugarFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
            DatePickerDialog.OnDateSetListener{

        private TextView tipo;
        private long id;
        private Lugar lugar;
        private Uri uriFoto;
        final static int RESULTADO_EDITAR = 1;
        private View v;
        private ImageView imageView;
        private RecyclerView recyclerView;
        public static AdaptadorLugaresBD adaptador;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstaceState) {
        View vista = inflador.inflate(R.layout.vista_lugar, contenedor, false);
        setHasOptionsMenu(true);

        LinearLayout pUrl = (LinearLayout) vista.findViewById(R.id.barra_url);
        pUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgWeb(null);
            }
        });

        LinearLayout pDireccion = (LinearLayout) vista.findViewById(R.id.pdireccion);
        pDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verMapa(null);
            }
        });

        LinearLayout pTelefono = (LinearLayout) vista.findViewById(R.id.ptelefono);
        pTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaTelefono(null);
            }
        });

        LinearLayout iconoHora = (LinearLayout) vista.findViewById(R.id.icono_hora);
        iconoHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarHora();
            }
        });

        LinearLayout iconoFecha = (LinearLayout) vista.findViewById(R.id.iconoFecha);
        iconoFecha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFecha();
            }
        });

        return vista;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        v = getView();
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("id", -1);
            if (id != -1) {
                actualizarVistas(id);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, lugar.getNombre() + " - " + lugar.getUrl());
                startActivityForResult(intent, RESULTADO_EDITAR);
                return true;
            case R.id.accion_llegar:
                verMapa(null);
                return true;
            case R.id.accion_editar:
                Intent i = new Intent(getActivity(), EdicionLugarActivity.class);
                i.putExtra("id", id);
                startActivity(i);
                return true;
            case R.id.accion_borrar:
                MainActivity.lugares.borrar((int) id);
                getActivity().finish();
                /*
                SelectorFragment selectorFragment= (SelectorFragment) getActivity().
                        getSupportFragmentManager().findFragmentById(R.id.selector_fragment);
                if (selectorFragment == null){
                    getActivity().finish();
                } else {
                    ((MainActivity)getActivity()).muestraLugar(0);
                }
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void verMapa(View view) {
        Uri uri;
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        if (lat != 0 || lon != 0) {
            uri = Uri.parse("geo:" + lat + "," + lon);
        } else {
            uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void llamadaTelefono(View v) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.getTelefono())));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void pgWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrl())));
    }

    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;
    //TESTE, LINHA ABAIXO
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void galeria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULTADO_GALERIA);
    }

    public void cambiarHora(){
        DialogoSelectorHora dialogo = new DialogoSelectorHora();
        dialogo.setOnTimeSetListener(this);
        Bundle args = new Bundle();
        args.putLong("fecha", lugar.getFecha());
        dialogo.setArguments(args);
        dialogo.show(getActivity().getSupportFragmentManager(),"selectorHora");
    }

    public void cambiarFecha(){//Metodo com erro.
        /*DialogoSelectorFecha dialogo = new DialogoSelectorFecha();
        dialogo.setOnDateSetListener(this);
        Bundle args = new Bundle();
        args.putLong("fecha", lugar.getFecha());
        dialogo.setArguments(args);
        dialogo.show(getActivity().getSupportFragmentManager(),"selectorFecha");*/
    }

    @Override
    public void onDateSet(DatePicker view, int anyo, int mes, int dia){
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(lugar.getFecha());
        calendario.set(Calendar.YEAR, anyo);
        calendario.set(Calendar.MONTH, mes);
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        lugar.setFecha(calendario.getTimeInMillis());
        actualizarVistas(id);//Verificar se dá erro.
        TextView tFecha = (TextView)getView().findViewById(R.id.fecha);
        DateFormat formato = DateFormat.getDateInstance();
        tFecha.setText(formato.format(new Date(lugar.getFecha())));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_EDITAR) {
            actualizarVistas(id);
            v.findViewById(R.id.scrollView1).invalidate();
        } else if (requestCode == RESULTADO_GALERIA) {
            if (resultCode == Activity.RESULT_OK) {
                lugar.setFoto(data.getDataString());
                ponerFoto(imageView, lugar.getFoto());
            } else {
                Toast.makeText(getActivity(), "Foto não carregada", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_FOTO) {
            if (resultCode == Activity.RESULT_OK && lugar != null && uriFoto != null) {
                lugar.setFoto(uriFoto.toString());
                ponerFoto(imageView, lugar.getFoto());
            } else {
                Toast.makeText(getActivity(), "Erro na captura", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void ponerFoto(ImageView imageView, String uri) {
        if (uri != null) {
            imageView.setImageBitmap(reduceBitmap(getActivity(), uri, 1024, 1024));
        } else {
            imageView.setImageBitmap(null);
        }
    }

    public void tomarFoto(View view) {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //uriFoto = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator
        //        + "img_" + (System.currentTimeMillis() / 1000) + ".jpg"));
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
    }

    public void eliminarFoto(View view) {
        /*lugar.setFoto(null);
        ponerFoto(imageView, null);*/
    }

    public static Bitmap reduceBitmap(Context context, String uri, int maxAncho, int maxAlto) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(uri)), null, options);
            options.inSampleSize = (int) Math.max(Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(uri)), null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Arquivo/Recurso não encontrado.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.vista_lugar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onTimeSet(TimePicker vista, int hora, int minuto){
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(lugar.getFecha());
        calendario.set(Calendar.HOUR_OF_DAY, hora);
        calendario.set(Calendar.MINUTE, minuto);
        lugar.setFecha(calendario.getTimeInMillis());
        actualizarVistas(id);//Verificar se com o id dá erro.
        TextView thora = (TextView) getView().findViewById(R.id.hora);
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        thora.setText(formato.format(new Date(lugar.getFecha())));
    }

    public void actualizarVistas(final long id) {
        int _id = SelectorFragment.adaptador.idPosicion((int) id);
        //MainActivity.lugares.actualiza(_id, lugar);  //Não deverá ser utilizado. está gerando lugar = null
        //SelectorFragment.adaptador.setCursor(MainActivity.lugares.extraeCursor()); //Não deverá ser utilizado. está gerando lugar = null
        //SelectorFragment.adaptador.notifyItemChanged((int) id); //Não deverá ser utilizado. está gerando lugar = null
        this.id = id;
        lugar = SelectorFragment.adaptador.lugarPosicion((int) id);
        if (lugar != null) {
            TextView nombre = (TextView) v.findViewById(R.id.nombre);
            nombre.setText(lugar.getNombre());
            ImageView logo_tipo = (ImageView) v.findViewById(R.id.logo_tipo);
            logo_tipo.setImageResource(lugar.getTipo().getRecurso());
            TextView tipo = (TextView) v.findViewById(R.id.tipo);
            tipo.setText(lugar.getTipo().getTexto());
            /*tipo = (Spinner) v.findViewById(R.id.stipo);
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, TipoLugar.getNombres());
            ArrayAdapter<String> adaptador = ArrayAdapter.createFromResource(this, TipoLugar.getNombres(), android.R.layout.simple_spinner_item); //Teste!
            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tipo.setAdapter(adaptador);
            tipo.setSelection(lugar.getTipo().ordinal());*/
            TextView direccion = (TextView) v.findViewById(R.id.direccion);
            direccion.setText(lugar.getDireccion());
            TextView telefono = (TextView) v.findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
            TextView url = (TextView) v.findViewById(R.id.url);
            url.setText(lugar.getUrl());
            TextView comentario = (TextView) v.findViewById(R.id.comentario);
            comentario.setText(lugar.getComentario());
            TextView fecha = (TextView) v.findViewById(R.id.fecha);
            fecha.setText(DateFormat.getDateInstance().format(new Date(lugar.getFecha())));
            TextView hora = (TextView) v.findViewById(R.id.hora);
            hora.setText(DateFormat.getTimeInstance().format(new Date(lugar.getFecha())));
            RatingBar valoracion = (RatingBar) v.findViewById(R.id.valoracion);
            valoracion.setOnRatingBarChangeListener(null);
            valoracion.setRating(lugar.getValoracion());
            valoracion.setOnRatingBarChangeListener(
                    new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar,
                                                    float valor, boolean fromUser) {
                            lugar.setValoracion(valor);
                        }
                    });
        }
    }
}
