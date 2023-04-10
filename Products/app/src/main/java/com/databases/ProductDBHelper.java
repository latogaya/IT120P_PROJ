package com.databases;

import static java.lang.Integer.parseInt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "product_details.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCT_DETAILS = "product_details";
    private static final String COLUMN_PRODUCT_ID = "clothes_id";
    private static final String COLUMN_PRODUCT_NAME = "clothes_name";
    private static final String COLUMN_PRODUCT_BRAND = "clothes_brand";
    private static final String COLUMN_PRODUCT_PRICE = "clothes_price";
    private static final String COLUMN_PRODUCT_SIZE = "clothes_size";
    private static final String COLUMN_PRODUCT_CONDITION = "clothes_condition";
    private static final String COLUMN_PRODUCT_TYPE = "clothes_type";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "description_content";
    private static final String COLUMN_PRODUCT_IMAGE = "clothes_image";



    public ProductDBHelper(Context context) {
        super(context, "product_details.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_DETAILS);
        onCreate(db);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_BRAND, product.getBrand());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_SIZE, product.getSize());
        values.put(COLUMN_PRODUCT_CONDITION, product.getCondition());
        values.put(COLUMN_PRODUCT_TYPE, product.getType());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_IMAGE, product.getImage());

        db.insert(TABLE_PRODUCT_DETAILS, null, values);
        db.close();
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCT_DETAILS, new String[] {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_BRAND,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_SIZE,
                COLUMN_PRODUCT_CONDITION,
                COLUMN_PRODUCT_TYPE,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_IMAGE
        }, COLUMN_PRODUCT_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(
                parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Double.parseDouble(cursor.getString(3)),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getBlob(8));

        cursor.close();
        db.close();

        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_DETAILS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                byte[] imageBytes = cursor.getBlob(8);
                Product product = new Product(parseInt(COLUMN_PRODUCT_ID), COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_BRAND, Double.parseDouble(COLUMN_PRODUCT_PRICE), COLUMN_PRODUCT_SIZE, COLUMN_PRODUCT_CONDITION, COLUMN_PRODUCT_TYPE, COLUMN_PRODUCT_DESCRIPTION, imageBytes);
                product.setId(parseInt(cursor.getString(0)));
                product.setName(cursor.getString(1));
                product.setBrand(cursor.getString(2));
                product.setPrice(Double.parseDouble(cursor.getString(3)));
                product.setSize(cursor.getString(4));
                product.setCondition(cursor.getString(5));
                product.setType(cursor.getString(6));
                product.setDescription(cursor.getString(7));
                product.setImage(cursor.getBlob(8));
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return productList;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_BRAND, product.getBrand());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_SIZE, product.getSize());
        values.put(COLUMN_PRODUCT_CONDITION, product.getCondition());
        values.put(COLUMN_PRODUCT_TYPE, product.getType());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_IMAGE, product.getImage());

        // updating row
        return db.update(TABLE_PRODUCT_DETAILS, values, COLUMN_PRODUCT_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT_DETAILS, COLUMN_PRODUCT_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
        db.close();
    }


}