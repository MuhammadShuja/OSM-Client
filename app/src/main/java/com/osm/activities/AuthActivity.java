package com.osm.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.osm.R;
import com.osm.adapters.WizardAdapter;
import com.osm.fragments.WizardFragment;

public class AuthActivity extends AppCompatActivity implements WizardFragment.OnFragmentInteractionListener {

    public static ViewPager viewPager;
    private Button btnStart, btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_auth);

        ((TextView) findViewById(R.id.tvLogo)).setTypeface(Typeface.createFromAsset(getAssets(), "bigdey.ttf"));

        viewPager = (ViewPager) findViewById(R.id.wizardViewPager);
        viewPager.setAdapter(new WizardAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
