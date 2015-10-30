package com.ben.colorpicker.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ben.colorpicker.db.Column;

public abstract class Model implements Parcelable {
    private String id;

    protected Model() {
        super();
        setId(null);
    }

    protected Model(Parcel parcel) {
        setId(parcel.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
    }

    public ContentValues asContentValues() {
        final ContentValues values = new ContentValues();
        values.put(getIdColumn().getName(), id);
        return values;
    }

    public void updateFromCursor(Cursor cursor, String columnPrefixTable) {
        int index;

        // Id
        index = cursor.getColumnIndex(getIdColumn().getName(columnPrefixTable));
        if (index >= 0) {
            setId(cursor.getString(index));
        }

    }


    protected abstract Column getIdColumn();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Model)) return false;

        final Model model = (Model) o;
        return !(TextUtils.isEmpty(id) || TextUtils.isEmpty(model.id)) && id.equals(model.id);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
