package com.example.saveimagetosqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.net.IDN;
import java.net.URI;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sqlite.thingsManager";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "THINGS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DETAIL = "DETAIL";
    public static final String COLUMN_IMAGE_BITMAP = "IMAGE_BITMAP";
    public static final String COLUMN_IMAGE_URI = "IMAGE_URI";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DETAIL + " TEXT, "
                + COLUMN_IMAGE_BITMAP + " BLOB, "
                + COLUMN_IMAGE_URI + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addAnItem(ThingModel thing) {
        SQLiteDatabase database = this.getWritableDatabase();

        byte[] image = null;
        if (thing.getBitmap() != null)
            image = ImageHandle.bitmapToByte(thing.getBitmap());

        String uri = null;
        if (thing.getImageUri() != null)
            uri = thing.getImageUri().toString();


        ContentValues cv = new ContentValues();
        //cv.put(COLUMN_ID, thing.getId());
        cv.put(COLUMN_NAME, thing.getName());
        cv.put(COLUMN_DETAIL, thing.getDetail());
        cv.put(COLUMN_IMAGE_BITMAP, image);
        cv.put(COLUMN_IMAGE_URI, uri);

        database.insert(TABLE_NAME, null, cv);
        database.close();
    }

    public void toastAllItems(Context context) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectStatement = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = database.rawQuery(selectStatement, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String detail = cursor.getString(2);

                Bitmap bitmap = null;
                if (cursor.getBlob(3) != null)
                    bitmap = ImageHandle.byteToBitmap(cursor.getBlob(3));

                String uriString = cursor.getString(4);
                Uri uri = null;
                if (uriString != null)
                    uri = Uri.parse(uriString);

                ThingModel thing = new ThingModel(id, name, detail, bitmap, uri);

                Toast.makeText(context, thing.toString(), Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
        }
        database.close();
    }

    public ThingModel getLastThing(Context context) {
        ThingModel thing = new ThingModel();

        SQLiteDatabase database = this.getReadableDatabase();
        String selectStatement = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = database.rawQuery(selectStatement, null);

        if (cursor.moveToLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String detail = cursor.getString(2);

            Bitmap bitmap = null;
            if (cursor.getBlob(3) != null)
                bitmap = ImageHandle.byteToBitmap(cursor.getBlob(3));

            String uriString = cursor.getString(4);
            Uri uri = null;
            if (uriString != null)
                uri = Uri.parse(uriString);

            thing = new ThingModel(id, name, detail, bitmap, uri);
        }
        database.close();
        return thing;
    }

    public ArrayList<ThingModel> getAllToArrayList() {
        ArrayList<ThingModel> arrayThing = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        String sqlStatement = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(sqlStatement, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String detail = cursor.getString(2);
                Bitmap bitmap = ImageHandle.byteToBitmap(cursor.getBlob(3));

                arrayThing.add(new ThingModel(id, name, detail, bitmap, null));

            } while (cursor.moveToNext());
        }

        database.close();
        return arrayThing;
    }

    public ArrayList<ThingModel> findAnItem(String name) {
        ArrayList<ThingModel> arrayResult = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        String sqlStatement = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(sqlStatement, null);

        if (cursor.moveToFirst()) {
            do {
                if (name.equals(cursor.getString(1))) {
                    Bitmap bitmap = null;
                    if (cursor.getBlob(3) != null) {
                        bitmap = ImageHandle.byteToBitmap(cursor.getBlob(3));
                    }

                    Uri uri = null;
                    if (cursor.getString(4) != null) {
                        uri = Uri.parse(cursor.getString(4));
                    }

                    ThingModel thing = new ThingModel(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            bitmap,
                            uri);
                    arrayResult.add(thing);
                }
            } while (cursor.moveToNext());
        }

        return arrayResult;
    }

    public void editAnItem(int id, ThingModel newThing) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, newThing.getName());
        cv.put(COLUMN_DETAIL, newThing.getDetail());

        if (newThing.getBitmap() != null)
            cv.put(COLUMN_IMAGE_BITMAP, ImageHandle.bitmapToByte(newThing.getBitmap()));

        if (newThing.getImageUri() != null)
            cv.put(COLUMN_IMAGE_URI, newThing.getImageUri().toString());

        database.update(TABLE_NAME, cv, COLUMN_ID + " = ?",
                new String[] {String.valueOf(id)});

    }

}
