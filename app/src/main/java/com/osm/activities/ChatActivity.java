package com.osm.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.API.OSM;
import com.osm.R;
import com.osm.adapters.ChatAdapter;
import com.osm.adapters.FriendsAdapter;
import com.osm.models.CropModel;
import com.osm.models.MergerModel;
import com.osm.models.UserModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private List<MergerModel> mergers;

    private RecyclerView rvMergers;
    private Button btnNewMerger;
    private ChatAdapter adapter;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle(OSM.activeChatName);

        mergers = new ArrayList<>();

        adapter = new ChatAdapter(ChatActivity.this, mergers);

        rvMergers = (RecyclerView) findViewById(R.id.rvChat);
        rvMergers.setLayoutManager(new LinearLayoutManager(this));
        rvMergers.setAdapter(adapter);
        if(adapter.getItemCount() > 0 ){
            rvMergers.scrollToPosition(adapter.getItemCount() - 1);
        }

        OSM.getInstance(getApplicationContext()).getMergers(new APIResponse.MergerFriendListener() {
            @Override
            public void onSuccess(List<MergerModel> mergers) {
                adapter = new ChatAdapter(ChatActivity.this, mergers = OSM.mergers);
                rvMergers.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(adapter.getItemCount() > 0 ){
                    rvMergers.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            }
        });

        btnNewMerger = (Button) findViewById(R.id.btnNewMerger);
        btnNewMerger.setOnClickListener(new View.OnClickListener() {
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

            CropModel.image = selectedImage;
            startActivity(new Intent(ChatActivity.this, AdjustActivity.class));
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
                    ImageActionDialog.this.getDialog().cancel();
                    startActivity(new Intent(ChatActivity.this, CameraActivity.class));
                }
            });
            view.findViewById(R.id.btnFromGallery).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageActionDialog.this.getDialog().cancel();
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                    getActivity().startActivityForResult(chooserIntent, PICK_IMAGE);
                }
            });
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                    // Add action buttons
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ImageActionDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}