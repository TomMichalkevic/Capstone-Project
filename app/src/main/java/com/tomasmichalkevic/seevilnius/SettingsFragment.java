package com.tomasmichalkevic.seevilnius;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SeekBarPreference;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);
        final SeekBarPreference seekBar = (SeekBarPreference) findPreference("radius");
        if(seekBar!=null){
            seekBar.setMin(1);
            seekBar.setMax(5);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

}
