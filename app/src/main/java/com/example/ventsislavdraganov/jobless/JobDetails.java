package com.example.ventsislavdraganov.jobless;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ventsislavdraganov on 10/22/17.
 */

public class JobDetails extends AppCompatActivity {
    private TextView jobInfoTextView;
    private EditText position;
    private EditText startDateEditText;
    private EditText lastDateEditText;
    private LinearLayout paymentTypeLinearLayout;
    private GridLayout employmentStatusGridLayout;
    private String paymentType;
    private String employmentStatus;
    private boolean testDate = true;
    private boolean testDate1 = true;
    private boolean paymentTypeSelect = false;
    private JoblessGlobal joblessGlobal;
    private Job jobObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarJobDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jobInfoTextView = (TextView) findViewById(R.id.jobInfoTextView);
        position = (EditText) findViewById(R.id.positionEditText);
        startDateEditText = (EditText) findViewById(R.id.startDateInputEditText);
        startDateEditText.addTextChangedListener(editTextWatcher);
        lastDateEditText = (EditText) findViewById(R.id.lastDateInputEditText);
        lastDateEditText.addTextChangedListener(editTextWatcher);
        paymentTypeLinearLayout = (LinearLayout) findViewById(R.id.paymentTypeLinearLayout);
        addPaymentTypeListener();
        employmentStatusGridLayout = (GridLayout) findViewById(R.id.employmentStatusGridLayout);
        addEmploymentStatusListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        joblessGlobal = JoblessGlobal.getInstance();
        String jobName = joblessGlobal.getAdapterItemJobNamePosition();
        if (jobName != null) {
            jobInfoTextView.setText(getString(R.string.income_label, jobName));
            jobObject = joblessGlobal.getJobObject(joblessGlobal.getAdapterItemJobPosition());
        }
        //load the information if the object exist
        if (joblessGlobal.isJobExist() && jobObject != null) {
            loadUserInformation();
        }

    }

    //loading the saved information
    private void loadUserInformation() {
        //if its the default value we are setting hint
        if (jobObject.getPosition().isEmpty()) {
            position.setHint(R.string.unknown);
        } else {
            position.setText(jobObject.getPosition());
        }

        if (jobObject.getStartDayOfWork().isEmpty()) {
            startDateEditText.setHint(R.string.unknown);
            startDateEditText.setOnFocusChangeListener(dateHintShow);
        } else {
            startDateEditText.setText(jobObject.getStartDayOfWork());
        }
        if (jobObject.getLastDayOfWork().isEmpty()) {
            lastDateEditText.setHint(R.string.unknown);
            lastDateEditText.setOnFocusChangeListener(dateHintShow);
        } else {
            lastDateEditText.setText(jobObject.getLastDayOfWork());
        }
        //selecting the buttons which are equal to what the user which he selected previously, if any
        if(!jobObject.getPaymentType().isEmpty()) {
            for (int i = 0; i < paymentTypeLinearLayout.getChildCount(); i++) {
                Button button = (Button) paymentTypeLinearLayout.getChildAt(i);
                if (button.getText().toString().equals(jobObject.getPaymentType())) {
                    button.setEnabled(false);
                }
            }
            paymentTypeSelect = true;
        }

        if(!jobObject.getEmploymentStatus().isEmpty()) {
            for (int i = 0; i < employmentStatusGridLayout.getChildCount(); i++) {
                Button button = (Button) employmentStatusGridLayout.getChildAt(i);
                if (button.getText().toString().equals(jobObject.getEmploymentStatus())) {
                    button.setEnabled(false);
                }
            }
        }

    }

    //saving the information that the user enter
    private void userInformationReceived() {

        jobObject.setPosition(position.getText().toString());
        jobObject.setStartDayOfWork(startDateEditText.getText().toString());
        jobObject.setLastDayOfWork(lastDateEditText.getText().toString());
        if(employmentStatus != null) {
            jobObject.setEmploymentStatus(employmentStatus);
        }
        if(paymentType !=null) {
            jobObject.setPaymentType(paymentType);
        }

    }

    private boolean isDateValid(String date , boolean lastDay) {
        if(date != null && !date.isEmpty()) {
            final String DATE_FORMAT = "MM/dd/yyyy";
            String firstDataOfBaseYear = joblessGlobal.getBaseYear().trim().split("-")[0];
            String lastDataOfBaseYear = joblessGlobal.getBaseYear().trim().split("-")[1];

            try {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                dateFormat.setLenient(false);
                Date enteredDate = dateFormat.parse(date); //if date is incorrect throws exception
                Date firstDateBaseYear= dateFormat.parse(firstDataOfBaseYear);
                Date lastDateBaseYear = dateFormat.parse(lastDataOfBaseYear);
                if(enteredDate.before(firstDateBaseYear)&& lastDay){
                    joblessGlobal.displayMessage(this, getString(R.string.last_date_job_does_not_qualify, firstDataOfBaseYear));
                    return false;
                }
                else if(enteredDate.after(lastDateBaseYear)&& !lastDay){
                    joblessGlobal.displayMessage(this, getString(R.string.first_date_job_does_not_qualify, lastDataOfBaseYear));
                    return false;
                }
                return true;

            } catch (ParseException e) {
                joblessGlobal.displayMessage(this, lastDay? getString(R.string.invalid_date_last_day):getString(R.string.invalid_date_first_day));
                return false;
            }
        }
        else{
            joblessGlobal.displayMessage(this, lastDay? getString(R.string.enter_last_date):getString(R.string.enter_first_date));
            return false;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.job_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        userInformationReceived();
        //moving to the Income Activity
        if (item.getItemId() == R.id.job_details_menu_next && isDateValid(startDateEditText.getText().toString(), false)
                && isDateValid(lastDateEditText.getText().toString(), true) && paymentTypeSelect) {
            Intent intent = new Intent(this, Income.class);
            startActivity(intent);
        }
       else if (item.getItemId() == R.id.job_details_menu_next && isDateValid(startDateEditText.getText().toString(),false)
               && isDateValid(lastDateEditText.getText().toString(), true) && !paymentTypeSelect) {
            joblessGlobal.displayMessage(this, getString(R.string.select_payment_type));
        }
        else if (item.getItemId() == R.id.job_details_menu_exit){
            joblessGlobal.alertDialog(this, getString(R.string.save_information), false, true, true);

        }
        else if(item.getItemId()==R.id.job_details_menu_save){
            joblessGlobal.setSavedStatus(true);
            joblessGlobal.saveDraft();
        }

        return super.onOptionsItemSelected(item);
    }

    //listener for the payment type
    private final View.OnClickListener paymentTypeSelected = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i < paymentTypeLinearLayout.getChildCount(); i++) {
                paymentTypeLinearLayout.getChildAt(i).setEnabled(true);
            }
            paymentTypeSelect = true;
            Button button = (Button) view;
            paymentType = button.getText().toString();
            button.setEnabled(false);
        }
    };

    //adding the listener to the buttons
    private void addPaymentTypeListener() {
        for (int i = 0; i < paymentTypeLinearLayout.getChildCount(); i++) {
            paymentTypeLinearLayout.getChildAt(i).setOnClickListener(paymentTypeSelected);
        }
    }

    //listener for the employment status
    private final View.OnClickListener employmentStatusListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i < employmentStatusGridLayout.getChildCount(); i++) {
                employmentStatusGridLayout.getChildAt(i).setEnabled(true);
            }
            Button button = (Button) view;
            employmentStatus = button.getText().toString();
            button.setEnabled(false);
        }
    };

    //adding the listener to the buttons
    private void addEmploymentStatusListener() {
        for (int i = 0; i < employmentStatusGridLayout.getChildCount(); i++) {
            employmentStatusGridLayout.getChildAt(i).setOnClickListener(employmentStatusListener);
        }
    }


    //setting the date hint in case the user did not enter a date
   private final View.OnFocusChangeListener dateHintShow = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (view.getId() == R.id.lastDateInputEditText && hasFocus) {
                lastDateEditText.setHint(R.string.date_type_hint);
            } else if (view.getId() == R.id.startDateInputEditText && hasFocus) {
                startDateEditText.setHint(R.string.date_type_hint);
            }
        }
    };

        //text watcher for setting the dates style and saving the dates
        private final TextWatcher editTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                try {
                    //indicating if the user deleted the backslash and setting the boolean to true
                    //which will allow to insert a backslash again
                    if (charSequence.length() < 2) {
                        testDate = true;
                    }
                    if (charSequence.length() < 5) {
                        testDate1 = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //inserting the backslash in the date text and setting the boolean to false which will be enable the deleting
                //of the backslash
                if (testDate) {
                    if (editable.length() == 2) {
                        editable.append("/");
                        testDate = false;
                    }
                }
                if (testDate1) {
                    if (editable.length() == 5) {
                        editable.append("/");
                        testDate1 = false;
                    }
                }
            }
        };


    }
