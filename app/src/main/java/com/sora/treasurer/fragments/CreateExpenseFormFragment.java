package com.sora.treasurer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.treasurer.R;
import com.sora.treasurer.database.AppDatabase;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.enums.ViewScreen;
import com.sora.treasurer.listeners.OnFragmentInteractionListener;
import com.sora.treasurer.listeners.OnSwipeTouchListener;
import com.sora.treasurer.utils.DataService;
import com.sora.treasurer.utils.Util;

import java.util.Date;


public class CreateExpenseFormFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView form_expense_category, form_expense_type, form_expense_current_time;
    private Button form_button_done, form_button_cancel;
    private EditText form_expense_description, form_expense_value;
    private LinearLayout form_expense_container;

    private OnFragmentInteractionListener mListener;
    private Context context;

    public CreateExpenseFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateExpenseFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateExpenseFormFragment newInstance(String param1, String param2) {
        CreateExpenseFormFragment fragment = new CreateExpenseFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return setupUI(inflater, container);
    }


    private View setupUI(LayoutInflater inflater, final ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_create_expense_form, container, false);
        form_expense_category = fragmentView.findViewById(R.id.form_expense_category);
        form_expense_category.setText(DataService.getExpenseCategory());
        form_expense_description = fragmentView.findViewById(R.id.form_expense_description);
        form_expense_value = fragmentView.findViewById(R.id.form_expense_value);

        form_button_cancel = fragmentView.findViewById(R.id.form_button_cancel);
        form_button_cancel.setOnClickListener(this);
        form_button_done = fragmentView.findViewById(R.id.form_button_done);
        form_button_done.setOnClickListener(this);
        form_expense_type = fragmentView.findViewById(R.id.form_expense_type);
        form_expense_current_time = fragmentView.findViewById(R.id.form_expense_current_time);
        form_expense_current_time.setText(Util.formatDateTimeGetHourMinute(new Date().getTime()));
        switch (DataService.getExpenseType()) {
            case DataService.GAIN_TYPE:
                form_expense_type.setText(getResources().getString(R.string.form_header_income));
                break;
            case DataService.EXPENSE_TYPE:
                form_expense_type.setText(getResources().getString(R.string.form_header_expense));
                break;
        }

        form_expense_container = fragmentView.findViewById(R.id.form_expense_container);
        form_expense_container.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeRight() {
                mListener.onFragmentInteraction(ViewScreen.VIEW_CREATE_EXPENSE_CATEGORY, true);
            }

            @Override
            public void onSwipeLeft() {
            }
        });

        return fragmentView;
    }


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        form_expense_current_time.setText(Util.formatDateTimeGetHourMinute(new Date().getTime()));
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.form_button_done:
                if (form_expense_description.getText().toString().isEmpty()) {
                    Toast.makeText(context, "You haven't stated what this is for.", Toast.LENGTH_SHORT).show();
                } else if (form_expense_value.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter an ammount", Toast.LENGTH_SHORT).show();
                } else {
                    double value = Double.valueOf(form_expense_value.getText().toString() == "" ? "0" : form_expense_value.getText().toString());
                    String description = form_expense_description.getText().toString();
                    String category = form_expense_category.getText().toString();
                    ExpenseEntity expenseEntity = new ExpenseEntity(value, DataService.getExpenseType(), description, category, "", 0);
                    AppDatabase.getDatabase(context).expenseDao().CreateExpense(expenseEntity);
                    DataService.setExpenseEntity(expenseEntity);
                    mListener.onFragmentInteraction(null, true);
                    form_expense_description.setText("");
                    form_expense_value.setText("");
                    form_expense_description.clearFocus();
                    form_expense_value.clearFocus();
                }

                break;
            case R.id.form_button_cancel:
                mListener.onFragmentInteraction(ViewScreen.VIEW_CREATE_EXPENSE_CATEGORY, true);
                break;
        }
    }
}
