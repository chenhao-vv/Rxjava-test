package com.vivo.chmusicdemo.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import com.vivo.chmusicdemo.R;

public class MySettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    private PreferenceScreen mKnown;
    private ListPreference mFirstShow;
    private SwitchPreference mNetWork;

    @Override
    protected void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);
        addPreferencesFromResource(R.xml.settingspreference);
        init();
    }

    private void init() {
        mKnown = (PreferenceScreen) findPreference("known");
        mFirstShow = (ListPreference) findPreference("mainfragment");
        mNetWork = (SwitchPreference) findPreference("personal_network");
        mFirstShow.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference pre, Object newValue) {
        if(pre instanceof ListPreference) {
            ListPreference listPreference = (ListPreference)pre;
            CharSequence[] entries = listPreference.getEntries();
            int dex = listPreference.findIndexOfValue((String)newValue);
            listPreference.setSummary(entries[dex]);
        }
        return true;
    }
}
