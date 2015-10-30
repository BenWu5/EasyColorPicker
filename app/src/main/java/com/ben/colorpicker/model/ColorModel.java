package com.ben.colorpicker.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.ben.colorpicker.db.Column;
import com.ben.colorpicker.db.Tables;

/**
 * Created by Hui on 2015/10/25.
 */
public class ColorModel extends Model {
    private int color;
    private long createTime;
    private String note;

    @Override
    protected Column getIdColumn() {
        return Tables.Color.ID;
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues contentValues = super.asContentValues();
        contentValues.put(Tables.Color.COLOR.getName(), color);
        contentValues.put(Tables.Color.CREATE_TIME.getName(), createTime);
        contentValues.put(Tables.Color.NOTE.getName(), note);
        return contentValues;
    }

    public static ColorModel from(Cursor cursor) {
        final ColorModel colorModel = new ColorModel();
        if (cursor.getCount() > 0) {
            colorModel.updateFromCursor(cursor, null);
        }
        return colorModel;
    }

    public void updateFromCursor(Cursor cursor, String columnPrefixTable) {
        super.updateFromCursor(cursor, columnPrefixTable);
        int index;
        // color
        index = cursor.getColumnIndex(Tables.Color.COLOR.getName(columnPrefixTable));
        if (index >= 0) {
            setColor(cursor.getInt(index));
        }
        // note
        index = cursor.getColumnIndex(Tables.Color.NOTE.getName(columnPrefixTable));
        if (index >= 0) {
            setNote(cursor.getString(index));
        }

        // note
        index = cursor.getColumnIndex(Tables.Color.CREATE_TIME.getName(columnPrefixTable));
        if (index >= 0) {
            setCreateTime(cursor.getLong(index));
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
