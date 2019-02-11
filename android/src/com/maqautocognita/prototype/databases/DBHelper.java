package com.maqautocognita.prototype.databases;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.maqautocognita.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by kotarou on 19/8/15.
 * change the file DB_PATH and DB_NAME before compile
 */


public class DBHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DB_PATH = "/data/data/com.maqautocognita.prototype/databases/";
    private static final String DB_NAME = "autoCognita-player.db";
    private final Context myContext;
    private String SDB_NAME;
    private SQLiteDatabase myDataBase;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        SDB_NAME = DB_NAME;
        myContext = context;
    }

    public DBHelper(Context context, String databaseName, int DB_VERSION) {
        super(context, databaseName, null, DB_VERSION);
        SDB_NAME = databaseName;
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db = SQLiteDatabase.openDatabase(DB_NAME, null, 0);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        //db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        int dbExist = checkDataBase();

        if (dbExist == 0) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            this.close();
            try {

                copyDataBase();

            } catch (IOException e) {
                Log.e(getClass().getName(), "Error when copying database", e);

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private int checkDataBase() {

        try {
            String myPath = FileUtils.getCopiedFileStorePath(myContext) + File.separator + SDB_NAME;
            SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.
            return 1;
        }

        return 0;
//        testVersion = "";
//        if (checkDB != null) {
//
//            try {
//                cursor = checkDB.rawQuery("select versionNo from Version", null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        testVersion = cursor.getString(cursor.getColumnIndex("versionNo"));
//                    } while (cursor.moveToNext());
//                }
//                checkDB.close();
//
//            } catch (SQLiteException e) {
//                return -1;
//            }
//
//        }
//
//        if (testVersion == "") {
//            return -1;
//        } else if (Integer.parseInt(testVersion) < DATABASE_VERSION) {
//            return 1;
//        } else {
//            return 0;
//        }
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {


        FileUtils.copyFileFromAsset(myContext, SDB_NAME, false);

//        try {
//            Log.d("TAG", Arrays.toString(myContext.getAssets().list(".")));
//        } catch (IOException e) {
//
//            Log.e("TAG", e.getLocalizedMessage(), e);
//        }
//
//        //Open your local db as the input stream
//        InputStream myInput = myContext.getAssets().open(SDB_NAME);
//
//        // Path to the just created empty db
//        String outFileName = DB_PATH + SDB_NAME;
//
//        File dbFolder = new File(outFileName);
//        if(!dbFolder.exists()){
//            dbFolder.mkdirs();
//        }
//
//        //Open the empty db as the output stream
//        OutputStream myOutput = new FileOutputStream(outFileName);
//
//        //transfer bytes from the inputfile to the outputfile
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer))>0){
//            myOutput.write(buffer, 0, length);
//        }
//
//        //Close the streams
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + SDB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

}
