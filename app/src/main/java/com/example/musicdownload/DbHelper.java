package com.example.musicdownload;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.musicdownload.DbContract.SongEntry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HymnReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " +
                    SongEntry.TABLE_NAME + " (" +
                    SongEntry._ID + " INTEGER PRIMARY KEY," +
                    SongEntry.ALBUM + " TEXT," +
                    SongEntry.NAME+" TEXT,"+
                    SongEntry.URL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =  "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertData(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        ContentValues contentValues = new ContentValues();
        String baseURL = "https://www.last.fm/music/+free-music-downloads?page=";
            try {
                for(int i=1; i<13; i++){
                    URL url = new URL(baseURL+i);
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line=bufferedReader.readLine())!=null){

                        if(line.endsWith(".mp3\"")){
                            line = line
                                    .replaceAll("\\s", "")
                                    .replace("\"", "")
                                    .replace("href=", "");
                            String[] strings = line.split("/");
                            String title = strings[7]
                                    .replace(".mp3", "")
                                    .split("-")[1];
                            title = URLDecoder.decode(title, "UTF-8");
                            contentValues.put(SongEntry.NAME, title);
                            contentValues.put(SongEntry.URL, line);
                            sqLiteDatabase.insert(SongEntry.TABLE_NAME, null, contentValues);


                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

    }

    public Cursor readData(String searchString) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                SongEntry.NAME,
                SongEntry.URL
        };


        String[] selectionArgs = null;
        String selection = null;
        if (!searchString.isEmpty()){
            selectionArgs = new String[]{"%"+searchString+"%"};
            selection = SongEntry.NAME + " LIKE ?";
        }


        return sqLiteDatabase.query(
                SongEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );
    }

}
