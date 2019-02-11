package com.maqautocognita.desktop.databases;

import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.badlogic.gdx.Gdx;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class DesktopDatabaseCursor implements DatabaseCursor {

    private static final String TAG = "DesktopDatabase";

    private ResultSet resultSet;

    @Override
    public byte[] getBlob(int columnIndex) {
        return new byte[0];
    }

    @Override
    public double getDouble(int columnIndex) {
        try {
            return resultSet.getDouble(columnIndex + 1);
        } catch (SQLException e) {
            Gdx.app.log(TAG, "getDouble", e);
        }

        return 0;
    }

    @Override
    public float getFloat(int columnIndex) {
        try {
            return resultSet.getFloat(columnIndex + 1);
        } catch (SQLException e) {
            Gdx.app.log(TAG, "getFloat", e);
        }

        return 0;
    }

    @Override
    public int getInt(int columnIndex) {

        try {
            return resultSet.getInt(columnIndex + 1);
        } catch (SQLException e) {
            Gdx.app.log(TAG, "getInt", e);
        }

        return 0;
    }

    @Override
    public long getLong(int columnIndex) {

        try {
            return resultSet.getLong(columnIndex + 1);
        } catch (SQLException e) {
            Gdx.app.log(TAG, "getLong", e);
        }

        return 0;
    }

    @Override
    public short getShort(int columnIndex) {

        try {
            return resultSet.getShort(columnIndex + 1);
        } catch (SQLException e) {
            Gdx.app.log(TAG, "getShort", e);
        }

        return 0;
    }

    @Override
    public String getString(int columnIndex) {

        try {
            return resultSet.getString(columnIndex + 1);
        } catch (SQLException e) {
            Gdx.app.log(TAG, "getString", e);
        }

        return null;
    }

    @Override
    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            Gdx.app.log(TAG, "next", e);
        }
        return false;
    }

    @Override
    public boolean moveToFirst() {
        return next();
    }

    @Override
    public int getCount() {
//        try {
//            resultSet.last();
//            int count = resultSet.getRow();
//            resultSet.beforeFirst();
//            return count;
//        } catch (SQLException e) {
//            Gdx.app.log(TAG, "getCount", e);
//        }
        //TODO SQLite is not support cursor move back, so there is no way to get the number of records in the resulet by cursor
        return 100;
    }

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (SQLException e) {
            Gdx.app.log(TAG, "close", e);
        }
    }

    @Override
    public int getColumnIndex(String columnName) {
        try {
            return resultSet.findColumn(columnName) - 1;
        } catch (SQLException e) {
            Gdx.app.error(getClass().getName(), "", e);
        }

        return 0;
    }

    public void setNativeCursor(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
