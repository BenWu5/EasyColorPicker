package com.ben.colorpicker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.ben.colorpicker.model.Model;


public final class DataStore {
    private DataStore() {
    }

    public static DataStoreInsert insert() {
        return new DataStoreInsert();
    }

    public static DataStoreUpdate update() {
        return new DataStoreUpdate();
    }

    public static DataStoreDelete delete() {
        return new DataStoreDelete();
    }

    public static DataStoreQuery query() {
        return new DataStoreQuery();
    }


    public static final class DataStoreInsert {
        private ContentValues values;

        public DataStoreInsert model(Model model) {
            return values(model.asContentValues());
        }

        public DataStoreInsert values(ContentValues values) {
            if (this.values != null) {
                throw new IllegalStateException("Values is already set.");
            }

            if (values == null) {
                throw new NullPointerException("Values cannot be null.");
            }

            this.values = values;
            return this;
        }

        public Uri into(Context context, Uri uri) {
            if (values == null) {
                throw new IllegalStateException("Values must be set before executing insert.");
            }

            return context.getContentResolver().insert(uri, values);
        }

        public long into(SQLiteDatabase database, String table) {
            if (values == null) {
                throw new IllegalStateException("Values must be set before executing insert.");
            }

            return database.insert(table, null, values);
        }
    }

    public static final class DataStoreUpdate {
        private ContentValues values;
        private String selection;
        private String[] selectionArgs;

        private DataStoreUpdate() {
        }

        public DataStoreUpdate model(Model model) {
            selection = Tables.Color.ID.getName() + "=?";
            selectionArgs = new String[]{model.getId()};
            return values(model.asContentValues());
        }


        public DataStoreUpdate values(ContentValues values) {
            if (this.values != null) {
                throw new IllegalStateException("Values is already set.");
            }

            if (values == null) {
                throw new NullPointerException("Values cannot be null.");
            }

            this.values = values;
            return this;
        }


        public int update(Context context, Uri uri) {
            if (values == null) {
                throw new IllegalStateException("Values must be set before executing insert.");
            }

            return context.getContentResolver().update(uri, values, selection, selectionArgs);
        }
    }

    public static class DataStoreDelete {
        private String selection;
        private String[] selectionArgs;

        public DataStoreDelete model(Model model) {
            selection = Tables.Color.ID.getName() + "=?";
            selectionArgs = new String[]{model.getId()};
            return this;
        }

        public int delete(Context context, Uri uri) {
            return context.getContentResolver().delete(uri, selection, selectionArgs);
        }

        public int clear(Context context, Uri uri) {
            selection = Tables.Color.ID.getName() + " is not null";
            return context.getContentResolver().delete(uri, selection, null);
        }
    }

    public static class DataStoreQuery {
        String sortOrder;
        String[] projection;

        public DataStoreQuery projection(String[] projection) {
            this.projection = projection;
            return this;
        }

        public DataStoreQuery sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public DataStoreQuery sortOrderDesc(String sortOrder) {
            this.sortOrder = sortOrder + " desc";
            return this;
        }

        public CursorLoader query(Context context, Uri uri) {
            CursorLoader loader = new CursorLoader(context);
            loader.setUri(uri);
            loader.setProjection(projection);
            loader.setSortOrder(sortOrder);
            return loader;
        }
    }


}
