package com.example.ventsislavdraganov.jobless.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ventsislavdraganov on 12/20/17.
 */

public class JoblessDatabase extends SQLiteOpenHelper {

    //creating the database in the constructor
    private static final String DATABASE_NAME = "'jobless.db";
    private static final int DATABASE_VERSION = 1;
    private final String userTable;
    private final String jobTable;
    private final String incomeTable;

    public JoblessDatabase(Context context, String userTable, String jobTable, String incomeTable){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.userTable = userTable;
        this.jobTable = jobTable;
        this.incomeTable = incomeTable;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(userTable);
        sqLiteDatabase.execSQL(jobTable);
        sqLiteDatabase.execSQL(incomeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
