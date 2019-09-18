package com.osm.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.osm.R;
import com.osm.models.CropModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageButton btnCapture, btnClose, btnSwitch;

    private Camera mCamera;
    private int mCameraFace = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean isPreviewing = false;
    private File picPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if(!isPreviewing){

            mCamera = Camera.open(mCameraFace);
            if (mCamera != null){
                try {
                    mCamera.setDisplayOrientation(90);
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                    mCamera.startPreview();
                    isPreviewing = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        btnCapture = (ImageButton) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCamera != null)
                {
                    mCamera.takePicture(mShutterCallback, mPictureCallback_RAW, mPictureCallback_JPG);

                }
            }
        });
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPreviewing) {
                    mCamera.stopPreview();
                }
                mCamera.release();
                if(mCameraFace == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    mCameraFace = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                else{
                    mCameraFace = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                mCamera = Camera.open(mCameraFace);

                setCameraDisplayOrientation(CameraActivity.this, mCameraFace, mCamera);
                try {
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }
        });
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, ChatActivity.class));
            }
        });
    }

    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback(){

        public void onShutter() {
        }};

    Camera.PictureCallback mPictureCallback_RAW = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
        }};

    Camera.PictureCallback mPictureCallback_JPG = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
            CropModel.image = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, true);

            startActivity(new Intent(CameraActivity.this, AdjustActivity.class));
        }};

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open(mCameraFace);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(isPreviewing){
            mCamera.stopPreview();
            isPreviewing = false;
        }

        if(mCamera != null){
            try {
                setCameraDisplayOrientation(CameraActivity.this, mCameraFace, mCamera);
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                isPreviewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        isPreviewing = false;
    }

    public static void setCameraDisplayOrientation(AppCompatActivity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
