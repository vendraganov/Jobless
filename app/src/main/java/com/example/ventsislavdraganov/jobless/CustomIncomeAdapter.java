package com.example.ventsislavdraganov.jobless;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by ventsislavdraganov on 12/17/17.
 */

public class CustomIncomeAdapter extends ArrayAdapter<IncomeView> {
    //view holder to hold the views of the layout
    private static class ViewHolder {
        TextView period;
        EditText income;
        int positionItem;


    }

    protected CustomIncomeAdapter(Context context, ArrayList<IncomeView> incomes) {
        super(context, 0, incomes);

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //viewHolder reference
        final CustomIncomeAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            //if the content view is not created we create the viewHolder class and
            //we are getting references to the views
            viewHolder = new CustomIncomeAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_income_list_view, parent, false);
            viewHolder.period = (TextView) convertView.findViewById(R.id.incomePeriodTextView);
            viewHolder.income = (EditText)convertView.findViewById(R.id.enterIncomeEditText);
            convertView.setTag(viewHolder);
        } else {
            //if the contentVies exist we are getting the object which was created
            viewHolder = (CustomIncomeAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.positionItem = position;
        //setting the name
        viewHolder.period.setText(getItem(position).period);
        if(getItem(position).income!=null && !getItem(position).income.isEmpty()  ) {
            viewHolder.income.setText(getItem(position).income);
        }
        viewHolder.income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getItem(viewHolder.positionItem).income = editable.toString();
            }
        });

        return convertView;

    }
}
