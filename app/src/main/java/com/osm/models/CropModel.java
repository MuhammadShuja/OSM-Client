package com.osm.models;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class CropModel {

    public static Bitmap image = null;
    public static Bitmap croppedImage = null;
    public static Bitmap background = null;

    public static Bitmap responseImage = null;
    public static Bitmap responseCroppedImage = null;

    private float x, y;

    public CropModel(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static byte[] getImageBytes(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CropModel.croppedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] getBackgroundBytes(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CropModel.background.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] getResponseBytes(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CropModel.responseCroppedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
