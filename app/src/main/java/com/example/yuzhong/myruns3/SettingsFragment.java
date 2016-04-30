package com.example.yuzhong.myruns3;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.content.BroadcastReceiver;

/**
 * Created by Yuzhong on 2016/4/7.
 */
public class SettingsFragment extends PreferenceFragment {
    private HistoryFragment historyFragment;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //get preference data!!!!
        PreferenceScreen preferenceScreen = (PreferenceScreen)getPreferenceScreen();
        ListPreference res = (ListPreference) preferenceScreen.findPreference("list_preference");
//        historyFragment = (HistoryFragment) getActivity().getFragmentManager().findFragmentById(2);
        ViewPager v = (ViewPager)getActivity().findViewById(R.id.view_pager);
        res.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {

//                HistoryFragment fragment = (HistoryFragment) getFragmentManager().findFragmentById(1);
//                fragment.refresh();
//                historyFragment.refresh();
                Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                getActivity().sendBroadcast(intent);
                return true;
            }
        });


    }

}

