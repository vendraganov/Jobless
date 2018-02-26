package com.example.ventsislavdraganov.jobless;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.example.ventsislavdraganov.jobless.R;

/**
 * Created by ventsislavdraganov on 11/12/17.
 */

public class Job {

    private String position; //database Job
    private String startDayOfWork; //database Job
    private String lastDayOfWork; //database Job
    private String paymentType;  //database Job
    private String employmentStatus; //database Job
    private boolean doneWithThisJob; //database Job
    private ArrayList<IncomeView> displayPeriods; //create from the database

    private String[] incomePeriod;
    private double[] incomes;
    private double[] baseWeeks;
    private final int BASE_WEEKS = 52;
    private final int MONTHLY_PERIOD = 12;
    private final int WEEKLY_PERIOD = 52;
    private final int BIWEEKLY_PERIOD = 26;
    private ArrayList<Double> dayPeriod;




    public Job() {
        position = "";
        startDayOfWork = "";
        lastDayOfWork = "";
        paymentType = "";
        employmentStatus = "";
        doneWithThisJob = false;
        displayPeriods = new ArrayList<>();

    }

    public void setDisplayPeriods(ArrayList<IncomeView> displayPeriodsIn){
        displayPeriods.clear();
        displayPeriods.addAll(displayPeriodsIn);
    }
    public void setDoneWithThisJob(){doneWithThisJob = true;}

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStartDayOfWork(String startDayOfWork) {
        this.startDayOfWork = startDayOfWork;
    }

