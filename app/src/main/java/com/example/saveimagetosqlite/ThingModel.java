package com.example.saveimagetosqlite;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class ThingModel implements Serializable {
    private int id;
    private String name;
    private String detail;
    private Bitmap bitmap;
    private Uri imageUri;

    public ThingModel(int id, String name, String detail, Bitmap bitmap, Uri imageUri) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.bitmap = bitmap;
        this.imageUri = imageUri;
    }

    public ThingModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "ThingModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", bitmap=" + bitmap +
                ", imageUri=" + imageUri +
                '}';
    }
}
