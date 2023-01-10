package com.example.eduar.mislugares;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferenciasFragments extends PreferenceFragment{
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }

}
