package com.osm.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.osm.API.OSM;
import com.osm.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MergerViewActivity extends AppCompatActivity {

    private ImageView ivMerger;
    private ImageButton btnClose, btnDownload;
    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_merger_view);

        imageURL = OSM.activeMergerView;

        ivMerger = (ImageView) findViewById(R.id.ivImage);
        Glide
            .with(getApplicationContext())
            .load(imageURL)
            .into(ivMerger);

        btnClose = (ImageButton) findViewById(R.id.btnMergerClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MergerViewActivity.this, ChatActivity.class));
            }
        });

        btnDownload = (ImageButton) findViewById(R.id.btnMergerDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "OSM");
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            String path = mediaStorageDir.getAbsolutePath(); //access specific directory, path to our directory
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String file_name = path +"/IMG_"+ date + ".jpg";
            final File picPath = new File(file_name);
            Glide.with(getBaseContext())
                .asBitmap()
                .load(imageURL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(picPath);
                            resource.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                            Toast.makeText(getApplicationContext(),"Image has been succefully saved to: "+picPath, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
