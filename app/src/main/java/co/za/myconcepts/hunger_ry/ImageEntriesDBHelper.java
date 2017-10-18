package co.za.myconcepts.hunger_ry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ImageEntriesDBHelper {

    private ImageDbHelper mDBHelper;
    private Context mContext;

    public ImageEntriesDBHelper(Context context){
        this.mContext = context;
        mDBHelper = new ImageDbHelper(context);
    }

    public void insertEntries(ContentValues values){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.insert(ImageContract.ImageEntry.TABLE_NAME, null, values);
    }

    public List<String> readEntries(){
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {ImageContract.ImageEntry.COLUMN_NAME_IMAGE_URL};

        Cursor cursor = db.query(
                ImageContract.ImageEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List <String> image_urls = new ArrayList<>();
        while(cursor.moveToNext()){
            String image_url = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.ImageEntry.COLUMN_NAME_IMAGE_URL));
            image_urls.add(image_url);
        }
        cursor.close();
        return image_urls;
    }
}
