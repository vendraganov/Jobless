package com.example.ventsislavdraganov.jobless;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ventsislavdraganov on 11/8/17.
 */

 class CustomJobNamesAdapter extends ArrayAdapter<String> {
     JoblessGlobal joblessGlobal = JoblessGlobal.getInstance();
    //view holder to hold the views of the layout
    private static class ViewHolder{
        ImageView rightArrow;
        ImageView doneSign;
        TextView jobName;

    }


    protected CustomJobNamesAdapter(Context context, ArrayList<String> jobNames){
        super(context,0,jobNames);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //viewHolder reference
        ViewHolder viewHolder;
        if(convertView == null) {
            //if the content view is not created we create the viewHolder class and
            //we are getting references to the views
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_job_names_list_view, parent, false);
            viewHolder.jobName = (TextView)convertView.findViewById(R.id.jobName);
            viewHolder.rightArrow = (ImageView)convertView.findViewById(R.id.right_icon_arrow);
            viewHolder.doneSign= (ImageView)convertView.findViewById(R.id.doneSign);

            //placing the holder as tag
            convertView.setTag(viewHolder);
        }
        else{
            //if the contentVies exist we are getting the object which was created
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //setting the name
        viewHolder.jobName.setText(getItem(position));
        if(joblessGlobal.isJobInfoComplete(position)){
            viewHolder.doneSign.setVisibility(View.VISIBLE);
        }
        else{viewHolder.doneSign.setVisibility(View.GONE);}

        //returning the build view
        return convertView;

    }




}
