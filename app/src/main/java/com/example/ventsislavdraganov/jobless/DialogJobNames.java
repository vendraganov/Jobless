package com.example.ventsislavdraganov.jobless;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ventsislavdraganov on 11/4/17.
 */

 public class DialogJobNames extends Dialog {
    private JoblessGlobal joblessGlobal;
    private int count;
    private ArrayList<String> jobNames;
    private Button saveName;
    private EditText jobNameEditText;
    private Button doneWithNames;
    private TextView enterJobNumberTextView;
    private ArrayAdapter<String> list;
    private boolean editNameClicked;
    private int namePosition;
    private int totalJobs;
    private NewEstimate newEstimate;



    DialogJobNames(@NonNull Context context) {
        super(context);
        count = 1;
        editNameClicked = false;
        newEstimate = (NewEstimate)context;
        joblessGlobal = JoblessGlobal.getInstance();
        totalJobs = joblessGlobal.getTotalJobs();
        jobNames = joblessGlobal.getJobsNames();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setContentView(R.layout.dialog_job_names);
        saveName = (Button)findViewById(R.id.saveJobNameButton);
        saveName.setOnClickListener(saveListener);
        jobNameEditText = (EditText)findViewById(R.id.enterJobNameEditText);
        jobNameEditText.addTextChangedListener(doneButtonDisable);
        doneWithNames = (Button)findViewById(R.id.doneWithNamesButton);
        doneWithNames.setOnClickListener(doneListener);
        enterJobNumberTextView = (TextView)findViewById(R.id.enterJobNameTextView);
        if(!jobNames.isEmpty()){
            enterJobNumberTextView.setText(getContext().getString(R.string.edit_name));
            jobNameEditText.setEnabled(false);
            saveName.setEnabled(false);
            doneWithNames.setEnabled(true);

        }
        enterJobNumberTextView.setText(getContext().getString(R.string.enter_job_name, count));
        ListView listView = (ListView)findViewById(R.id.jobNamesListView);
        list = new ArrayAdapter<String>(getContext(), R.layout.basic_list_view, jobNames);
        listView.setAdapter(list);
        listView.setOnItemClickListener(itemClickListener);
    }

    // when the user change the number of jobs
    void editNames() {
        totalJobs = joblessGlobal.getTotalJobs();
        //if he reduce the number of jobs we are storing the first ones entered
        //with option to be edit
        jobNames.clear();
        jobNames.addAll(joblessGlobal.getJobsNames());
        list.notifyDataSetChanged();
        if(totalJobs< jobNames.size()){
            while(totalJobs<jobNames.size()){
                jobNames.remove(totalJobs);

            }
            list.notifyDataSetChanged();
            enterJobNumberTextView.setText(getContext().getString(R.string.edit_name));
            jobNameEditText.setEnabled(false);
            saveName.setEnabled(false);

        }
        //if the number is equal we are loading the list with option the names to be edit
        else if(totalJobs==jobNames.size()){
            enterJobNumberTextView.setText(getContext().getString(R.string.edit_name));
            jobNameEditText.setEnabled(false);
            saveName.setEnabled(false);
        }
        //if the number of jobs is bigger then the previous we are prompting the user to enter the new names
        else if(totalJobs > jobNames.size()) {
            count = jobNames.size() + 1;
            enterJobNumberTextView.setText(getContext().getString(R.string.enter_job_name, count));
            jobNameEditText.setEnabled(true);
            saveName.setEnabled(true);
        }
    }

    private final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //the editTextField is enable and the save button as well
            jobNameEditText.setEnabled(true);
            saveName.setEnabled(true);
            enterJobNumberTextView.setText(getContext().getString(R.string.editing_name));
            //setting the previous name which the user wants to edit
            jobNameEditText.setText(jobNames.get(i));
            //move the cursor et the end of the text
            jobNameEditText.setSelection(jobNames.get(i).length());
            //when the user click on the save button we are sending a message to the listener
            //to perform the necessary action
            editNameClicked =true;
            namePosition = i;
        }
    };

    //listener for the the done button
    private final View.OnClickListener doneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //displaying alert messages if the user did not add all the jobs names
            if(jobNames.isEmpty()){
                joblessGlobal.alertDialog(view.getContext(),view.getContext().getString(R.string.alert_message_no_jobs),false, false, false);

            }
            else if(jobNames.size()<totalJobs){
                newEstimate.setEnterJobNamesButtonText();
                joblessGlobal.setJobsNames(jobNames);
                joblessGlobal.alertDialog(view.getContext(), view.getContext().getString(R.string.alert_message_missing_jobs),false, false, false);
            }
            else {
                newEstimate.setEnterJobNamesButtonText();
                joblessGlobal.setJobsNames(jobNames);
            }
            dismiss();

        }
    };


   //saving the name
    private final View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //if the user is adding a new name
              if(!editNameClicked) {
                  if (count < totalJobs) {
                      enterJobNumberTextView.setText(getContext().getString(R.string.enter_job_name, (count + 1)));
                      jobNames.add(jobNameEditText.getText().toString());
                      jobNameEditText.setText("");
                      list.notifyDataSetChanged();
                   //if the user populate na list we are displaying a message
                   //and we disable the editText view and the save button
                  } else if (count == totalJobs || count > totalJobs) {
                      jobNames.add(jobNameEditText.getText().toString());
                      list.notifyDataSetChanged();
                      enterJobNumberTextView.setText(getContext().getString(R.string.no_more_jobs));
                      jobNameEditText.setText("");
                      jobNameEditText.setEnabled(false);
                      saveName.setEnabled(false);

                  }

              }
              //user edit a name
              else{
                  count--;
                  editNameClicked =false;
                  jobNames.set(namePosition,jobNameEditText.getText().toString());
                  jobNameEditText.setText("");
                  list.notifyDataSetChanged();
                  if (count < totalJobs) {
                      enterJobNumberTextView.setText(getContext().getString(R.string.enter_job_name, (count +1)));
                  } else if (count == totalJobs || count > totalJobs) {
                      enterJobNumberTextView.setText(getContext().getString(R.string.no_more_jobs));
                      jobNameEditText.setEnabled(false);
                      saveName.setEnabled(false);

                  }
              }
            //incrementing the jobs count
            count++;
            doneWithNames.setEnabled(true);


        }



    };

    //text watcher if the user is adding a name we disable the done button
    //the only option for the user is to save the name
    private final TextWatcher doneButtonDisable = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            doneWithNames.setEnabled(false);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}
