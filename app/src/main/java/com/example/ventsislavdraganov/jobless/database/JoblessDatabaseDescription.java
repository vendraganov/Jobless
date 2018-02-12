package com.example.ventsislavdraganov.jobless.database;

/**
 * Created by ventsislavdraganov on 12/20/17.
 */

public class JoblessDatabaseDescription {

    //creating the table JoblessDatabaseQueries
    public static final String USER_TABLE = "userTable";
    public static final String KEY_ID = "estimateStatus";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String GENDER = "Gender";
    public static final String DOB = "DateOfBirth";
    public static final String BASE_YEAR = "BaseYear";
    public static final String WEEKS_TO_COLLECT = "WeeksToCollect";
    public static final String WEEKLY_BENEFIT = "WeeklyBenefit";
    public static final String TOTAL_BENEFIT = "TotalBenefit";
    public static final String SAVED = "Saved";
    public static final String NOT_ELIGIBLE_FOR_BENEFIT ="NotEligibleForBenefit";
    public static final String ESTIMATE_COMPLETE ="estimateComplete";
    private static final String USER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            USER_TABLE + " ( " +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            FIRST_NAME + " VARCHAR(50), "+
            LAST_NAME +" VARCHAR(50) ,"+
            GENDER + " VARCHAR(50) ,"+
            DOB + " VARCHAR(50) ,"+
            BASE_YEAR + " VARCHAR(50) ,"+
            WEEKS_TO_COLLECT + " INTEGER ,"+
            WEEKLY_BENEFIT + " REAL ,"+
            TOTAL_BENEFIT + " REAL ," +
            SAVED + " INTEGER ,"+
            NOT_ELIGIBLE_FOR_BENEFIT + " INTEGER, " +
            ESTIMATE_COMPLETE + " INTEGER " +
            ")";


    //creating the table Job
    public static final String JOB_TABLE = "jobTable";
    public static final String JOB_INDEX = "jobIndex";
    public static final String JOB_NAME = "jobName";
    public static final String POSITION = "position";
    public static final String FIRST_DAY = "startDay";
    public static final String LAST_DAY = "lastDay";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String EMPLOYMENT_STATUS = "employmentStatus";
    public static final String DONE_WITH_THIS_JOB = "doneWithThisJob";
    public static final String USER_ID = "userId"; //Foreign Key - we can not insert job if the key does not match any of the users id's;
    private static final String JOB_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            JOB_TABLE + " ( " +
            JOB_INDEX + " INTEGER PRIMARY KEY, " +
            JOB_NAME + " VARCHAR(50), "+
            POSITION +" VARCHAR(50) ,"+
            FIRST_DAY + " VARCHAR(50) ,"+
            LAST_DAY + " VARCHAR(50) ,"+
            PAYMENT_TYPE + " VARCHAR(50) ,"+
            EMPLOYMENT_STATUS + " VARCHAR(50) ,"+
            DONE_WITH_THIS_JOB + " INTEGER ," +
            USER_ID + " INTEGER, " +
            "FOREIGN KEY(" + USER_ID+") REFERENCES " + USER_TABLE + "("+KEY_ID+"))";


    //creating the table Income
    public static final String INCOME_TABLE = "incomeTable";
    public static final String JOB_ID = "jobId";
    public static final String PERIOD = "period";
    public static final String INCOME = "income";
    public static final String INCOME_INDEX ="incomeIndex";
    private static final String INCOME_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            INCOME_TABLE + " ( " +
            INCOME_INDEX + " INTEGER PRIMARY KEY, " +
            PERIOD+ " VARCHAR(50), "+
            INCOME +" VARCHAR(50) ,"+
            JOB_ID + " INTEGER, " +
            "FOREIGN KEY(" + JOB_ID+") REFERENCES " + JOB_TABLE + "("+JOB_INDEX+"))";





 public static String createTableUser(){
     return USER_TABLE_CREATE;
 }

    public static String createTableJob(){
        return JOB_TABLE_CREATE;
    }

    public static String createTableIncome(){
        return INCOME_TABLE_CREATE;
    }
}
