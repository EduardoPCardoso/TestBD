package com.example.eduar.mislugares;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DialogoSelectorHora extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener escuchador;

    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener escuchador){
        this.escuchador = escuchador;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendario = Calendar.getInstance();
        Bundle args = this.getArguments();
        if (args != null){
            long fecha = args.getLong("fecha");
            calendario.setTimeInMillis(fecha);
        }
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), escuchador, hora, minuto, DateFormat.is24HourFormat(getActivity()));
    }
}
