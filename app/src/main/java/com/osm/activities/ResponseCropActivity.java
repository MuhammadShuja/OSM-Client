package com.osm.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.API.OSM;
import com.osm.R;
import com.osm.models.CropModel;

import java.util.ArrayList;

public class ResponseCropActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnCrop;

    private ProgressDialog pDialog;
    private ArrayList<CropModel> cropPoints;
    private Display display;
    private Point pointSize;

    private Bitmap image, alteredImage, temporaryBitmap;
    private int screenWidth, screenHeight;

    private Path clipPath;
    private Canvas canvas;
    private Paint paint, cPaint;
    private float downX = 0;
    private float downY = 0;
    private float tDownX = 0;
    private float tDownY = 0;
    private float upX = 0;
    private float upY = 0;
    private long lastTouchDown = 0;
    private int CLICK_ACTION_THRESHHOLD = 100;
    private float smallX, smallY, largeX, largeY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_response_crop);

        if(CropModel.responseImage == null){
            startActivity(new Intent(ResponseCropActivity.this, ChatActivity.class));
        }

        btnCrop = (Button) findViewById(R.id.btnCrop);
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        pDialog = new ProgressDialog(ResponseCropActivity.this);
        cropPoints = new ArrayList<>();
        display = getWindowManager().getDefaultDisplay();
        pointSize = new Point();
        display.getSize(pointSize);
        screenWidth = pointSize.x;
        screenHeight = pointSize.y;

        image = Bitmap.createScaledBitmap(CropModel.responseImage, screenWidth, screenHeight, false);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage.setImageBitmap(image);
        initCanvas();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
            }
        }

        int cX = (screenWidth - image.getWidth()) >> 1;
        int cY = (screenHeight - image.getHeight()) >> 1;
        canvas.drawBitmap(image, 0, 0, null);
        ivImage.setImageBitmap(alteredImage);
        ivImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        clipPath = new Path();
                        clipPath.moveTo(downX, downY);
                        tDownX = downX;
                        tDownY = downY;
                        smallX = downX;
                        smallY = downY;
                        largeX = downX;
                        largeY = downY;
                        lastTouchDown = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        cropPoints.add(new CropModel(upX, upY));
                        clipPath = new Path();
                        clipPath.moveTo(tDownX,tDownY);
                        for(int i = 0; i<cropPoints.size();i++){
                            clipPath.lineTo(cropPoints.get(i).getX(),cropPoints.get(i).getY());
                        }
                        canvas.drawPath(clipPath, paint);
                        ivImage.invalidate();
                        downX = upX;
                        downY = upY;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {

                            cropPoints.clear();
                            initCanvas();

                            int cX = (screenWidth - image.getWidth()) >> 1;
                            int cY = (screenHeight - image.getHeight()) >> 1;
                            canvas.drawBitmap(image, 0, 0, null);
                            ivImage.setImageBitmap(alteredImage);

                        } else {
                            if (upX != upY) {
                                upX = motionEvent.getX();
                                upY = motionEvent.getY();


                                canvas.drawLine(downX, downY, upX, upY, paint);
                                clipPath.lineTo(upX, upY);
                                ivImage.invalidate();

                                crop();
                            }

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initCanvas(){
        alteredImage = Bitmap.createBitmap(screenWidth, screenHeight, image.getConfig());
        canvas = new Canvas(alteredImage);
        paint = new Paint();
        paint.setColor( Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{15.0f, 15.0f}, 0));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void crop() {

        clipPath.close();
        clipPath.setFillType( Path.FillType.INVERSE_WINDING);

        for(int i = 0; i<cropPoints.size();i++){
            if(cropPoints.get(i).getY()<smallX){

                smallX=cropPoints.get(i).getY();
            }
            if(cropPoints.get(i).getX()<smallY){

                smallY=cropPoints.get(i).getX();
            }
            if(cropPoints.get(i).getY()>largeX){

                largeX=cropPoints.get(i).getY();
            }
            if(cropPoints.get(i).getX()>largeY){

                largeY=cropPoints.get(i).getX();
            }
        }

        temporaryBitmap = alteredImage;
        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setColor(getResources().getColor(R.color.colorAccent));
        cPaint.setAlpha(100);
        canvas.drawPath(clipPath, cPaint);

        canvas.drawBitmap(temporaryBitmap, 0, 0, cPaint);

    }


    private void save() {

        if(clipPath != null) {
            final int color = 0xff424242;
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            paint.setXfermode(new PorterDuffXfermode( PorterDuff.Mode.CLEAR));
            canvas.drawPath(clipPath, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            canvas.drawBitmap(alteredImage, 0, 0, paint);

            float w = largeX - smallX;
            float h = largeY - smallY;
            try{
                alteredImage = Bitmap.createBitmap(alteredImage, (int) smallX, (int) smallY, (int) w, (int) h);
            }
            catch (Exception ex){
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else{
            alteredImage = image;
        }
        pDialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                CropModel.responseCroppedImage = alteredImage;
                startActivity(new Intent(ResponseCropActivity.this, HomeActivity.class));
                OSM.getInstance(getApplicationContext()).acceptRequest(new APIResponse.MergerListener() {
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
        };
        mThread.start();

    }
}
