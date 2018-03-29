package com.getweixinmessage;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.*;

public class ZeteticApplication extends Application {

	private static ZeteticApplication instance;
	private Activity activity;
    public static String DATABASE_NAME = "EnMicroMsg.db";
    public static String DATABASE_PASSWORD = "test";
    public static final String TAG = "Zetetic";
    public static final String ONE_X_DATABASE = "1x.db";
    public static final String ONE_X_USER_VERSION_DATABASE = "1x-user-version.db";
    public static final String UNENCRYPTED_DATABASE = "unencrypted.db";

    public ZeteticApplication(){
        instance = this;
    }

    public static ZeteticApplication getInstance(){
        return instance;
    }

    public void setCurrentActivity(Activity activity){
        this.activity = activity;
    }

    public Activity getCurrentActivity(){
        return activity;
    }

    public void prepareDatabaseEnvironment(){
        File databaseFile = getDatabasePath(DATABASE_NAME);
        databaseFile.getParentFile().mkdirs();

        if(databaseFile.exists()){
            Log.i(TAG, "Before delete of database file");
            databaseFile.delete();
        }
    }

    public SQLiteDatabase createDatabase(File databaseFile, String password){
        Log.i(TAG, "Before SQLiteDatabase.openOrCreateDatabase");
        return SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null);
    }

    public SQLiteDatabase openDatabase(String path, String password){
        Log.i(TAG, "openDatabase path="+path +" password="+password);
        return SQLiteDatabase.openDatabase(path, password, null, SQLiteDatabase.OPEN_READONLY);
    }
    
    public void extractAssetToDatabaseDirectory(String fileName) throws IOException {

        int length;
        InputStream sourceDatabase = ZeteticApplication.getInstance().getAssets().open(fileName);
        File destinationPath = ZeteticApplication.getInstance().getDatabasePath(fileName);
        OutputStream destination = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[4096];
        while((length = sourceDatabase.read(buffer)) > 0){
            destination.write(buffer, 0, length);
        }
        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    public void delete1xDatabase() {
        
        File databaseFile = getInstance().getDatabasePath(ONE_X_DATABASE);
        databaseFile.delete();
    }

    public void deleteDatabaseFileAndSiblings(String databaseName){

        File databaseFile = ZeteticApplication.getInstance().getDatabasePath(databaseName);
        File databasesDirectory = new File(databaseFile.getParent());
        for(File file : databasesDirectory.listFiles()){
            file.delete();
        }
    }
}
