package com.maqautocognita.desktop.databases;

import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.maqautocognita.prototype.databases.SQLiteGdxException;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class DesktopDatabase implements Database {

    private static final String TAG = "DesktopDatabase";

    private Connection connection;

    public DesktopDatabase(String jdbcUrl) {
        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            Gdx.app.log(TAG, "openOrCreateDatabase", e);
        }
    }

    @Override
    public void setupDatabase() {

    }

    @Override
    public void openOrCreateDatabase() throws SQLiteGdxException {

    }

    @Override
    public void closeDatabase() throws SQLiteGdxException {
        try {
            connection.close();
        } catch (SQLException e) {
            Gdx.app.log(TAG, "closeDatabase", e);
            throw new SQLiteGdxException(e);
        }
    }

    @Override
    public void execSQL(String sql) throws SQLiteGdxException {
        ResultSet resultSet = execSQLAndReturnResultSet(sql);
        try {
            resultSet.close();
        } catch (SQLException e) {
            Gdx.app.log(TAG, "close result set " + sql, e);
            throw new SQLiteGdxException(e);
        }
    }

    @Override
    public void updateTable(String tableName, String[] fieldNames, String[] updateValues, String whereClause, String[] args) throws SQLiteGdxException {
        PreparedStatement stmt = null;
        StringBuilder updateSQL = new StringBuilder();
        try {
            updateSQL.append("update " + tableName + " set ");
            for (String fieldName : fieldNames) {
                updateSQL.append(fieldName + " = ?,");
            }
            updateSQL.deleteCharAt(updateSQL.length() - 1);
            if (StringUtils.isNotBlank(whereClause)) {
                updateSQL.append(" where " + whereClause);
            }
            Gdx.app.log(getClass().getName(), updateSQL.toString());
            stmt = connection.prepareStatement(updateSQL.toString());
            int parameterIndex = 1;
            for (String updateValue : updateValues) {
                stmt.setObject(parameterIndex, updateValue);
                parameterIndex++;
            }
            if (ArrayUtils.isNotEmpty(args)) {
                for (String arg : args) {
                    stmt.setObject(parameterIndex, arg);
                    parameterIndex++;
                }
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            Gdx.app.log(TAG, "execSQL " + updateSQL, e);
            throw new SQLiteGdxException(e);
        } finally {
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public DatabaseCursor rawQuery(String sql) throws SQLiteGdxException {
        DesktopDatabaseCursor desktopDatabaseCursor = new DesktopDatabaseCursor();
        return rawQuery(desktopDatabaseCursor, sql);
    }

    @Override
    public DatabaseCursor rawQuery(DatabaseCursor desktopDatabaseCursor, String sql) throws SQLiteGdxException {
        ResultSet resultSet = execSQLAndReturnResultSet(sql);
        ((DesktopDatabaseCursor) desktopDatabaseCursor).setNativeCursor(resultSet);
        return desktopDatabaseCursor;

    }

    private ResultSet execSQLAndReturnResultSet(String sql) throws SQLiteGdxException {
        PreparedStatement stmt = null;
        ResultSet resultSet;
        try {
            stmt = connection.prepareStatement(sql);
            resultSet = stmt.executeQuery();
        } catch (SQLException e) {
            Gdx.app.log(TAG, "execSQL " + sql, e);
            throw new SQLiteGdxException(e);
        } finally {
            if (null != stmt) {
//                try {
//                    stmt.close();
//                } catch (SQLException e) {
//                    Gdx.app.log(TAG, "close prepare statement for sql " + sql, e);
//                    throw new SQLiteGdxException(e);
//                }
            }
        }

        return resultSet;
    }
}
