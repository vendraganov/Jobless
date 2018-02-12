package com.example.ventsislavdraganov.jobless;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ventsislavdraganov on 11/6/17.
 */

public class JoblessGlobal extends Application{


    private String firstName;//database
    private String lastName;//database
    private String gender;//database
    private String dateOfBirth;//database
    private String baseYear; //database
    private int weeksToCollect;//database
    private double weeklyBenefit;     //database
    private double totalBenefit;     //database
    private boolean saved = false; //database
    private boolean notEligibleForBenefit = false; //database
    private boolean estimateComplete = false;// database
    private ArrayList<String> jobsNames; //database Job_Table
    private Map<Integer, Job> jobs; // create from database
    private  int totalJobs;        //create from database



    private static JoblessGlobal instance;
    private int[] baseWeeksCount; //array to hold the base weeks for all jobs
    private int adapterItemJobNamePosition;
    private String userDate;
    private boolean userDateChosen = false;
    private boolean jobExist;
    protected int requestCode;
    protected final int NEW_ESTIMATE = 0;
    protected final int DRAFT_ESTIMATE = 1; //database user id
    protected final int COMPLETE_ESTIMATE = 3; //database user id
    protected final int EDIT_ESTIMATE = 2;
    private final int BASE_WEEKS = 52;
    private final double BASE_WEEK_MIN_INCOME = 168.00;
    private final double PERCENT_AMOUNT_BENEFIT = 0.6;
    private final int MAXIMUM_WEEKS_TO_COLLECT = 26;
    private View view; //we are using this to modify the adapter View for specific item
    //fields to calculate the unemployment compensation



    //Creating the Jobless Object all the fields that will be loaded from the database
    @Override
    public void onCreate() {
        super.onCreate();
        //default value for our fields;
        firstName = "";
        lastName = "";
        gender = "";
        dateOfBirth ="";
        userDate ="";
        baseYear ="";
        totalJobs =0;
        jobs = new HashMap<Integer, Job>();
        jobsNames = new ArrayList<String>();
        weeksToCollect =0;
        weeklyBenefit =0.0;
        totalBenefit =0.0;
        instance = this;
    }

