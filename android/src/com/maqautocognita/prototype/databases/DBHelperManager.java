
package com.maqautocognita.prototype.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.IOException;

/**
 *
 */
public class DBHelperManager implements DatabaseManager {

    private Context context;
    private DBHelper dbHelper;

    public DBHelperManager(Context context) {
        /*Create Player database connection*/
        dbHelper = new DBHelper(context);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
    }

    public DBHelperManager(Context context, String databaseName, int dbVersion) {
        dbHelper = new DBHelper(context, databaseName, dbVersion);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
    }

    @Override
    public Database getDBHelperDatabase() {
        return new AndroidDatabase(dbHelper);
    }

    @Override
    public Database getNewDatabase(String dbName, int dbVersion, String dbOnCreateQuery, String dbOnUpgradeQuery) {
        return new AndroidDatabase(dbHelper);
    }

    private class AndroidDatabase implements Database {

        private DBHelper helper;
        private SQLiteDatabase database;


        private AndroidDatabase(DBHelper dbHelper) {
            helper = dbHelper;

            database = helper.getWritableDatabase();

        }

        @Override
        public void setupDatabase() {

        }

        @Override
        public void openOrCreateDatabase() throws SQLiteGdxException {
            try {
                database = helper.getWritableDatabase();
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public void closeDatabase() throws SQLiteGdxException {
            try {
                helper.close();
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public void execSQL(String sql) throws SQLiteGdxException {
            try {
                database.execSQL(sql);
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public void updateTable(String tableName, String[] fieldName, String[] updateValues, String whereClause, String[] arg) throws SQLiteGdxException {

            ContentValues cv = new ContentValues();
            for(int i = 0 ; i < fieldName.length; i++){
                cv.put(fieldName[i],updateValues[i]);
            }

            database.update(tableName, cv, whereClause, arg);
        }

        @Override
        public DatabaseCursor rawQuery(String sql) throws SQLiteGdxException {
            AndroidCursor aCursor = new AndroidCursor();
            try {
                Cursor tmp = database.rawQuery(sql, null);
                aCursor.setNativeCursor(tmp);
                return aCursor;
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public DatabaseCursor rawQuery(DatabaseCursor cursor, String sql) throws SQLiteGdxException {
            AndroidCursor aCursor = (AndroidCursor) cursor;
            try {
                Cursor tmp = database.rawQuery(sql, null);
                aCursor.setNativeCursor(tmp);
                return aCursor;
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

    }

}
