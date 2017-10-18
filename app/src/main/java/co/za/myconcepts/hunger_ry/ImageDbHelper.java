package co.za.myconcepts.hunger_ry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Images.db";

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ", ";

    //SQL_CREATE_ENTRIES in plain SQL
    //CREATE TABLE image (id INTEGER PRIMARY KEY, image_url TEXT)

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ImageContract.ImageEntry.TABLE_NAME
            + " (" + ImageContract.ImageEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            ImageContract.ImageEntry.COLUMN_NAME_IMAGE_URL +" " + TEXT_TYPE +  ")";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IN EXISTS " + ImageContract.ImageEntry.TABLE_NAME;

    public ImageDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