    public static JoblessGlobal getInstance(){
        return instance;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getDateOfBirth(){
        return dateOfBirth;
    }

    public String getGender(){return gender;}

    public boolean getSavedStatus(){
        return saved;
    }

    public int getSavedStatusDatabase(){
        if(saved){
            return 1;
        }
        else{
            return 0;
        }
    }

    public int getEstimateCompleteDatabase(){
        if(estimateComplete){
            return 1;
        }
        else{
            return 0;
        }
    }

    public int getNotEligibleForBenefitDatabase(){
        if(notEligibleForBenefit){
            return 1;
        }
        else{
            return 0;
        }
    }

    public int getWeeksToCollect(){
        return weeksToCollect;
    }

    public double getWeeklyBenefit(){
        return weeklyBenefit;
    }

    public double getTotalBenefit(){
        return totalBenefit;
    }

    public int getAdapterItemJobPosition(){return adapterItemJobNamePosition;}

    public String getAdapterItemJobNamePosition(){return jobsNames.get(adapterItemJobNamePosition);}

    public Map<Integer, Job> getJobsInstance(){
        return jobs;
    }

    public boolean isJobExist(){return jobExist;}
    public boolean isEstimateComplete(){return estimateComplete;}

    public int getTotalJobs(){
        return totalJobs;
    }

    public View getAdapterItemView(){return view;}

    public String getBaseYear(){return baseYear;}

    public boolean getIsNotEligibleForBenefit(){return notEligibleForBenefit;}

    //public boolean getUserDateChosen(){return userDateChosen;}

    public void setSavedStatus(boolean saved){
        this.saved = saved;
    }

    public void setSavedStatusDatabase(int status){
        if(status == 1){
            saved = true;
        }
        else if(status == 0){saved = false;}
    }

    public void setEstimateCompleteDatabase(int status){
        if(status == 1){
            estimateComplete = true;
        }
        else if(status == 0){estimateComplete = false;}
    }

    public void setNotEligibleForBenefitDatabase(int status){
        if(status == 1){
            notEligibleForBenefit = true;
        }
        else if(status == 0){notEligibleForBenefit = false;}
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setTotalJobs(int total){
        totalJobs = total;
    }

    public void setAdapterItemView(View view){this.view = view;}

    public void setAdapterItemJobNamePosition(int itemPosition){adapterItemJobNamePosition = itemPosition;}

    //bug!!!
    public void setUserDate(String date){
        if(date == null){
            userDateChosen = false;
        }
        else {
            userDate = date;
            userDateChosen = true;
        }
    }

    public void setBaseWeeksCount(double[] incomes){
        if(incomes!=null && incomes.length == BASE_WEEKS) {
             baseWeeksCount = new int[BASE_WEEKS];
            for (int i = 0; i < baseWeeksCount.length; i++) {
                if (incomes[i] > BASE_WEEK_MIN_INCOME) {
                    baseWeeksCount[i] += 1;
                }
            }
        }
    }


    //setting the names of the job and getting the names
    public void setJobsNames(ArrayList<String> jobsNamesToSave){
        jobsNames.clear();
        jobsNames.addAll(jobsNamesToSave);
    }
    public ArrayList<String> getJobsNames(){

            return new ArrayList<String>(jobsNames);
        }
    //getting a reference to the jobsNames Object
    public ArrayList<String> getJobsNamesInstance(){
        return jobsNames;
    }

    //creating the map with the job name as key and Job object as values
    public Job getJobObject(int key){
        //getting the name of the job and creating the Job object
      if(!jobs.containsKey(key)){
          jobs.put(key, new Job());
          jobExist = false;
        }
       else{
          jobExist = true;
      }

      return jobs.get(key);
    }


    public void updateMap(){
        int countNames = jobsNames.size();
        int countJobs = jobs.size();
        while(countJobs > countNames){
            jobs.remove(countJobs-1);
            countJobs = jobs.size();
        }
    }

    //methods for working with the data entered by the user


    //clear all the fields in case the user when back to the main activity
    public void clearAllFields(){
        firstName ="";
        lastName ="";
        dateOfBirth ="";
        gender ="";
        baseYear="";
        userDate ="";
        userDateChosen = false;
        baseWeeksCount = null;
        totalJobs =0;
        jobs.clear();
        jobsNames.clear();
        weeksToCollect =0;
        weeklyBenefit =0.0;
        totalBenefit =0.0;
    }

    //checking if we are done with the job and placing the symbol
    public boolean isJobInfoComplete(int index){
        boolean done = false;
        if (jobs.get(index) != null && jobs.get(index).getDoneWithThisJob() ) {
            done = true;
        }
       return done;
    }

    //check if we are done with all the jobs
    public boolean isAllJobsInfoComplete(){

        if(!jobs.isEmpty()) {
            int count = 0;
            int index = 0;
            while (index < jobs.size()) {
                if (jobs.get(index) != null && jobs.get(index).getDoneWithThisJob() ) {
                        count++;
                }
                index++;
            }
            if (index == count && jobs.size()== jobsNames.size()) {
                return true;
            }
        }
        return false;
    }

    //calculating the final result. Will be call when the user clicks estimate Unemployment button
    public void estimateUnemploymentCompensation() {

        estimateComplete = true;
        weeksToCollect = 0;
        int index = 0;
        double total = 0.0;
        if (baseWeeksCount != null) {
            for (int week : baseWeeksCount) {
                if (week > 0) {
                    weeksToCollect++;
                }
            }
        }
        if (!jobs.isEmpty()) {
            ArrayList<Job> jobsToCheck = new ArrayList<Job>(jobs.values());
            while (index < jobsToCheck.size()) {
                if (jobsToCheck.get(index) != null) {
                    total = jobsToCheck.get(index).getTotalIncomeForJob();

                }
                index++;
            }

        }

        if(!(weeksToCollect>20 || total> 8400)){
            notEligibleForBenefit = true;
        }

        if(weeksToCollect>26){
            weeksToCollect = MAXIMUM_WEEKS_TO_COLLECT;
        }

        weeklyBenefit = total/weeksToCollect*PERCENT_AMOUNT_BENEFIT;
        totalBenefit = weeklyBenefit*weeksToCollect;


    }

    //methods related to the UI

    //showing alert message
    public AlertDialog alertDialog(final Context context, String message, boolean addDelay, boolean saveIn, final boolean exit ){
        String buttonText = saveIn? getString(R.string.save): getString(R.string.ok);
        final boolean save = saveIn;
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(getString(R.string.alert_message));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 if(save) {
                     saved = true;
                     saveDraft();
                 }
                 if(exit){
                     saveDraft();
                     System.exit(0);
                 }


            }
        });
        if(save) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                        saved = false;
                        if(exit){
                            System.exit(0);
                        }
                }
            });
        }
        alertDialog.setCancelable(false);
        //delaying the alert dialog with 1.5 second
        if(addDelay) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.show();
                }
            }, 1200);
        }
        else {
            alertDialog.show();
        }
        return alertDialog;
    }


    //method which is call when we want to hide the keyboard
    public void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    //creating and showing a toast message
    public void displayMessage(Context context, String message){
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //calculating the unemployment compensation methods
    public void createBaseYear(){
        Calendar calendar = Calendar.getInstance();
        int year = userDateChosen? Integer.parseInt(userDate.split("/")[2]) : calendar.get(Calendar.YEAR);
        int month = userDateChosen? Integer.parseInt(userDate.split("/")[0]): (calendar.get(Calendar.MONTH) +1);
        if(1<=month&&month<=3){
            baseYear = "10/01/" + (year -2)+ " - 09/30/" + (year-1);
        }
        //second quarter
        else if(4<=month&&month<=6){
            baseYear = "01/01/" +(year-1)+" - 12/31/" + (year -1);
        }
        //third quarter
        else if(7<=month&&month<=9){
            baseYear = "04/01/" +(year-1)+" - 03/31/" + year;
        }
        //forth quarter
        else if(10<=month&&month<=12) {
            baseYear = "07/01/" + (year - 1)+ " - 06/30/" + year;
        }
    }

    public void saveDraft(){
        //if the map is null they are no job objects we do not save anything in job table

    }

    public void savedEstimate(){

    }

    //call by the new estimate
    public void loadDraft(){

    }

    //call by the previous estimate
    public void loadEstimate(){


    }




}
