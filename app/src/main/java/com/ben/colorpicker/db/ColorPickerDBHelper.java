package com.ben.colorpicker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hui on 2015/10/25.
 */
public class ColorPickerDBHelper  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "colorPicker.db"; //数据库名称
    private static final int DATABASE_VERSION = 1;//数据库版本

    public ColorPickerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tables.Color.createScript());
    }
    //数据库版本或表结构改变会被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