    public void setLastDayOfWork(String lastDayOfWork) {
        this.lastDayOfWork = lastDayOfWork;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setEmploymentStatus(String employmentStatus) {this.employmentStatus = employmentStatus;}

    public ArrayList<IncomeView> getDisplayPeriods(){
        return displayPeriods;
    }

    public boolean getDoneWithThisJob(){
        return doneWithThisJob;
    }

    public String getPosition() {
        return position;
    }

    public String getStartDayOfWork() {
        return startDayOfWork;
    }

    public String getLastDayOfWork() {
        return lastDayOfWork;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public int getPaymentTypeInt(Context context){
        if(paymentType.equals(context.getString(R.string.weekly_payment_type))){
            return WEEKLY_PERIOD;
        }
        else if(paymentType.equals(context.getString(R.string.biweekly_payment_type))){
            return BIWEEKLY_PERIOD;
        }
        else if(paymentType.equals(context.getString(R.string.monthly_payment_type))){
           return MONTHLY_PERIOD;
        }
        return -1;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public String[] getIncomePeriod(){return incomePeriod;}


    //creating and returning an array with the date incomeToDisplay periods
    public void createIncomePeriodsArrays(Context context , String baseYear) {

        int populatePeriods = 0;//count the populate periods
        if(paymentType.equals(context.getString(R.string.weekly_payment_type))){

            createIncomePeriods(52, baseYear);

        }
        else if(paymentType.equals(context.getString(R.string.biweekly_payment_type))){

            createIncomePeriods(26, baseYear);


        }
        else if(paymentType.equals(context.getString(R.string.monthly_payment_type))){
            createIncomePeriodsForMonthlyTypePayment(baseYear);


        }

    }
    //creating the date periods base on monthly type of payment
    private void createIncomePeriodsForMonthlyTypePayment(String baseYear){
        incomePeriod = new String[MONTHLY_PERIOD];// creating the array
        int populatePeriods = 0;//count the populate MONTHLY_PERIODS
        String secondPeriod;// period to be add to the first
        String startPeriod = baseYear.split("-")[0].trim();//getting the start period
        int startDay = Integer.parseInt(startPeriod.split("/")[1]);//getting the day
        int startMonth = Integer.parseInt(startPeriod.split("/")[0]);//getting the month
        int startYear = Integer.parseInt(startPeriod.split("/")[2]);//getting the year
        //creating the period
        while(populatePeriods < MONTHLY_PERIOD){

            switch(startMonth){
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12: { startDay= 31;break;}
                case 2:{
                    if(isLeapYear(startYear)){startDay = 29;}
                    else{startDay = 28;}
                    break;}
                case 4:
                case 6:
                case 9:
                case 11:{startDay = 30;break;}
            }

            secondPeriod = startMonth+"/"+startDay+"/"+startYear;
            startPeriod += "-" + secondPeriod;
            incomePeriod[populatePeriods] = startPeriod;
            startMonth++;
            if(startMonth > 12){
                startYear++;
                startMonth -= 12;

            }
            startDay = 1;
            startPeriod = startMonth+"/"+startDay+"/"+startYear;
            populatePeriods++;
        }

    }

    //creating the date period base on weekly or biweekly payment type
    private void createIncomePeriods(int period, String baseYear) {
        int INCREMENT = period > BIWEEKLY_PERIOD? 6 : 13; //change to 6 for 52 periods 13 for biweekly
        incomePeriod = new String[period];
        int populatePeriods = 0;//count the populate periods
        String secondPeriod; // period to be add to the first
        String startPeriod = baseYear.split("-")[0].trim();//getting the start period
        boolean matchLastDay;//if the last day match to the last day of the month we clear the day to 0! That way the next month will start with day 1
        int startDay = Integer.parseInt(startPeriod.split("/")[1]);//start period day
        int startMonth = Integer.parseInt(startPeriod.split("/")[0]);//start period month
        int startYear = Integer.parseInt(startPeriod.split("/")[2]);//start period year
        //populating the array base on the periods
        while(populatePeriods < period){
            startDay += INCREMENT;
            matchLastDay = false;

            switch(startMonth){
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                {   if(startDay > 31){
                    startMonth++;
                    startDay -= 31;
                }
                else if (startDay == 31){
                    matchLastDay = true;

                }
                    break;
                }
                case 2:{
                    if(isLeapYear(startYear)){
                        if(startDay>29){
                            startMonth++;
                            startDay -= 29;

                        }
                        else if(startDay == 29){
                            matchLastDay = true;

                        }
                    }
                    else{
                        if(startDay >28){
                            startMonth++;
                            startDay -= 28;

                        }
                        else if(startDay == 28){
                            matchLastDay = true;

                        }
                    }
                    break;

                }
                case 4:
                case 6:
                case 9:
                case 11:{
                    if(startDay > 30){
                        startMonth++;
                        startDay -= 30;
                    }
                    else if(startDay == 30){
                        matchLastDay = true;

                    }
                    break;
                }
            }
            if(startMonth > 12){
                startYear++;
                startMonth -= 12;

            }
            secondPeriod = startMonth+"/"+startDay+"/"+startYear;
            startPeriod += "-" + secondPeriod;
            incomePeriod[populatePeriods] = startPeriod;
            if(matchLastDay){
                startDay = 0;
                startMonth++;
            }
            startDay++;
            startPeriod = startMonth+"/"+startDay+"/"+startYear;
            populatePeriods++;
        }

    }

    // calculating the results Methods
    public void createIncomeArray(int period){
        if(period == MONTHLY_PERIOD){
            incomes = new double[MONTHLY_PERIOD];
            dayPeriod = new ArrayList<Double>(366);
        }
        else if (period == WEEKLY_PERIOD){
            incomes = new double[WEEKLY_PERIOD];
        }
        else if (period == BIWEEKLY_PERIOD){
            incomes = new double[BIWEEKLY_PERIOD];
        }
    }
    //storing the monthly payments and populating the dayPeriod array which we will create base on average day payment
    //this will be use to create the base weeks array. doing so would bring more accurance to our calculation
    public void saveIncomePeriod(double income , int position){
        if(incomes.length == MONTHLY_PERIOD) {
            String startPeriod =  incomePeriod[position].split("-")[0].trim();//getting the start period
            int month = Integer.parseInt(startPeriod.split("/")[0]);
            int year = Integer.parseInt(startPeriod.split("/")[2]);//start period year
            int index = 0;
            incomes[position] = income;

            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12: {
                    while (index < 31) {
                        dayPeriod.add(income / 31.0);
                        index++;
                    }
                    break;
                }
                case 2: {
                    if (isLeapYear(year)) {
                        while (index < 29) {
                            dayPeriod.add(income / 29.0);
                            index++;
                        }
                    } else {
                        while (index < 28) {
                            dayPeriod.add(income / 28.0);
                            index++;
                        }
                    }
                    break;
                }
                case 4:
                case 6:
                case 9:
                case 11: {
                    while (index < 30) {
                        dayPeriod.add(income / 30.0);
                        index++;
                    }
                    break;
                }
            }
        }
        else{
            incomes[position] = income;
        }


    }


    //creating the base weeks. this method will be call when the user clicks dane with "job name" button
    public double[] createBaseWeeks(Context context){
        //creating the base weeks when the payment is monthly
        baseWeeks = new double[BASE_WEEKS];

        if(paymentType.equals(context.getString(R.string.weekly_payment_type))){
            baseWeeks = incomes.clone();
        }
        else if(paymentType.equals(context.getString(R.string.biweekly_payment_type))){
                 int j =0;
                 for(double income: incomes){
                     baseWeeks[j] = income/2;
                     j++;
                     baseWeeks[j] = income/2;
                     j++;
                 }

        }
        else if(paymentType.equals(context.getString(R.string.monthly_payment_type))){
            int i = 0;
            double adjustArrayToCurrentWeekCount = 0.0;
            while(dayPeriod.size()>364){
                adjustArrayToCurrentWeekCount += dayPeriod.remove(dayPeriod.size()-1);
            }
            while(0<dayPeriod.size() && i < 52){
                int index = 0;
                double sum = 0.0;
                while(index<7 && !dayPeriod.isEmpty()){
                    sum+=dayPeriod.remove(0);
                    index ++;
                }
                sum+=adjustArrayToCurrentWeekCount/52;
                baseWeeks[i] = sum;

                i++;
            }

        }
        return baseWeeks;
    }
    //call by global to do final estimate
    public double getTotalIncomeForJob(){
        double total = 0.0;
        for (double income: incomes) {
            total += income;
        };
        return total;
    }



    private boolean isLeapYear(int year) {
        return ((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0);

    }
}
