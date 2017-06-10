package com.example.andrea.nav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andrea on 21/03/17.
 */

public class DatabaseOperations extends SQLiteOpenHelper {

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE " + TableData.TableInfo.TABLE_NAME + "(" + TableData.TableInfo.PRODUCT_ID + " TEXT, " + TableData.TableInfo.PRODUCT_NAME + " TEXT, " + TableData.TableInfo.PRODUCT_PRICE + " TEXT," + TableData.TableInfo.PRODUCT_Qta + " TEXT," + TableData.TableInfo.PRODUCT_IMG + " TEXT );";

    public DatabaseOperations(Context context){
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        Log.d("Database_Operations", "Database_Created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_QUERY);
        Log.d("Database_Operations", "Table_Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertProductOnCart(DatabaseOperations dop, String id, String name, String price, String qta ){
        int precQta = getQtaOfProductInCart(dop, id);
        if(precQta==-1) {
            SQLiteDatabase SQ = dop.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TableData.TableInfo.PRODUCT_ID, id);
            cv.put(TableData.TableInfo.PRODUCT_NAME, name);
            cv.put(TableData.TableInfo.PRODUCT_PRICE, price);
            cv.put(TableData.TableInfo.PRODUCT_Qta, qta);
            long k = SQ.insert(TableData.TableInfo.TABLE_NAME, null, cv);
        }else{
            Integer newQta = new Integer(qta) + precQta;
            updateQtaProductOnCart(dop, id, newQta.toString());
        }
        Log.d("Database_Operations", "Row inserted");
    }

    public void insertProductOnCart2(DatabaseOperations dop, String id, String name, String price, String qta, String img ){
        int precQta = getQtaOfProductInCart(dop, id);
        if(precQta==-1) {
            SQLiteDatabase SQ = dop.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TableData.TableInfo.PRODUCT_ID, id);
            cv.put(TableData.TableInfo.PRODUCT_NAME, name);
            cv.put(TableData.TableInfo.PRODUCT_PRICE, price);
            cv.put(TableData.TableInfo.PRODUCT_Qta, qta);
            cv.put(TableData.TableInfo.PRODUCT_IMG, img);
            long k = SQ.insert(TableData.TableInfo.TABLE_NAME, null, cv);
        }else{
            Integer newQta = new Integer(qta) + precQta;
            updateQtaProductOnCart(dop, id, newQta.toString());
        }
        Log.d("Database_Operations", "Row inserted");
    }

    public Float updateQtaProductOnCart(DatabaseOperations dop, String id, String qta){

        String selection = TableData.TableInfo.PRODUCT_ID + " LIKE ?";
        String[] args = {id};
        ContentValues values = new ContentValues();
        values.put(TableData.TableInfo.PRODUCT_Qta, qta);
        SQLiteDatabase SQ = dop.getReadableDatabase();
        SQ.update(TableData.TableInfo.TABLE_NAME, values, selection, args);
        Log.d("Database_Operations", id+"");
        return getProductPrice(dop, id);
    }

    public Float getProductPrice(DatabaseOperations dop, String id){
        String selection = TableData.TableInfo.PRODUCT_ID + " LIKE ?";
        String[] args = {id};
        String[] columns = {TableData.TableInfo.PRODUCT_PRICE};
        SQLiteDatabase SQ = dop.getReadableDatabase();
        Cursor CR = SQ.query(TableData.TableInfo.TABLE_NAME, columns, selection, args, null, null, null);
        if(CR.getCount()==1){
            CR.moveToFirst();
            return Float.parseFloat(CR.getString(0));
        }
        return Float.parseFloat("-1");
    }

    public int getQtaOfProductInCart(DatabaseOperations dop, String id){
        String selection = TableData.TableInfo.PRODUCT_ID + " LIKE ?";
        String[] args = {id};
        String[] columns = {TableData.TableInfo.PRODUCT_Qta};
        SQLiteDatabase SQ = dop.getReadableDatabase();
        Cursor CR = SQ.query(TableData.TableInfo.TABLE_NAME, columns, selection, args, null, null, null);
        if(CR.getCount()==1){
            CR.moveToFirst();
            return Integer.parseInt(CR.getString(0));
        }
        return -1;
    }

    public Cursor getCartProduct(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.PRODUCT_ID, TableData.TableInfo.PRODUCT_NAME, TableData.TableInfo.PRODUCT_PRICE, TableData.TableInfo.PRODUCT_Qta, TableData.TableInfo.PRODUCT_IMG};
        Cursor CR = SQ.query(TableData.TableInfo.TABLE_NAME, columns, null, null, null, null, null);
        return CR;
    }

    public void deleteAllCart(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.delete(TableData.TableInfo.TABLE_NAME, null, null);
        Log.d("Database_Operations", "Table empty");
    }

    public void deleteItemFromCart(DatabaseOperations dop, String id){
        String selection = TableData.TableInfo.PRODUCT_ID + " LIKE ?";
        String[] args = {id};
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.delete(TableData.TableInfo.TABLE_NAME, selection, args);
        Log.d("Database_Operations", "Table empty");
    }
}
