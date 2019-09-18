package com.osm.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.osm.R;

public class BackgroundChooserActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnBackground;
    private Bitmap image;

    public static final int PICK_IMAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_chooser);
    }
}
