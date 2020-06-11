package com.vivo.chmusicdemo.fragment;


import android.os.Bundle;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.ThemeHelper;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SetThemeFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.nightmode, rootKey);

        ListPreference themePref = findPreference("themePref");
        if(themePref != null) {
            themePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String themeOption = (String) newValue;
                    ThemeHelper.applyTheme(themeOption);
                    return true;
                }
            });
        }
    }
}
