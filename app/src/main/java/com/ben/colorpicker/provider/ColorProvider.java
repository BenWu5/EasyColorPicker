package com.ben.colorpicker.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.ben.colorpicker.BuildConfig;
import com.ben.colorpicker.db.ColorPickerDBHelper;
import com.ben.colorpicker.db.Column;
import com.ben.colorpicker.db.Tables;

/**
 * Created by Hui on 2015/10/25.
 */
public class ColorProvider extends ContentProvider {
    protected static final String CONTENT_URI_BASE = "content://";

    private static final int URI_ITEMS = 1;
    private static final int URI_ITEMS_ID = 2;

    protected static final String TYPE_LIST_BASE = "vnd.android.cursor.dir/vnd.ben.";
    protected static final String TYPE_ITEM_BASE = "vnd.android.cursor.item/vnd.ben.";

    private ColorPickerDBHelper dbOpenHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static Uri uriColor() {
        return uriModels(ColorProvider.class, Tables.Color.TABLE_NAME);
    }

    protected static String getAuthority(Class<? extends ContentProvider> cls) {
        return BuildConfig.APPLICATION_ID + ".provider." + cls.getSimpleName();
    }

    protected String getAuthority() {
        return getAuthority(getClass());
    }

    public static Uri uriModels(Class<? extends ContentProvider> providerClass, String baseModelTable) {
        return Uri.parse(CONTENT_URI_BASE + getAuthority(providerClass) + "/" + baseModelTable);
    }

    @Override
    public boolean onCreate() {
        this.dbOpenHelper = new ColorPickerDBHelper(this.getContext());
        final String authority = getAuthority();
        final String mainTable = getModelTable();
        uriMatcher.addURI(authority, mainTable, URI_ITEMS);
        uriMatcher.addURI(authority, mainTable + "/*", URI_ITEMS_ID);
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db =getDatabase();
        int number = db.delete(getModelTable(), selection, selectionArgs);
        this.getContext().getContentResolver().notifyChange(uri, null);
        return number;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_ITEMS:
                return TYPE_LIST_BASE + getModelTable();
            case URI_ITEMS_ID:
                return TYPE_ITEM_BASE + getModelTable();
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    //往ContentProvider添加数据
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int uriId = uriMatcher.match(uri);
        switch (uriId) {
            case URI_ITEMS:
                final SQLiteDatabase database = getDatabase();
                database.insert(getModelTable(), null, values);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return uri;

    }


    //往ContentProvider查询数据
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final Cursor cursor;

        final int uriId = uriMatcher.match(uri);
        switch (uriId) {
            case URI_ITEMS:
                cursor = queryItems(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case URI_ITEMS_ID:
                cursor = queryItem(uri, projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        final Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        cursor.moveToFirst();
        return cursor;
    }

    protected Cursor queryItems(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(getQueryTables(uri));

        final SQLiteDatabase database = getDatabase();
        return qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
    }

    protected Cursor queryItem(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(getQueryTables(uri));
        qb.appendWhere(getIdColumn() + "='" + uri.getPathSegments().get(1) + "'");

        final SQLiteDatabase database = getDatabase();
        return qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
    }

    protected String getQueryTables(Uri uri) {
        return getModelTable();
    }

    protected Column getIdColumn() {
        return Tables.Color.ID;
    }


    //往ContentProvider更新数据
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        final int uriId = uriMatcher.match(uri);
        switch (uriId) {
            case URI_ITEMS:
                count = updateItems(uri, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    protected int updateItems(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return getDatabase().update(getModelTable(), values, selection, selectionArgs);
    }


    protected String getModelTable() {
        return Tables.Color.TABLE_NAME;
    }

    protected SQLiteDatabase getDatabase() {
        return dbOpenHelper.getWritableDatabase();
    }
}
