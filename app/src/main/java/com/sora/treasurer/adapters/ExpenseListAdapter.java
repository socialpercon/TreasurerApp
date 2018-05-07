package com.sora.treasurer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sora.treasurer.R;
import com.sora.treasurer.adapters.adapter_models.ExpenseArrayAdapterEntity;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.utils.Util;

import java.util.ArrayList;

/**
 * Created by Sora on 07/05/2018.
 */

public class ExpenseListAdapter extends ArrayAdapter<ExpenseArrayAdapterEntity> {
    public ExpenseListAdapter(Context context, ArrayList<ExpenseArrayAdapterEntity> expenseArrayAdapterEntities) {
        super(context, 0, expenseArrayAdapterEntities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ExpenseArrayAdapterEntity expenseArrayAdapterEntity = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_view, parent, false);
        }
        // Lookup view for data population
        TextView list_item_category_text = (TextView) convertView.findViewById(R.id.list_item_category_text);
        TextView list_item_value = (TextView) convertView.findViewById(R.id.list_item_value);
        TextView list_item_description = (TextView) convertView.findViewById(R.id.list_item_description);
        TextView list_item_date_month_and_year = (TextView) convertView.findViewById(R.id.list_item_date_month_and_year);
        TextView list_item_date_day_value = (TextView) convertView.findViewById(R.id.list_item_date_day_value);
        TextView list_item_date_day_text = (TextView) convertView.findViewById(R.id.list_item_date_day_text);
        ImageView list_item_category_image = (ImageView) convertView.findViewById(R.id.list_item_category_image);
        LinearLayout list_expense_section_header = (LinearLayout) convertView.findViewById(R.id.list_expense_section_header);
        list_item_description.setText(expenseArrayAdapterEntity.getExpenseDescription());
        list_item_category_text.setText(expenseArrayAdapterEntity.getExpenseCategory());
        list_item_date_month_and_year.setText(Util.formatDateTimeGetMonthAndYear(expenseArrayAdapterEntity.getDateCreated()));
        list_item_date_day_value.setText(Util.formatDateTimeGetDay(expenseArrayAdapterEntity.getDateCreated()));
        list_item_date_day_text.setText(Util.formatDateTimeGetDayText(expenseArrayAdapterEntity.getDateCreated()));
        list_item_category_image.setImageResource(Util.getCategoryImageResource(getContext(),expenseArrayAdapterEntity.getExpenseCategory()));

        if(expenseArrayAdapterEntity.getExpenseType() == 0) {
            list_item_value.setText("-P" + String.valueOf(expenseArrayAdapterEntity.getExpenseValue()));
            list_item_value.setTextColor(getContext().getResources().getColor(R.color.colorExpense));
        }
        else {
            list_item_value.setText("+P" + String.valueOf(expenseArrayAdapterEntity.getExpenseValue()));
            list_item_value.setTextColor(getContext().getResources().getColor(R.color.colorGain));
        }

        if(expenseArrayAdapterEntity.getShowDay()) list_expense_section_header.setVisibility(View.VISIBLE);
        else list_expense_section_header.setVisibility(View.GONE);

        if(expenseArrayAdapterEntity.getShowMonthYear()) list_item_date_month_and_year.setVisibility(View.VISIBLE);
        else list_item_date_month_and_year.setVisibility(View.GONE);

        return convertView;
    }
}
