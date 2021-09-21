package com.example.saveimagetosqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Activity_Review_Image extends AppCompatActivity {

    ImageView imageReview;
    Button btnCamera, btnStorage, btnSubmit;
    EditText edtName, edtDetail;
    ThingModel receiveThing;
    DatabaseHelper database;

    final int CAMERA_PERMISSION_REQUEST = 123;
    final int STORAGE_PERMISSION_REQUEST = 321;
    final int CAMERA_RESULT = 9696;
    final int STORAGE_RESULT = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_image);

        imageReview = findViewById(R.id.imgMainReview);
        edtDetail = findViewById(R.id.edtDetailEdit);
        edtName = findViewById(R.id.edtNameEdit);
        btnCamera = findViewById(R.id.btnCameraEdit);
        btnStorage = findViewById(R.id.btnStorageEdit);
        btnSubmit = findViewById(R.id.btnEdit);

        database = new DatabaseHelper(Activity_Review_Image.this);

        receiveThing = ObjectHolder.currentThing;
        if (receiveThing != null) {
            imageReview.setImageBitmap(receiveThing.getBitmap());
            edtName.setText(receiveThing.getName());
            edtDetail.setText(receiveThing.getDetail());
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Activity_Review_Image.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST);
            }
        });

        btnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Activity_Review_Image.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String detail = edtDetail.getText().toString();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageReview.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                ThingModel newThing = new ThingModel(-1, name, detail, bitmap, null);
                database.editAnItem(receiveThing.getId(), newThing);
                Intent intent = new Intent(Activity_Review_Image.this, Activity_List_Things.class);
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_RESULT);
                }
                break;
            }
            case STORAGE_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, STORAGE_RESULT);
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_RESULT: {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imageReview.setImageBitmap(bitmap);
                    break;
                }
                case STORAGE_RESULT: {
                    Uri uri = data.getData();
                    imageReview.setImageURI(uri);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}