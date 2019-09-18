package com.osm.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.osm.R;
import com.osm.models.CropModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MergerResponseActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnResponseImage;
    private Bitmap image;

    public static final int PICK_IMAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merger_response);

        ivImage = (ImageView) findViewById(R.id.ivImage);

        btnResponseImage = (Button) findViewById(R.id.btnResponseImage);
        btnResponseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageActionDialog().show(getFragmentManager(), null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            final Uri imageUri = data.getData();
            Bitmap selectedImage = null;
            try {
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            CropModel.responseImage = selectedImage;
            startActivity(new Intent(MergerResponseActivity.this, ResponseAdjustActivity.class));
        }
    }

    @SuppressLint("ValidFragment")
    public class ImageActionDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.layout_image_action_dialog, null);
            view.findViewById(R.id.btnCaptureNew).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MergerResponseActivity.ImageActionDialog.this.getDialog().cancel();
                    startActivity(new Intent(MergerResponseActivity.this, ResponseCameraActivity.class));
                }
            });
            view.findViewById(R.id.btnFromGallery).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MergerResponseActivity.ImageActionDialog.this.getDialog().cancel();
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                    getActivity().startActivityForResult(chooserIntent, PICK_IMAGE);
                }
            });
            builder.setView(view)
                    // Add action buttons
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MergerResponseActivity.ImageActionDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}
