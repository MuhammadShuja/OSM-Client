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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class BackgroundActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnBackground;
    private Bitmap image;

    public static final int PICK_IMAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        if(CropModel.croppedImage == null){
            startActivity(new Intent(BackgroundActivity.this, ChatActivity.class));
        }
        image = CropModel.croppedImage;

        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage.setImageBitmap(image);

        btnBackground = (Button) findViewById(R.id.btnBackground);
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundActionDialog().show(getFragmentManager(), null);
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

            CropModel.background = selectedImage;
            startActivity(new Intent(BackgroundActivity.this, RequestMergerActivity.class));
        }
    }

    @SuppressLint("ValidFragment")
    public class BackgroundActionDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.layout_background_action_dialog, null);
            view.findViewById(R.id.btnBGFromApp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BackgroundActivity.BackgroundActionDialog.this.getDialog().cancel();
                    startActivity(new Intent(BackgroundActivity.this, BackgroundChooserActivity.class));
                }
            });
            view.findViewById(R.id.btnBGFromGallery).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BackgroundActivity.BackgroundActionDialog.this.getDialog().cancel();
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
                            BackgroundActivity.BackgroundActionDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}
