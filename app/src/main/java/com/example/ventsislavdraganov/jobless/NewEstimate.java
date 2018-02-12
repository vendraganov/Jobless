package com.example.ventsislavdraganov.jobless;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by ventsislavdraganov on 10/20/17.
 */

public class NewEstimate extends AppCompatActivity{
    //variable references to: global class and dialog for entering the job's names
    private JoblessGlobal joblessGlobal;
    private DialogJobNames dialogJobNames;
    private boolean dialogCreated = false;
    //references to our elements in the UI
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private TextView datePickerTextView;
    private RadioGroup genderRadioGroup;
    private LinearLayout jobCount;
    private Button enterJobNamesButton;
    private String dobToDisplay;
    private String userGender;
    private String jobCountOverPlus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estimate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNewEstimate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText)findViewById(R.id.lastNameEditText);
        datePickerTextView = (TextView)findViewById(R.id.datePickerTextView);
        datePickerTextView.setOnClickListener(datePickerPressed);
        genderRadioGroup = (RadioGroup)findViewById(R.id.genderRadioGroup);
        genderRadioGroup.setOnCheckedChangeListener(genderSelectedListener);
        jobCount = (LinearLayout) findViewById(R.id.jobsCount);
        addListenerToJobCountButtons(jobCount);
        enterJobNamesButton = (Button) findViewById(R.id.enterJobNamesButton);
        enterJobNamesButton.setOnClickListener(showAlertDialogToEnterJobNames);
        GridLayout gridLayout =(GridLayout)findViewById(R.id.gridLayout);
        gridLayout.setOnTouchListener(onTouchListener); //hiding the keyboard
        joblessGlobal = JoblessGlobal.getInstance();
        onStartNewOrDraft();


}
    private void onStartNewOrDraft() {
        if(joblessGlobal.requestCode == joblessGlobal.NEW_ESTIMATE){
            joblessGlobal.clearAllFields();
            //creating the base year on which the jobs incomes will be based
            joblessGlobal.createBaseYear();

        }
        else if(joblessGlobal.requestCode == joblessGlobal.DRAFT_ESTIMATE){
                joblessGlobal.loadDraft();//populating the fields if we have any store data
                loadUserInformation();
            }
        else if( joblessGlobal.requestCode == joblessGlobal.EDIT_ESTIMATE) {
                 loadUserInformation();

            }
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_estimate_menu, menu);
        return true;
    }
   //Adding items to the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        userInformationReceived();
        if (item.getItemId() == R.id.toJobsMenu) {
            Intent intent = new Intent(this, JobNames.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            final Intent intent = new Intent(this, MainActivity.class);
            AlertDialog alertDialog = joblessGlobal.alertDialog(this, getString(R.string.save_information), false, true, false);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    startActivity(intent);
                }
            });
        return true;
        }
        else if (item.getItemId() == R.id.new_estimate_menu_exit){
            joblessGlobal.alertDialog(this, getString(R.string.save_information), false, true, true);
            return true;
        }
        else if(item.getItemId()==R.id.new_estimate_menu_save){
            joblessGlobal.setSavedStatus(true);
            joblessGlobal.saveDraft();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //saving the information that the user enter
    private void userInformationReceived(){
        joblessGlobal.setFirstName(firstNameEditText.getText().toString());
        joblessGlobal.setLastName(lastNameEditText.getText().toString());
        //if its null we are using the default values
        if(dobToDisplay!= null) {
            joblessGlobal.setDateOfBirth(dobToDisplay);
        }
        if(userGender != null) {
            joblessGlobal.setGender(userGender);
        }
    }
    //loading the saved data if the user start the activity by pressing the draft button
    private void loadUserInformation(){
        if(joblessGlobal.getFirstName().isEmpty()) {
            firstNameEditText.setHint(R.string.unknown);
        }else{
            firstNameEditText.setText(joblessGlobal.getFirstName());
        }
        if(joblessGlobal.getLastName().isEmpty()) {
            lastNameEditText.setHint(R.string.unknown);
        }else{
            lastNameEditText.setText(joblessGlobal.getLastName());
        }

        if(joblessGlobal.getDateOfBirth().isEmpty()) {
            datePickerTextView.setHint(R.string.unknown);
        }else{
            datePickerTextView.setText(joblessGlobal.getDateOfBirth());
        }
        //selecting the gender
        if(!joblessGlobal.getGender().isEmpty()) {
            if (joblessGlobal.getGender().equals(getString(R.string.male))) {
                ((RadioButton) findViewById(R.id.maleButton)).setChecked(true);
            } else if (joblessGlobal.getGender().equals(getString(R.string.female))) {
                ((RadioButton) findViewById(R.id.femaleButton)).setChecked(true);
            }
        }
        //selecting the jobs count button. if its more than what we display we are adding it to the
        //button with sign + which if it pressed will display the alert box with the saved job count
        for (int job = 0; job < jobCount.getChildCount()-1; job++) {
            Button button = (Button) jobCount.getChildAt(job);
            if(Integer.parseInt(button.getText().toString())==joblessGlobal.getTotalJobs()){
               button.setTextColor(getResources().getColor(R.color.menuColorDefault, null));
               button.setBackground(getResources().getDrawable(R.drawable.rectangle_selected));
                break;
            }
        }
        if (jobCount.getChildCount()<= joblessGlobal.getTotalJobs()){
            Button button = (Button) jobCount.getChildAt(jobCount.getChildCount()-1);
            button.setTextColor(getResources().getColor(R.color.menuColorDefault, null));
            button.setBackground(getResources().getDrawable(R.drawable.rectangle_selected));
            jobCountOverPlus = String.valueOf(joblessGlobal.getTotalJobs());
        }
        enterJobNamesButton.setText(getString(R.string.click_edit_jobs_names));

    }


    //Creating date picker dialor whet we click on the date Text view
    private final View.OnClickListener datePickerPressed = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog= new DatePickerDialog(view.getContext(),
                                                    AlertDialog.THEME_HOLO_LIGHT,datePickerListener,
                                                      year,month, day);
            TextView title = new TextView(view.getContext());
            title.setText(R.string.select_dob);
            title.setPadding(10, 30, 10, 30);
            title.setGravity(Gravity.CENTER);
            title.setTextSize(30);

            datePickerDialog.setCustomTitle(title);
            datePickerDialog.show();

        }
    };
    //Listener for the selected date
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            dobToDisplay = (month+1)+"/"+date+"/"+year;
            datePickerTextView.setText(dobToDisplay);
            //bug!!!!
            if(year == Calendar.getInstance().get(Calendar.YEAR )&& month > Calendar.getInstance().get(Calendar.MONTH) ||
                    year > Calendar.getInstance().get(Calendar.YEAR )){
                joblessGlobal.setUserDate(dobToDisplay);
                //creating the base year on which the jobs incomes will be based
                joblessGlobal.createBaseYear();
            }
            else{
                joblessGlobal.setUserDate(null);
            }


        }
    };
    //Listener for the gender, male and female radio buttons
    RadioGroup.OnCheckedChangeListener genderSelectedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int gender) {
            try{
                joblessGlobal.hideKeyboard((Activity)radioGroup.getContext());
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }

            if(gender == R.id.maleButton){
                userGender = getString(R.string.male);

            }
            else if(gender == R.id.femaleButton){
                userGender = getString(R.string.female);
            }

        }
    };

    // adding listener to all the buttons in the linearLayout
    private void addListenerToJobCountButtons(LinearLayout jobCount) {
        for (int job = 0; job < jobCount.getChildCount(); job++) {
            Button button = (Button) jobCount.getChildAt(job);
            button.setOnClickListener(onJobCountSelected);
        }

    }
   // This onClickLictener whil will set a different state for the buttons
    //When a button is clicked will change his text and background color to blue
    //The rest buttons will also change their text and background colors to white
    //the effect would present a selective mode
    private final View.OnClickListener onJobCountSelected = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            joblessGlobal.hideKeyboard((Activity)view.getContext());
            Button button = (Button) view;
            if (button.getText().toString().endsWith("+")) {
                //Crating a custom alert dialog to get the jo count if the user press 10+
                LayoutInflater inflater = getLayoutInflater();
                View alert = inflater.inflate(R.layout.alert_dialog_enter_job_count, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(alert);
                final EditText jobCountOver = (EditText) alert.findViewById(R.id.jobCountOver10);
                if(jobCountOverPlus != null) {
                    jobCountOver.setText(jobCountOverPlus);
                    jobCountOver.setSelection(jobCountOverPlus.length());
                }
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(jobCountOver.getText().toString().isEmpty()){
                            joblessGlobal.setTotalJobs(0);
                        }
                        else {
                            joblessGlobal.setTotalJobs(Integer.parseInt(jobCountOver.getText().toString()));
                        }

                    }

                });
                alertDialog.show();

            } else {
                joblessGlobal.setTotalJobs(Integer.parseInt(button.getText().toString()));
            }
            //changing the color state of the buttons
            //checking if the the buttons is clicked(selected)
            if (button.getTag() == (null) || button.getTag().equals("Unselected")) {
                button.setTextColor(getResources().getColor(R.color.menuColorDefault, null));
                button.setBackground(getResources().getDrawable(R.drawable.rectangle_selected));
            }
            //looping throuth the buttons that are not clicked and setting a tag Unselected
            //and changing the color state
            for (int job = 0; job < jobCount.getChildCount(); job++) {
                Button buttonToCheck = (Button) jobCount.getChildAt(job);
                if (!button.equals(buttonToCheck)) {
                    buttonToCheck.setTextColor(getResources().getColor(R.color.menuColor));
                    buttonToCheck.setBackground(getResources().getDrawable(R.drawable.rectangle));
                    buttonToCheck.setTag("Unselected");

                }
            }




        }

    };

    //creading a dialog where the user can add the names for his jobs
    private final View.OnClickListener showAlertDialogToEnterJobNames = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            joblessGlobal.hideKeyboard((Activity)view.getContext());
            if (!dialogCreated) {
                dialogJobNames = new DialogJobNames(NewEstimate.this);
                dialogJobNames.show();
                dialogCreated = true;
            } else {
                dialogJobNames.editNames();
                dialogJobNames.show();

            }

        }
    };



    //this method is call in the DialogJobNames class to change the text on the button when
    //the user is done whit entering the names
    protected void setEnterJobNamesButtonText() {
        enterJobNamesButton.setText(getString(R.string.click_edit_jobs_names));

    }

    //Listener for a touch out side of the EditText Views to hide the keyboard
    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            joblessGlobal.hideKeyboard((Activity)view.getContext());
            return false;
        }
    };



}
