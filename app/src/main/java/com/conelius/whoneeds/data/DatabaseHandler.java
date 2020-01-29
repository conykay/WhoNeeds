package com.conelius.whoneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListPopupWindow;

import androidx.annotation.Nullable;

import com.conelius.whoneeds.model.Item;
import com.conelius.whoneeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * created by Conelius on 1/21/2020 at 8:05 AM
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BABY_TABLE = "CREATE TABLE "+ Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                +Constants.KEY_ITEM + " TEXT,"
                +Constants.KEY_COLOR +" TEXT,"
                +Constants.KEY_QTY_NUMBER + " INTEGER,"
                +Constants.KEY_ITEM_SIZE +" INTEGER,"
                +Constants.KEY_DATE_NUMBER +" LONG);";

        db.execSQL(CREATE_BABY_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);

    }

    //CRUD operation

    //add item

    public void addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM,item.getItemName());
        values.put(Constants.KEY_COLOR,item.getItemColor());
        values.put(Constants.KEY_QTY_NUMBER,item.getItemQuantity());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());
        values.put(Constants.KEY_DATE_NUMBER,java.lang.System.currentTimeMillis());//timestamp

        //insert the rows

        db.insert(Constants.TABLE_NAME,null,values);

        Log.d("DBhandler", "addItem: ");
    }

    //get an item

    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                Constants.KEY_ITEM,
                Constants.KEY_QTY_NUMBER,
                Constants.KEY_COLOR,
                Constants.KEY_ITEM_SIZE,
                Constants.KEY_DATE_NUMBER},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        Item item = new Item();
        if (cursor != null) {
            cursor.moveToFirst();

            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
            item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));
            item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));
            //convert timestamp to readable format

            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NUMBER))).getTime());

            item.setDateItemAdded(formattedDate);

        }

        return item;
    }

    //Get all items

    public List<Item> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_ITEM,
                        Constants.KEY_COLOR,
                        Constants.KEY_QTY_NUMBER,
                        Constants.KEY_ITEM_SIZE,
                        Constants.KEY_DATE_NUMBER},
                null, null, null, null,
                Constants.KEY_DATE_NUMBER +" DESC");

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
                item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));
                item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
                item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NUMBER))).getTime());

                item.setDateItemAdded(formattedDate);

                //Add to array list
                itemList.add(item);
            }while (cursor.moveToNext());
        }

        return itemList;

    }

    //update item.

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM,item.getItemName());
        values.put(Constants.KEY_COLOR,item.getItemColor());
        values.put(Constants.KEY_QTY_NUMBER,item.getItemQuantity());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());
        values.put(Constants.KEY_DATE_NUMBER,java.lang.System.currentTimeMillis());//timestamp

        //update rows


        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    //Add Delete item

    public void deleteItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID+"=?",new String[]{String.valueOf(id)});

        //close
        db.close();

    }

    public int getItemCount() {

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();

    }

}














