package team.kudi.dongengku.Handler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import java.util.ArrayList;
import java.util.List;

import team.kudi.dongengku.Adapter.AdapterListDogeng;
import team.kudi.dongengku.Object.DongengItem;


/**
 * Created by user on 3/29/2017.
 */

public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;

    private static DbHandler sInstance;

    // Database Name
    private static final String DATABASE_NAME = "dongeng_kudi";

    // Dongeng table name
    private static final String TABLE_DONGENG = "dongeng";

    // Dongeng Table Columns names
    private static final String KEY_ID_DONGENG = "id_dongeng";
    private static final String KEY_JUDUL = "judul";
    private static final String KEY_DESKRIPSI = "deskripsi";
    private static final String KEY_FAVORITED = "favorited";
    private static final String KEY_SRC_COVER = "cover";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_PAGE = "page";

    private String CREATE_DONGENG_TABLE = "CREATE TABLE "
            + TABLE_DONGENG+ "("
            + KEY_ID_DONGENG + " VARCHAR PRIMARY KEY,"
            + KEY_PAGE + " VARCHAR,"
            + KEY_JUDUL + " VARCHAR,"
            + KEY_DESKRIPSI + " INT,"
            + KEY_LIKES + "INT,"
            + KEY_SRC_COVER + " TEXT,"
            + KEY_FAVORITED + " VARCHAR"
            + ")";

        //favorited 1, 0 unfavorited

    public static synchronized DbHandler getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHandler(context);
        }
        return sInstance;
    }

    public DbHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DONGENG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DONGENG);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void dropTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DONGENG);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addDongengItem(DongengItem dongengItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_DONGENG, dongengItem.getIdDongeng());
        values.put(KEY_PAGE, dongengItem.getPage());
        values.put(KEY_JUDUL, dongengItem.getJudulDongeng());
        values.put(KEY_DESKRIPSI, dongengItem.getDeskDongeng());
        values.put(KEY_SRC_COVER, dongengItem.getIdCoverResource());
        values.put(KEY_FAVORITED, dongengItem.getFavorited());

        // Inserting Row
        db.insert(TABLE_DONGENG, null, values);
        db.close(); // Closing database connection
    }

    public List<DongengItem> getAllDongeng(){
        List<DongengItem> dongengItemList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DONGENG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DongengItem d = new DongengItem(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));

                // Adding contact to list
                dongengItemList.add(d);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dongengItemList;

    }

    public List<DongengItem> getDongengsByPage(String page){
        List<DongengItem> dongengItemList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DONGENG + " WHERE "+KEY_PAGE+" = '"+page+"'";;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DongengItem d = new DongengItem(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));

                // Adding contact to list
                dongengItemList.add(d);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dongengItemList;

    }

    public List<DongengItem> getAllFavDongeng(){
        List<DongengItem> dongengItemList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DONGENG + " WHERE "+KEY_FAVORITED+" = '"+1+"'";;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DongengItem d = new DongengItem(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));

                // Adding contact to list
                dongengItemList.add(d);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dongengItemList;

    }

    public boolean updateDongengFav(DongengItem dongengItem, String fav){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FAVORITED, fav);

        // updating row
        int a =  db.update(TABLE_DONGENG, values, KEY_ID_DONGENG + " = ?",
                new String[] { String.valueOf(dongengItem.getIdDongeng()) });
        if (a > 0){
            return true;
        }else{
            return false;
        }

    }

    public boolean isFavorite(String id){
        String selectQuery = "SELECT "+KEY_FAVORITED+" FROM " + TABLE_DONGENG + " WHERE "+KEY_ID_DONGENG+" = '"+id+"'";;
        String result = "2";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return result.equals("1");
    }

    public void insertDongeng(DongengItem dongengItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PAGE, dongengItem.getPage());
        values.put(KEY_JUDUL, dongengItem.getJudulDongeng());
        values.put(KEY_DESKRIPSI, dongengItem.getDeskDongeng());
        values.put(KEY_SRC_COVER, dongengItem.getIdCoverResource());
        values.put(KEY_FAVORITED, dongengItem.getFavorited());

        if(isDongengExist(dongengItem.getIdDongeng())){
            db.update(TABLE_DONGENG, values,KEY_ID_DONGENG+ " = ?",
                    new String[]{String.valueOf(dongengItem.getIdDongeng())});
            db.close();
        }else{
           addDongengItem(dongengItem);
        }
    }

    public boolean isDongengExist(String idDongeng){
        String selectQuery = "SELECT "+KEY_ID_DONGENG+" FROM " + TABLE_DONGENG + " WHERE "+KEY_ID_DONGENG+" = '"+idDongeng+"'";;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount()>0){
            return true;
        }
        return false;
    }
}
