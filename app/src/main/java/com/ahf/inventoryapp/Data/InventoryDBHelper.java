package com.ahf.inventoryapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ahmed Hassan on 26/08/2018.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "create table " + InventoryContract.InventoryEntry.TABLE_NAME + " ("
                + InventoryContract.InventoryEntry._ID + " integer primary key autoincrement, "
                + InventoryContract.InventoryEntry.ID + " integer , "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " text not null, "
                + InventoryContract.InventoryEntry.COLUMN_QUANTITY + " integer not null, "
                + InventoryContract.InventoryEntry.COLUMN_PRICE + " text not null, "
                + InventoryContract.InventoryEntry.COLUMN_IMAGE + " text, "
                + InventoryContract.InventoryEntry.COLUMN_SUP_NAME + " text, "
                + InventoryContract.InventoryEntry.COLUMN_SUP_EMAIL + " text, "
                + InventoryContract.InventoryEntry.COLUMN_SUP_PHONE + " text);";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String insertItem(InventoryItem inventoryItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, inventoryItem.getProductName());
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, inventoryItem.getCurrentQuantity());
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, inventoryItem.getPrice());
        values.put(InventoryContract.InventoryEntry.COLUMN_IMAGE, inventoryItem.getImage());
        values.put(InventoryContract.InventoryEntry.COLUMN_SUP_NAME, inventoryItem.getSupplierName());
        values.put(InventoryContract.InventoryEntry.COLUMN_SUP_EMAIL, inventoryItem.getSupplierEmail());
        values.put(InventoryContract.InventoryEntry.COLUMN_SUP_PHONE, inventoryItem.getSupplierPhone());
        long id = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
//        inventoryItem.setId(id+"");
        Log.e("sss", id + "");
        ContentValues value = new ContentValues();
        value.put(InventoryContract.InventoryEntry.ID, id);
        db.update(InventoryContract.InventoryEntry.TABLE_NAME, value, InventoryContract.InventoryEntry._ID + " = " + id, null);
        return id + "";
    }

    public void deletItem(InventoryItem item) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_DELET_ITEM = "delete from " + InventoryContract.InventoryEntry.TABLE_NAME
                + " where _id = " + item.getId();
        db.execSQL(SQL_DELET_ITEM);
    }

    public ArrayList<InventoryItem> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<InventoryItem> items = new ArrayList<>();
        String SQL_SELECT_ITEM = "select * from " + InventoryContract.InventoryEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(SQL_SELECT_ITEM, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY)) != 0) {
                InventoryItem inventoryItem = new InventoryItem(
                        cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUP_NAME)),
                        cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUP_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUP_PHONE)),
                        cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.ID))
                );
                items.add(inventoryItem);
            }
            cursor.moveToNext();
        }

        return items;
    }

    public int reduceQuantity(InventoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        int newQuantity = (item.getCurrentQuantity() + (-1));
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, newQuantity);
        db.update(InventoryContract.InventoryEntry.TABLE_NAME, values, " _id = " + item.getId(), null);
        return newQuantity;
    }

    public int increasrQuantity(InventoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        int newQuantity = (item.getCurrentQuantity() + (1));
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, newQuantity);
        db.update(InventoryContract.InventoryEntry.TABLE_NAME, values, " _id = " + item.getId(), null);
        return newQuantity;
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }

}
