package com.example.ventsislavdraganov.jobless;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by ventsislavdraganov on 10/28/17.
 */

public class Income extends AppCompatActivity {
    private TextView enterIncomeTextViewLabel;
    private ListView incomePeriodsListView;
    private Button doneWithThisNameButton;
    private ArrayList<IncomeView> displayPeriods;
    private CustomIncomeAdapter adapter;
    private JoblessGlobal joblessGlobal;
    private Job jobObject;
    private String jobName;
    private final double NO_INCOME_ENTERED = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarIncome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        joblessGlobal = JoblessGlobal.getInstance();
        enterIncomeTextViewLabel = (TextView)findViewById(R.id.enterIncomeTextViewLabel);
        doneWithThisNameButton = (Button)findViewById(R.id.doneWithThisNameButton);
        doneWithThisNameButton.setOnClickListener(doneWithThisNameButtonClicked);
        displayPeriods = new ArrayList<IncomeView>();
        adapter = new CustomIncomeAdapter(this,displayPeriods);
        incomePeriodsListView = (ListView)findViewById(R.id.incomeListView);
        incomePeriodsListView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        joblessGlobal = JoblessGlobal.getInstance();
        jobName = joblessGlobal.getAdapterItemJobNamePosition();
        enterIncomeTextViewLabel.setText(getString(R.string.click_to_enter_income, jobName));
        doneWithThisNameButton.setText(getString(R.string.done_with_this_job, jobName));
        jobObject = joblessGlobal.getJobObject(joblessGlobal.getAdapterItemJobPosition());
        //creating the array with the periods to be display
        if(jobObject.getIncomePeriod() == null||
                (jobObject.getIncomePeriod() != null && jobObject.getIncomePeriod().length != jobObject.getPaymentTypeInt(this)) ||
                jobObject.getDisplayPeriods().isEmpty()) {
            jobObject.createIncomePeriodsArrays(this, joblessGlobal.getBaseYear());
            displayPeriodsList();

        }
        else {
            loadUserInformation();
        }

    }

    private void displayPeriodsList(){
        if(jobObject.getIncomePeriod() != null) {
            for (int index = 0; index < jobObject.getIncomePeriod().length; index++) {
            displayPeriods.add(index, new IncomeView(jobObject.getIncomePeriod()[index], null));
            }
        }
        adapter.notifyDataSetChanged();
    }


    //saving the information that the user enter
    private void userInformationReceived(){
        jobObject.setDisplayPeriods(displayPeriods);
    }

    //loading the user save information
    private void loadUserInformation(){
        displayPeriods.addAll(jobObject.getDisplayPeriods());
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.income_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        userInformationReceived();
        if (item.getItemId() == R.id.job_income_menu_exit){
            joblessGlobal.alertDialog(this, getString(R.string.save_information), false, true, true);

        }
        else if(item.getItemId()==R.id.job_income_menu_save){
            joblessGlobal.setSavedStatus(true);
            joblessGlobal.saveDraft();
        }

        return super.onOptionsItemSelected(item);
    }

    //listener for the submit button, this button will be available only if the user enter
    //all the income periods
    private final View.OnClickListener doneWithThisNameButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            userInformationReceived();
            jobObject.setDoneWithThisJob();
            jobObject.createIncomeArray(jobObject.getPaymentTypeInt(view.getContext()));
            int index = 0;
            while(index < displayPeriods.size()){
                if(displayPeriods.get(index).income == null){
                    jobObject.saveIncomePeriod(NO_INCOME_ENTERED, index);
                }
                else{
                    jobObject.saveIncomePeriod(Double.parseDouble(displayPeriods.get(index).income), index);
                }
                index++;
            }
            joblessGlobal.setBaseWeeksCount(jobObject.createBaseWeeks(view.getContext()));
            View adapterItemView = joblessGlobal.getAdapterItemView();
            if(adapterItemView != null) {
                ImageView doneSign = (ImageView) adapterItemView.findViewById(R.id.doneSign);
                doneSign.setVisibility(View.VISIBLE);
            }
            Intent intent = new Intent(view.getContext(), JobNames.class);
            startActivity(intent);

        }
    };


}
