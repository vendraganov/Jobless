package com.example.ventsislavdraganov.jobless;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ventsislavdraganov on 10/20/17.
 */

public class PreviousEstimate extends AppCompatActivity {
    private JoblessGlobal joblessGlobal;
    private final String NO_ESTIMATE = "$0.00";
    private final String NO_WEEKS = "0";
    private TextView displayFirstName;
    private TextView displayLastName;
    private TextView displayAge;
    private TextView displayGender;
    private TextView unemploymentCompensationLabel;
    private TextView weeksToCollect;
    private TextView weeklyBenefit;
    private TextView totalBenefit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_estimate);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarLastEstimate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        displayFirstName = (TextView)findViewById(R.id.displayFirstNameTextView);
        displayLastName = (TextView)findViewById(R.id.displayLastNameTextView);
        displayAge = (TextView)findViewById(R.id.displayAgeTextView);
        displayGender = (TextView) findViewById(R.id.displayGenderTextView);
        unemploymentCompensationLabel = (TextView)findViewById(R.id.currentUnemploymentCompensationLabel);
        unemploymentCompensationLabel.setText(getString(R.string.current_compensation, ""));
        weeksToCollect = (TextView)findViewById(R.id.displayTotalWeeksToCollectTextView);
        weeklyBenefit = (TextView)findViewById(R.id.displayWeeklyBenefitTextView);
        totalBenefit = (TextView)findViewById(R.id.displayTotalBenefitTextView);
        joblessGlobal = JoblessGlobal.getInstance();
        if(joblessGlobal.getIsNotEligibleForBenefit() && joblessGlobal.isEstimateComplete()){
            unemploymentCompensationLabel.setText(R.string.not_eligible_for_benefit);
            weeksToCollect.setVisibility(View.GONE);
            weeklyBenefit.setVisibility(View.GONE);
            totalBenefit.setVisibility(View.GONE);
        }
        else if(joblessGlobal.requestCode == joblessGlobal.COMPLETE_ESTIMATE){
            loadEstimateResults(joblessGlobal.isEstimateComplete());
        }
        else if(joblessGlobal.requestCode == joblessGlobal.EDIT_ESTIMATE){
            joblessGlobal.loadEstimate();
            //TODO load the database
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.previous_estimate_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.previous_menu_edit){
                Intent intent = new Intent(this,NewEstimate.class);
                joblessGlobal.requestCode = joblessGlobal.EDIT_ESTIMATE;
                startActivity(intent);
                return true;

        }
        if (item.getItemId() == R.id.previous_menu_exit){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.alert_message));
            alertDialog.setMessage(getString(R.string.save_information));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(joblessGlobal.isEstimateComplete()) {
                                joblessGlobal.setSavedStatus(false);
                                joblessGlobal.savedEstimate();
                            }
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        joblessGlobal.setSavedStatus(false);
                            System.exit(0);

                    }
                });
            alertDialog.setCancelable(false);
            alertDialog.show();
          return true;
        }
        if(item.getItemId()==R.id.previous_menu_save){
            if(joblessGlobal.isEstimateComplete()) {
                joblessGlobal.setSavedStatus(false);
                joblessGlobal.savedEstimate();
            }
            return true;
        }
        if(joblessGlobal.isEstimateComplete()) {
            joblessGlobal.setSavedStatus(false);
            joblessGlobal.savedEstimate();
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadEstimateResults(boolean isEstimateComplete){
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            displayFirstName.setText(joblessGlobal.getFirstName());
            displayLastName.setText(joblessGlobal.getLastName());
            displayGender.setText(joblessGlobal.getGender());
            displayAge.setText(getAge(joblessGlobal.getDateOfBirth()));
            unemploymentCompensationLabel.setText(joblessGlobal.getBaseYear());
            if (isEstimateComplete){
                weeksToCollect.setText(String.valueOf(joblessGlobal.getWeeksToCollect()));
                weeklyBenefit.setText("$"+decimalFormat.format(joblessGlobal.getWeeklyBenefit()));
                totalBenefit.setText("$" + decimalFormat.format(joblessGlobal.getTotalBenefit()));
            } else {
                weeksToCollect.setText(NO_WEEKS);
                weeklyBenefit.setText(NO_ESTIMATE);
                totalBenefit.setText(NO_ESTIMATE);
            }

    }

    private String getAge(String date) {
        if(date.isEmpty()){
            return "";
        }
        else {
            final String DATE_FORMAT = "MM/dd/yyyy";
            try {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                dateFormat.setLenient(false);
                dateFormat.parse(date); //if date is incorrect throws exception
                int day = Integer.parseInt(date.split("/")[1]);
                int month = Integer.parseInt(date.split("/")[0]) - 1;
                int year = Integer.parseInt(date.split("/")[2]);
                Calendar calendar = new GregorianCalendar(year, month, day);
                Calendar now = new GregorianCalendar();
                int age = now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
                if ((calendar.get(Calendar.MONTH) > now.get(Calendar.MONTH)) ||
                        (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                                (calendar.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH)))) {
                    age--;
                }
                return String.valueOf(age);


            } catch (ParseException e) {
                return "";
            }


        }
    }


}
