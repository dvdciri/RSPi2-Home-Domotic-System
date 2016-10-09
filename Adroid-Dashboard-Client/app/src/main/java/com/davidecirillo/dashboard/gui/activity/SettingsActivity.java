package com.davidecirillo.dashboard.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.davidecirillo.dashboard.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by davidecirillo on 28/04/16.
 */
public class SettingsActivity extends PreferenceActivity {

    public final static String EXTRA_KEY_SETTING_LIST = "key_setting_list";

    Toolbar mToolbar;

    public static Intent getSettingsActivity(Context context, HashMap<String, String> settingList) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(EXTRA_KEY_SETTING_LIST, settingList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar) findViewById(R.id.screenToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Display the fragment as the main content.
        GeneralFragmentPreference fragment = new GeneralFragmentPreference();
        fragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_frame_container, fragment)
                .commit();

    }

    public static class GeneralFragmentPreference extends PreferenceFragment {

        HashMap<String, String> settingList;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            settingList = ((HashMap<String, String>) getArguments().get(EXTRA_KEY_SETTING_LIST));

            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(getActivity());

            PreferenceCategory category = new PreferenceCategory(getActivity());
            category.setTitle("General Settings");
            screen.addPreference(category);

            //Load Preference
            Iterator it = settingList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                Preference preference = new Preference(getActivity());
                preference.setTitle(pair.getKey().toString() + " : " + pair.getValue());

                category.addPreference(preference);
            }

            setPreferenceScreen(screen);
        }
    }
}
