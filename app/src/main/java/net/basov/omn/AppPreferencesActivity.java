package net.basov.omn;


/**
 * Created by mvb on 6/22/17.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class AppPreferencesActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Preference author = findPreference(getString(R.string.pk_notes_author));
        author.setSummary(defSharedPref.getString(getString(R.string.pk_notes_author), ""));

    }

    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Preference pref = findPreference(key);

        String pref_key_author = getString(R.string.pk_notes_author);

        if (pref_key_author.equals(key)) {
            pref.setSummary(sharedPreferences.getString(pref_key_author,""));
        }

    }



}


