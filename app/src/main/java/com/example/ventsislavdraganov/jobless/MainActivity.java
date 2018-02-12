package com.example.ventsislavdraganov.jobless;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button newButton;
    private Button oldButton;
    private Button draftButton;
    private JoblessGlobal joblessGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newButton = (Button) findViewById(R.id.newButton);
        oldButton = (Button) findViewById(R.id.previousButton);
        draftButton = (Button) findViewById(R.id.draftButton);
        newButton.setOnClickListener(onClickListener);
        oldButton.setOnClickListener(onClickListener);
        draftButton.setOnClickListener(onClickListener);
        joblessGlobal = JoblessGlobal.getInstance();
        showDraftButton();

    }



    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.newButton) {
                Intent intent = new Intent(view.getContext(), NewEstimate.class);
                joblessGlobal.requestCode = joblessGlobal.NEW_ESTIMATE;
                startActivity(intent);

            } else if (view.getId() == R.id.previousButton) {
                joblessGlobal.requestCode = joblessGlobal.EDIT_ESTIMATE;
                Intent intent = new Intent(view.getContext(), PreviousEstimate.class);
                startActivity(intent);
            } else if (view.getId() == R.id.draftButton) {
                Intent intent = new Intent(view.getContext(), NewEstimate.class);
                joblessGlobal.requestCode = joblessGlobal.DRAFT_ESTIMATE;
                startActivity(intent);

            }
        }
    };


    private void showDraftButton() {
        if (joblessGlobal.getSavedStatus()) {
            draftButton.setVisibility(View.VISIBLE);
        } else {
            draftButton.setVisibility(View.INVISIBLE);
        }
    }


}



