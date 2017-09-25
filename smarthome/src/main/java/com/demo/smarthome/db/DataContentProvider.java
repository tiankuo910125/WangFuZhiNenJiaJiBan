package com.demo.smarthome.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by liukun on 2016/2/26.
 */
public class DataContentProvider extends ContentProvider {

    private String TAG = "DataContentProvider";
    private DBHelper mOpenHelper;
    private static final UriMatcher MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CONFIG=1;
    private static final int CONFIG_ID=2;
    private static final int MESSAGE_ID=7;
    private static final int MESSAGES=8;

    static{
        MATCHER.addURI("DataContentProvider", "config", CONFIG);
        MATCHER.addURI("DataContentProvider", "config/#", CONFIG_ID);
        MATCHER.addURI("DataContentProvider", "message", MESSAGES);
        MATCHER.addURI("DataContentProvider", "message/#", MESSAGE_ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper=new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db= mOpenHelper.getWritableDatabase();
        switch(MATCHER.match(uri)){
            case CONFIG_ID: {
                long id = ContentUris.parseId(uri);
                String where = "id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                return db.query("config", projection, where, selectionArgs,null,null,sortOrder);
            }
            case CONFIG: {
                return db.query("config", projection, selection, selectionArgs,null,null,sortOrder);
            }
            case MESSAGE_ID: {
                long id = ContentUris.parseId(uri);
                String where = "id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                return db.query("message", projection, where, selectionArgs,null,null,sortOrder);
            }
            case MESSAGES: {
                return db.query("message", projection, selection, selectionArgs,null,null,sortOrder);
            }
            default:
                throw new IllegalArgumentException("Unknow Uri:"+uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(MATCHER.match(uri)){
            case CONFIG:
                return "vnd.android.cursor.dir/config";
            case CONFIG_ID:
                return "vnd.android.cursor.item/config";
            case MESSAGES:
                return "vnd.android.cursor.dir/message";
            case MESSAGE_ID:
                return "vnd.android.cursor.item/message";
            default:
                throw new IllegalArgumentException("Unknow Uri:"+uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db= mOpenHelper.getWritableDatabase();
        long id=0;
        switch(MATCHER.match(uri)){
            case CONFIG: {
                id = db.insert("config", null, values);
                Uri insertUri=ContentUris.withAppendedId(uri, id);
                this.getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            }
            case MESSAGES: {
                id = db.insert("message", null, values);
                Uri insertUri=ContentUris.withAppendedId(uri, id);
                this.getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            }
            default:
                throw new IllegalArgumentException("Unknow Uri:"+uri.toString());
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db= mOpenHelper.getWritableDatabase();
        int count=0;
        switch(MATCHER.match(uri)){
            case CONFIG_ID: {
                long id = ContentUris.parseId(uri);
                String where = "id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                count = db.delete("config", where, selectionArgs);
                return count;
            }
            case CONFIG: {
                count = db.delete("config", selection, selectionArgs);
                return count;
            }
            case MESSAGE_ID: {
                long id = ContentUris.parseId(uri);
                String where = "id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                count = db.delete("message", where, selectionArgs);
                return count;
            }
            case MESSAGES: {
                count = db.delete("message", selection, selectionArgs);
                return count;
            }
            default:
                throw new IllegalArgumentException("Unknow Uri:"+uri.toString());
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db= mOpenHelper.getWritableDatabase();
        int count=0;
        switch(MATCHER.match(uri)){
            case CONFIG_ID: {
                long id = ContentUris.parseId(uri);
                String where = "id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                count = db.update("config", values, where, selectionArgs);
                return count;
            }
            case CONFIG: {
                count = db.update("config", values, selection, selectionArgs);
                return count;
            }
            case MESSAGE_ID: {
                long id = ContentUris.parseId(uri);
                String where = "id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                count = db.update("message", values, where, selectionArgs);
                return count;
            }
            case MESSAGES: {
                count = db.update("message", values, selection, selectionArgs);
                return count;
            }
            default:
                throw new IllegalArgumentException("Unknow Uri:"+uri.toString());
        }
    }
}
