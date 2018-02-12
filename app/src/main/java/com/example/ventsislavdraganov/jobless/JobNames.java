package com.example.ventsislavdraganov.jobless;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ventsislavdraganov on 11/6/17.
 */

public class JobNames extends AppCompatActivity {
    private JoblessGlobal joblessGlobal;
    private ListView jobsNamesListView;
    private ArrayList<String> jobsNames;
    private Button submitButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_names);
        Toolbar toolbar = (Toolbar)findViewById(R.id.namesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        joblessGlobal = JoblessGlobal.getInstance();
        jobsNamesListView = (ListView)findViewById(R.id.listTheFinalNames);
        onCreateList();
        //creating and displaying the listView
        ArrayAdapter<String> list = new CustomJobNamesAdapter(this, jobsNames);
        jobsNamesListView.setAdapter(list);
        jobsNamesListView.setOnItemClickListener(jobNameSelected);
        submitButton = (Button)findViewById(R.id.submitButton);
        doneWithJobsInfo();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.job_names_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //returning back to the NewEstimate activity. New Activity
        if (item.getItemId() == R.id.jobname_menu_exit){
            joblessGlobal.alertDialog(this, getString(R.string.save_information), false, true, true);
            return true;
        }
        else if(item.getItemId()==R.id.jobname_menu_save){
            joblessGlobal.setSavedStatus(true);
            joblessGlobal.saveDraft();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //click listener for the list item
    private final AdapterView.OnItemClickListener jobNameSelected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            joblessGlobal.setAdapterItemView(view);
            joblessGlobal.setAdapterItemJobNamePosition(i);
            Intent intent = new Intent(view.getContext(), JobDetails.class);
            startActivity(intent);
        }
    };
   //creating the list
    private void onCreateList(){
        int totalJobs = joblessGlobal.getTotalJobs();
        jobsNames = joblessGlobal.getJobsNamesInstance();
        TextView noJobsAdded = (TextView)findViewById(R.id.noJobsAddedTextView);
        //if the user did not choose any job count we are displaying a message via textView
            if(totalJobs==0){
                noJobsAdded.setVisibility(View.VISIBLE);
                noJobsAdded.setText(getResources().getString(R.string.no_names_added));


            }
            else{

                //hiding the text view if the user selected a jobs count
                noJobsAdded.setVisibility(View.GONE);
                //if there are no name we will create default names for each job
                    if(jobsNames.isEmpty()){
                        for(int index =0; index<totalJobs; index++){
                            jobsNames.add(getResources().getString(R.string.job)+" "+(index+1));
                        }
                        joblessGlobal.displayMessage(this,getString(R.string.default_jobs_names_added));


                    }
                    //adding extra default names to populate the list
                    else if(jobsNames.size()< totalJobs){
                        for(int index = jobsNames.size(); index<totalJobs; index++){
                            jobsNames.add(getResources().getString(R.string.job)+" "+(index+1));
                        }
                        joblessGlobal.displayMessage(this,getString(R.string.missing_job_names_added));
                    }
                    //removing the last added extra names
                    else if(jobsNames.size()>totalJobs){
                        joblessGlobal.displayMessage(this,getString(R.string.removing_the_extra_names));
                        while(jobsNames.size()>totalJobs){
                            jobsNames.remove(totalJobs);
                            joblessGlobal.updateMap();

                        }
                    }

            }

    }

    private void doneWithJobsInfo() {
        if (joblessGlobal.isAllJobsInfoComplete()){
            submitButton.setVisibility(View.VISIBLE);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    joblessGlobal.estimateUnemploymentCompensation();
                    joblessGlobal.requestCode = joblessGlobal.COMPLETE_ESTIMATE;
                    Intent intent = new Intent(view.getContext(), PreviousEstimate.class);
                    startActivity(intent);
                }
            });
        }

    }


}





