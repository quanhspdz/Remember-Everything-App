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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CAMERA_PERMISSION = 123;
    final int REQUEST_READ_STORAGE_PERMISSION = 321;
    final int CAMERA_RESULT = 6969;
    final int STORAGE_RESULT = 9696;

    Button btnCamera, btnStorage, btnAdd;
    ImageView imgMain;
    EditText edtName, edtDetail;

    DatabaseHelper database;
    ArrayList<ThingModel> arrayThing;
    boolean fromCamera = false, fromStorage = false;
    boolean storageAccessIsGranted = false;
    Uri imageUri;
    ThingModel lastThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = findViewById(R.id.btnCameraEdit);
        btnStorage = findViewById(R.id.btnStorageEdit);
        btnAdd = findViewById(R.id.btnEdit);
        imgMain = findViewById(R.id.imgMainReview);
        edtName = findViewById(R.id.edtNameEdit);
        edtDetail = findViewById(R.id.edtDetailEdit);

        database = new DatabaseHelper(MainActivity.this);
        arrayThing = new ArrayList<>();

        imgMain.setImageResource(R.drawable.cat_meme_loading);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_STORAGE_PERMISSION);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
            }
        });

        btnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storageAccessIsGranted) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, STORAGE_RESULT);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgMain.getDrawable() != null) {
                    String name = edtName.getText().toString();
                    String detail = edtDetail.getText().toString();
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgMain.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ThingModel thing = new ThingModel(-1, name, detail, bitmap, null);
                    database.addAnItem(thing);

                    Toast.makeText(MainActivity.this, "Add successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Activity_List_Things.class);
                    finish();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_RESULT);
                }
                break;
            }
            case REQUEST_READ_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storageAccessIsGranted = true;
                }
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
                    imgMain.setImageBitmap(bitmap);
                    fromCamera = true;
                    break;
                }
                case STORAGE_RESULT: {
                    imageUri = data.getData();
                    imgMain.setImageURI(imageUri);
                    fromStorage = true;
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}