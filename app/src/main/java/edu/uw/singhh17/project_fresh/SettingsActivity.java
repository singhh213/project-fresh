package edu.uw.singhh17.project_fresh;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.prefs.Preferences;

public class SettingsActivity extends AppCompatActivity implements edu.uw.singhh17.project_fresh.Preferences.OnFragmentInteractionListener,
 AboutUs.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle("Settings");
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.settings_container, new edu.uw.singhh17.project_fresh.Preferences());
        ft.commit();
    }
}
