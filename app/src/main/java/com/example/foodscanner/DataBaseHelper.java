package com.example.foodscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";

    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String FOOD_ID = "FOOD_ID";
    public static final String FOOD_BRAND = "FOOD_BRAND";
    public static final String FOOD_NAME = "FOOD_NAME";
    public static final String FOOD_DATE = "FOOD_DATE";
    public static final String FOOD_QUANTITY = "FOOD_QUANTITY";
    public static final String BARCODE = "BARCODE_COL";

    public static final String BARCODE_TABLE = "BARCODE_TABLE";
    public static final String BARCODE_ID = "BARCODE_ID";
    public static final String BARCODE_BRAND = "BARCODE_BRAND";
    public static final String BARCODE_NAME = "BARCODE_NAME";
    public static final String BARCODE_NUMBER = "BARCODE_NUMBER";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "ScanDB.db", null, 1);
    }

    // this is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableFoodList = "CREATE TABLE " + FOOD_TABLE + " (" + FOOD_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FOOD_BRAND + " TEXT, " + FOOD_NAME + " TEXT, " + FOOD_DATE + " TEXT, " + FOOD_QUANTITY + " INT, " + BARCODE + " INT )";

        String createTableBarcode = "CREATE TABLE " + BARCODE_TABLE + " (" + BARCODE_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BARCODE_BRAND + " TEXT, " + BARCODE_NAME + " TEXT, " + BARCODE_NUMBER + " TEXT )";

        db.execSQL(createTableBarcode);
        db.execSQL(createTableFoodList);

    }

    // This is called if there is a new database version to update.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(AddList addList) {

        String queryString = "SELECT * FROM " + FOOD_TABLE
                + " WHERE "  + FOOD_BRAND + " = '" + addList.getBrand() + "' AND "  + FOOD_NAME + " = '" + addList.getName() + "'";
        SQLiteDatabase Rdb = this.getReadableDatabase();
        Cursor cursor = Rdb.rawQuery(queryString, null);

        SQLiteDatabase db = this.getWritableDatabase();

        if (cursor.moveToFirst()) {

            String newqueryString = "UPDATE " + FOOD_TABLE
                    + " SET " + FOOD_QUANTITY + " = " + FOOD_QUANTITY + " + " + addList.getQuantity()
                    + " WHERE " + FOOD_BRAND + " = '" + addList.getBrand()
                    + "' AND " + FOOD_NAME + " = '" + addList.getName() + "' AND " + FOOD_DATE + " = " + addList.getDate();

            db.execSQL(newqueryString);

            db.close();
            cursor.close();

            return true;
        } else {

            ContentValues cv = new ContentValues();

            cv.put(FOOD_BRAND, addList.getBrand());
            cv.put(FOOD_NAME, addList.getName());
            cv.put(FOOD_DATE, addList.getDate());
            cv.put(FOOD_QUANTITY, addList.getQuantity());
            cv.put(BARCODE, addList.getBarcode());

            long insert = db.insert(FOOD_TABLE, null, cv);

            db.close();
            cursor.close();

            return insert != -1;
        }
    }

    public boolean deleteOne (InfoList infoList) {

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + FOOD_TABLE + " WHERE " + FOOD_ID + " = " + infoList.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        db.close();

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean deleteOneEx (ExpiryList expiryList) {

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + FOOD_TABLE + " WHERE " + FOOD_ID + " = " + expiryList.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        db.close();

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public ArrayList<InfoList> mainfoodlist() {

        ArrayList<InfoList> foodList = new ArrayList<>();

        String queryString = "WITH UniqueName AS "
                + "(SELECT *, ROW_NUMBER() OVER(PARTITION BY " + BARCODE + " ORDER BY " + FOOD_DATE + " ) "
                + "AS RowRank FROM " + FOOD_TABLE + ") "
                + "SELECT " + FOOD_ID + ", " +  FOOD_BRAND + ", " +  FOOD_NAME + ", " + FOOD_DATE + ", "
                + " SUM(" + FOOD_QUANTITY + " ), " + FOOD_QUANTITY + ", " + BARCODE + ", RowRank FROM UniqueName GROUP BY " + BARCODE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int foodID = cursor.getInt(0);
                String foodBrand = cursor.getString(1);
                String foodName = cursor.getString(2);
                String foodDate = cursor.getString(3);
                String foodTotalQuantity = cursor.getString(4);
                String foodQuantity = cursor.getString(5);
                String barcode = cursor.getString(6);

                InfoList newFood = new InfoList(foodID,foodBrand,foodName,foodDate,foodTotalQuantity,foodQuantity,barcode);

                foodList.add(newFood);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return foodList;
    }

    public ArrayList<ExpiryList> expiryLists() {

        ArrayList<ExpiryList> expiryLists = new ArrayList<>();

        String queryString = "WITH UniqueName AS "
                + "(SELECT *, ROW_NUMBER() OVER(PARTITION BY " + BARCODE
                + " ORDER BY " + FOOD_DATE + ") AS RowRank FROM "
                + FOOD_TABLE + ") SELECT * FROM UniqueName WHERE RowRank != 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int foodID = cursor.getInt(0);
                String foodBrand = cursor.getString(1);
                String foodName = cursor.getString(2);
                String foodDate = cursor.getString(3);
                String foodQuantity = cursor.getString(4);
                String barcode = cursor.getString(5);

                ExpiryList newexpiry = new ExpiryList(foodID,foodBrand,foodName,foodDate,foodQuantity,barcode);

                expiryLists.add(newexpiry);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return expiryLists;
    }

    public boolean addBarcode(BarcodeList barcodeList) {

        String queryString = "SELECT * FROM " + BARCODE_TABLE + " WHERE " + BARCODE_NUMBER + " = " + barcodeList.getBarcode();
        SQLiteDatabase Rdb = this.getReadableDatabase();
        Cursor cursor = Rdb.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {

            SQLiteDatabase Wdb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(BARCODE_BRAND, barcodeList.getBrand());
            cv.put(BARCODE_NAME, barcodeList.getName());
            cv.put(BARCODE_NUMBER, barcodeList.getBarcode());

            long insert = Wdb.insert(BARCODE_TABLE,null,cv);

            Wdb.close();
            cursor.close();
            return insert !=1;
        }
    }

    public boolean updateBarcode(BarcodeList barcodeList) {
        String queryString = "UPDATE " + BARCODE_TABLE
                + " SET " + BARCODE_BRAND + " = '" + barcodeList.getBrand() + "', " + BARCODE_NAME + " = '" + barcodeList.getName()
                + "' WHERE " + BARCODE_NUMBER + " = " + barcodeList.getBarcode();

        SQLiteDatabase Wdb = this.getWritableDatabase();
        Wdb.execSQL(queryString);
        Wdb.close();
        return true;
    }

    public ArrayList<BarcodeList> barcodeLists() {

        ArrayList<BarcodeList> barLists = new ArrayList<>();

        String queryString = "SELECT * FROM " + BARCODE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int barcodeID = cursor.getInt(0);
                String barcodeBrand = cursor.getString(1);
                String barcodeName = cursor.getString(2);
                String barcodeNumber = cursor.getString(3);

                BarcodeList newBarcode = new BarcodeList(barcodeID,barcodeBrand,barcodeName,barcodeNumber);

                barLists.add(newBarcode);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return barLists;
    }

    public ArrayList<NameBrandList> findBarcode(String barcode) {

        String bName;
        String bBrand;
        ArrayList<NameBrandList> barcodeName = new ArrayList<>();

        String queryString = "SELECT * FROM " + BARCODE_TABLE + " WHERE " + BARCODE_NUMBER + " LIKE " + barcode;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                bBrand = cursor.getString(1);
                bName = cursor.getString(2);

                NameBrandList nameBrandList = new NameBrandList(bBrand,bName);

                barcodeName.add(nameBrandList);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return barcodeName;

    }

    public boolean deleteOneBarcode (BarcodeList barcodeList) {

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + BARCODE_TABLE + " WHERE " + BARCODE_ID + " = " + barcodeList.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}
