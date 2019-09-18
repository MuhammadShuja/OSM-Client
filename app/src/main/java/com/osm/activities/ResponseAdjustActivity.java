package com.osm.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.osm.R;
import com.osm.models.CropModel;

import java.io.File;

public class ResponseAdjustActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnProceed;
    private ImageButton btnRotateLeft, btnRotateRight;

    private int angle;
    private Matrix rotationMatrix;

    private File picPath = null;
    private Bitmap image, rotatedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_response_adjust);

        if(CropModel.responseImage == null){
            startActivity(new Intent(ResponseAdjustActivity.this, MergerResponseActivity.class));
        }
        image = CropModel.responseImage;

        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage.setImageBitmap(image);

        rotationMatrix = new Matrix();

        btnRotateLeft = (ImageButton) findViewById(R.id.btnRotateLeft);
        btnRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                angle -= 90;
                rotationMatrix.postRotate(angle);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
                rotatedImage = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), rotationMatrix, true);
                ivImage.setImageBitmap(rotatedImage);
            }
        });
        btnRotateRight = (ImageButton) findViewById(R.id.btnRotateRight);
        btnRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                angle += 90;
                rotationMatrix.postRotate(angle);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
                rotatedImage = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), rotationMatrix, true);
                ivImage.setImageBitmap(rotatedImage);
            }
        });

        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rotatedImage != null){
                    CropModel.responseImage = rotatedImage;
                }
                startActivity(new Intent(ResponseAdjustActivity.this, ResponseCropActivity.class));
            }
        });
    }
}
