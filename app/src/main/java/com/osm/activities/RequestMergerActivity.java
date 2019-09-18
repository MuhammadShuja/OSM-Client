package com.osm.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.API.OSM;
import com.osm.R;
import com.osm.models.CropModel;

public class RequestMergerActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnRequestMerger;
    private Bitmap image, background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_merger);

        image = CropModel.croppedImage;
        background = CropModel.background;

        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage.setImageBitmap(background);

        btnRequestMerger = (Button) findViewById(R.id.btnRequestMerger);
        btnRequestMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestMergerActivity.this, HomeActivity.class));
                OSM.getInstance(getApplicationContext()).requestMerger(new APIResponse.MergerListener() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
