package com.example.mvince.instagramviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;


public class ApplicationDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public ApplicationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    private static final int DATABASE_VERSION = 18;

    private static final String DATABASE_NAME = "ImageStorage";

    private static final String TABLE_IMAGE= "ImageInformation";

    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE_PATH = "imagePath";
    private static final String KEY_USER_NAME = "username";

    private static final String CREATE_TABLE_COMMODITY = "CREATE TABLE " + TABLE_IMAGE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_IMAGE_PATH + " TEXT)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_COMMODITY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveToSdCard(Bitmap bitmap,String imageName,String username) {

        String picturePath = "";
        File sdcard = Environment.getExternalStorageDirectory();
        File folder = new File(sdcard.getAbsoluteFile(),"/Instagram");
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(),imageName);
        picturePath = file.toString();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Save image path ro sqlite database

        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues newPictureValue = new ContentValues();
            newPictureValue.put(KEY_IMAGE_PATH, picturePath);
            newPictureValue.put(KEY_USER_NAME,username);
            db.insert(TABLE_IMAGE, null, newPictureValue);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public  String getImagePath(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select imagePath from " + TABLE_IMAGE + " where username = '" + username + "'";
        Cursor cursor = db.rawQuery(Query, null);
        String imagePath=null;
        if (cursor.moveToFirst()) {
            do {
                imagePath = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return imagePath;
    }
    public  boolean checkCommodityImage(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_IMAGE + " where username = '" + username + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}




