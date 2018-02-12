package com.example.ventsislavdraganov.jobless.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ventsislavdraganov.jobless.JoblessGlobal;

/**
 * Created by ventsislavdraganov on 12/20/17.
 */

public class JoblessDatabaseQueries {

    private JoblessDatabase dbHelper;
    private SQLiteDatabase database;
    private Context context = null;
    private JoblessGlobal joblessGlobal;
    //private JoblessGlobal joblessGlobal;

    public JoblessDatabaseQueries(Context context) {
        dbHelper = new JoblessDatabase(context, JoblessDatabaseDescription.createTableUser(),
                JoblessDatabaseDescription.createTableJob(), JoblessDatabaseDescription.createTableIncome());
        this.context = context;
        joblessGlobal = JoblessGlobal.getInstance();

    }

    //working with the User Table
    public long insertUserInformation(int estimateStatus) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JoblessDatabaseDescription.KEY_ID, estimateStatus);
        values.put(JoblessDatabaseDescription.FIRST_NAME, joblessGlobal.getFirstName());
        values.put(JoblessDatabaseDescription.LAST_NAME, joblessGlobal.getLastName());
        values.put(JoblessDatabaseDescription.GENDER, joblessGlobal.getGender());
        values.put(JoblessDatabaseDescription.DOB, joblessGlobal.getDateOfBirth());
        values.put(JoblessDatabaseDescription.BASE_YEAR, joblessGlobal.getBaseYear());
        values.put(JoblessDatabaseDescription.WEEKS_TO_COLLECT, joblessGlobal.getWeeksToCollect());
        values.put(JoblessDatabaseDescription.WEEKLY_BENEFIT, joblessGlobal.getWeeklyBenefit());
        values.put(JoblessDatabaseDescription.TOTAL_BENEFIT, joblessGlobal.getTotalBenefit());
        values.put(JoblessDatabaseDescription.SAVED, joblessGlobal.getSavedStatusDatabase());
        values.put(JoblessDatabaseDescription.NOT_ELIGIBLE_FOR_BENEFIT, joblessGlobal.getNotEligibleForBenefitDatabase());
        values.put(JoblessDatabaseDescription.ESTIMATE_COMPLETE, joblessGlobal.getEstimateCompleteDatabase());
        long insert = database.insert(JoblessDatabaseDescription.USER_TABLE, null, values);
        database.close();

        return insert;
    }

    public int selectUserInformation() {
        database = dbHelper.getReadableDatabase();
        int userCount = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + JoblessDatabaseDescription.USER_TABLE, new String[]{});
        if (cursor.moveToFirst()) {
            do {

                //userName = ((cursor.getString(cursor.getColumnIndex(JoblessDatabaseDescription.LAST_NAME))));

            } while (cursor.moveToNext());
        }
        userCount = cursor.getCount();
        cursor.close();
        database.close();

        return userCount;
    }

    public int updateUserInformation(int estimateStatus){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JoblessDatabaseDescription.KEY_ID, estimateStatus);
        values.put(JoblessDatabaseDescription.FIRST_NAME, "Ven");
        values.put(JoblessDatabaseDescription.LAST_NAME, "Draganov");
        values.put(JoblessDatabaseDescription.GENDER, "male");
        values.put(JoblessDatabaseDescription.DOB, "11/30/81");
        values.put(JoblessDatabaseDescription.BASE_YEAR, "10/01/2016-09/30/2017");
        values.put(JoblessDatabaseDescription.WEEKS_TO_COLLECT, 26);
        values.put(JoblessDatabaseDescription.WEEKLY_BENEFIT, 612.70);
        values.put(JoblessDatabaseDescription.TOTAL_BENEFIT, 17983.90);
        values.put(JoblessDatabaseDescription.SAVED, 1);
        values.put(JoblessDatabaseDescription.NOT_ELIGIBLE_FOR_BENEFIT, 1);
        values.put(JoblessDatabaseDescription.ESTIMATE_COMPLETE, 1);
        String whereClause = JoblessDatabaseDescription.KEY_ID +"=?";
        String whereArgs[] = {String.valueOf(estimateStatus)};
        int update = database.update(JoblessDatabaseDescription.USER_TABLE, values, whereClause, whereArgs);
        database.close();

        return update;
    }

    public int deleteUser(int id) {
        database = dbHelper.getWritableDatabase();
        String whereClause = JoblessDatabaseDescription.KEY_ID +"=?";
        String whereArgs[] = {String.valueOf(id)};
        int delete =  database.delete(JoblessDatabaseDescription.USER_TABLE, whereClause, whereArgs);
        database.close();

        return delete;
    }


    //------------------------------------------------------------------------------------------
    //Working with the Job Table
    //------------------------------------------------------------------------------------------


    public long insertJobInformation(int userId, int jobIndex){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JoblessDatabaseDescription.JOB_INDEX, jobIndex);
        values.put(JoblessDatabaseDescription.JOB_NAME, "Job Name Here");
        values.put(JoblessDatabaseDescription.POSITION, "Job Position Here");
        values.put(JoblessDatabaseDescription.FIRST_DAY, "07/01/2008");
        values.put(JoblessDatabaseDescription.LAST_DAY, "12/30/2017");
        values.put(JoblessDatabaseDescription.PAYMENT_TYPE, "Payment Type Here");
        values.put(JoblessDatabaseDescription.EMPLOYMENT_STATUS, "Employ Status Goes Here");
        values.put(JoblessDatabaseDescription.DONE_WITH_THIS_JOB, 1);
        values.put(JoblessDatabaseDescription.USER_ID, userId);
        long insert = database.insert(JoblessDatabaseDescription.JOB_TABLE, null, values);
        database.close();

        return insert;
    }

    public int selectJobInformation(int userId){
        database = dbHelper.getReadableDatabase();
        int userCount = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + JoblessDatabaseDescription.JOB_TABLE + " WHERE "+JoblessDatabaseDescription.USER_ID + " = "+ userId, new String[]{});
        if (cursor.moveToFirst()) {
            do {

                //jobName = ((cursor.getString(cursor.getColumnIndex(JoblessDatabaseDescription.POSITION))));

            } while (cursor.moveToNext());
        }
        userCount = cursor.getCount();
        cursor.close();
        database.close();

        return userCount;

    }

    public int updateJobInformation(int userId, int jobIndex){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JoblessDatabaseDescription.JOB_INDEX, 1);
        values.put(JoblessDatabaseDescription.JOB_NAME, "Job Name Here");
        values.put(JoblessDatabaseDescription.POSITION, "Job Position Here");
        values.put(JoblessDatabaseDescription.FIRST_DAY, "07/01/2008");
        values.put(JoblessDatabaseDescription.LAST_DAY, "12/30/2017");
        values.put(JoblessDatabaseDescription.PAYMENT_TYPE, "Payment Type Here");
        values.put(JoblessDatabaseDescription.EMPLOYMENT_STATUS, "Employ Status Goes Here");
        values.put(JoblessDatabaseDescription.DONE_WITH_THIS_JOB, 1);
        values.put(JoblessDatabaseDescription.USER_ID, 1);
        String whereClause = JoblessDatabaseDescription.JOB_INDEX +"=? AND "+ JoblessDatabaseDescription.USER_ID + "=?";
        String whereArgs[] = {String.valueOf(jobIndex), String.valueOf(userId)};
        int update = database.update(JoblessDatabaseDescription.JOB_TABLE, values, whereClause, whereArgs);
        database.close();

        return update;

    }

    public int deleteJob(int jobIndex, int userId) {
        database = dbHelper.getWritableDatabase();
        String whereClause = JoblessDatabaseDescription.JOB_INDEX +"=? AND "+ JoblessDatabaseDescription.USER_ID + "=?";
        String whereArgs[] = {String.valueOf(jobIndex), String.valueOf(userId)};
        int delete =  database.delete(JoblessDatabaseDescription.JOB_TABLE, whereClause, whereArgs);
        database.close();

        return delete;
    }



    //------------------------------------------------------------------------------------------
    //working with the Income Table
    //------------------------------------------------------------------------------------------


    public long insertIncomeInformation(int jobIndex, int incomeIndex){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JoblessDatabaseDescription.JOB_ID, jobIndex);
        values.put(JoblessDatabaseDescription.PERIOD, "Job Period Here");
        values.put(JoblessDatabaseDescription.INCOME, "Job Income Here");
        values.put(JoblessDatabaseDescription.INCOME_INDEX, incomeIndex);
        long insert = database.insert(JoblessDatabaseDescription.INCOME_TABLE, null, values);
        database.close();

        return insert;

    }

    public int selectIncomeInformation(int userId, int jobIndex){
        database = dbHelper.getReadableDatabase();
        int userCount = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + JoblessDatabaseDescription.INCOME_TABLE +", "+
                JoblessDatabaseDescription.JOB_TABLE + " WHERE " + JoblessDatabaseDescription.USER_ID + " = "+ userId +
                " AND " + JoblessDatabaseDescription.JOB_ID + " = " + jobIndex, new String[]{});
        if (cursor.moveToFirst()) {
            do {

                //incomeName = ((cursor.getString(cursor.getColumnIndex(JoblessDatabaseDescription.INCOME))));

            } while (cursor.moveToNext());
        }
        userCount = cursor.getCount();
        cursor.close();
        database.close();

        return userCount;

    }

    public int updateIncomeInformation(int jobIndex, int incomeIndex){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JoblessDatabaseDescription.JOB_ID, jobIndex);
        values.put(JoblessDatabaseDescription.PERIOD, "Job Period Here");
        values.put(JoblessDatabaseDescription.INCOME, "Job Income Here");
        values.put(JoblessDatabaseDescription.INCOME_INDEX, incomeIndex);
        String whereClause = JoblessDatabaseDescription.JOB_ID +"=? AND "+ JoblessDatabaseDescription.INCOME_INDEX + "=?";
        String whereArgs[] = {String.valueOf(jobIndex), String.valueOf(incomeIndex)};
        int update = database.update(JoblessDatabaseDescription.INCOME_TABLE, values, whereClause, whereArgs);
        database.close();

        return update;

    }

    public int deleteIncome(int jobIndex, int incomeIndex) {
        database = dbHelper.getWritableDatabase();
        String whereClause = JoblessDatabaseDescription.JOB_ID +"=? AND "+ JoblessDatabaseDescription.INCOME_INDEX + "=?";
        String whereArgs[] = {String.valueOf(jobIndex), String.valueOf(incomeIndex)};
        int delete =  database.delete(JoblessDatabaseDescription.INCOME_TABLE, whereClause, whereArgs);
        database.close();

        return delete;
    }

    //------------------------------------------------------------------------------------------
    //deleting a table
    //------------------------------------------------------------------------------------------

    public void deleteTable(String tableName){
        database = dbHelper.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + tableName);
        database.close();

    }
}
