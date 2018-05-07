package com.sora.treasurer.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.treasurer.R;
import com.sora.treasurer.enums.TRFragment;
import com.sora.treasurer.enums.ViewScreen;
import com.sora.treasurer.listeners.OnFragmentInteractionListener;
import com.sora.treasurer.listeners.OnSwipeTouchListener;
import com.sora.treasurer.utils.DataService;
import com.sora.treasurer.utils.Util;

public class CreateExpenseCategoryFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout expenseOptionsBtn, gainOptionsBtn, includeExpenseOptions, includeGainOptions;
    private LinearLayout
            expenseCategoryFood, expenseCategoryTransport,
            expenseCategoryRent, expenseCategoryLaundry,
            expenseCategoryMedical, expenseCategoryFitness,
            expenseCategoryGrocery, expenseCategoryEntertainment,
            expenseCategoryElectronics, expenseCategoryOthers,
            gainCategoryATM, gainCategorySalary, gainCategoryAllowance,
            gainCategoryGift, gainCategoryStocks;

    private LinearLayoutCompat category_choose_frame;
    private TextView expenseOptionsTxt, gainOptionsTxt;
    private Button form_category_cancel;

    private Context context;


    public CreateExpenseCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateExpenseCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateExpenseCategoryFragment newInstance(String param1, String param2) {
        CreateExpenseCategoryFragment fragment = new CreateExpenseCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DataService.setExpenseType(DataService.EXPENSE_TYPE);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private View setupUI(LayoutInflater inflater, final ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_create_expense_category, container, false);
        expenseOptionsBtn = fragmentView.findViewById(R.id.category_choose_expense);
        expenseOptionsTxt = fragmentView.findViewById(R.id.category_choose_expense_text);
        gainOptionsBtn = fragmentView.findViewById(R.id.category_choose_gain);
        gainOptionsTxt = fragmentView.findViewById(R.id.category_choose_gain_text);
        expenseOptionsBtn.setOnClickListener(this);
        gainOptionsBtn.setOnClickListener(this);
        includeExpenseOptions = fragmentView.findViewById(R.id.include_expense_list);
        includeGainOptions = fragmentView.findViewById(R.id.include_gain_list);
        category_choose_frame = fragmentView.findViewById(R.id.category_choose_frame);
        category_choose_frame.setOnTouchListener(new OnSwipeTouchListener(this.context) {
            @Override
            public void onSwipeRight() {
                toggleExpenseCategoryType(TYPE_EXPENSE);
            }

            @Override
            public void onSwipeLeft() {
                toggleExpenseCategoryType(TYPE_GAIN);
            }
        });

        form_category_cancel = fragmentView.findViewById(R.id.form_category_cancel);
        form_category_cancel.setOnClickListener(this);

        /**
         *  CATEGORIES
         */

        expenseCategoryElectronics = fragmentView.findViewById(R.id.expenseCategoryElectronics);
        expenseCategoryEntertainment = fragmentView.findViewById(R.id.expenseCategoryEntertainment);
        expenseCategoryFitness = fragmentView.findViewById(R.id.expenseCategoryFitness);
        expenseCategoryFood = fragmentView.findViewById(R.id.expenseCategoryFood);
        expenseCategoryGrocery = fragmentView.findViewById(R.id.expenseCategoryGrocery);
        expenseCategoryLaundry = fragmentView.findViewById(R.id.expenseCategoryLaundry);
        expenseCategoryMedical = fragmentView.findViewById(R.id.expenseCategoryMedical);
        expenseCategoryOthers = fragmentView.findViewById(R.id.expenseCategoryOthers);
        expenseCategoryRent = fragmentView.findViewById(R.id.expenseCategoryRent);
        expenseCategoryTransport = fragmentView.findViewById(R.id.expenseCategoryTranspo);
        gainCategoryAllowance = fragmentView.findViewById(R.id.gainCategoryAllowance);
        gainCategoryATM = fragmentView.findViewById(R.id.gainCategoryATM);
        gainCategoryGift = fragmentView.findViewById(R.id.gainCategoryGift);
        gainCategorySalary = fragmentView.findViewById(R.id.gainCategorySalary);
        gainCategoryStocks = fragmentView.findViewById(R.id.gainCategoryStocks);

        expenseCategoryElectronics.setOnClickListener(new chooseOption());
        expenseCategoryEntertainment.setOnClickListener(new chooseOption());
        expenseCategoryFitness.setOnClickListener(new chooseOption());
        expenseCategoryFood.setOnClickListener(new chooseOption());
        expenseCategoryGrocery.setOnClickListener(new chooseOption());
        expenseCategoryLaundry.setOnClickListener(new chooseOption());
        expenseCategoryMedical.setOnClickListener(new chooseOption());
        expenseCategoryOthers.setOnClickListener(new chooseOption());
        expenseCategoryRent.setOnClickListener(new chooseOption());
        expenseCategoryTransport.setOnClickListener(new chooseOption());
        gainCategoryAllowance.setOnClickListener(new chooseOption());
        gainCategoryATM.setOnClickListener(new chooseOption());
        gainCategoryGift.setOnClickListener(new chooseOption());
        gainCategorySalary.setOnClickListener(new chooseOption());
        gainCategoryStocks.setOnClickListener(new chooseOption());

        return fragmentView;
    }

    private class chooseOption implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.expenseCategoryElectronics:
                    DataService.setExpenseCategory(DataService.CATEGORY_ELECTRONICS);
                    break;
                case R.id.expenseCategoryEntertainment:
                    DataService.setExpenseCategory(DataService.CATEGORY_ENTERTAINMENT);
                    break;
                case R.id.expenseCategoryFitness:
                    DataService.setExpenseCategory(DataService.CATEGORY_FITNESS);
                    break;
                case R.id.expenseCategoryFood:
                    DataService.setExpenseCategory(DataService.CATEGORY_FOOD);
                    break;
                case R.id.expenseCategoryGrocery:
                    DataService.setExpenseCategory(DataService.CATEGORY_GROCERY);
                    break;
                case R.id.expenseCategoryLaundry:
                    DataService.setExpenseCategory(DataService.CATEGORY_LAUNDRY);
                    break;
                case R.id.expenseCategoryMedical:
                    DataService.setExpenseCategory(DataService.CATEGORY_MEDICAL);
                    break;
                case R.id.expenseCategoryOthers:
                    DataService.setExpenseCategory(DataService.CATEGORY_OTHERS);
                    break;
                case R.id.expenseCategoryTranspo:
                    DataService.setExpenseCategory(DataService.CATEGORY_TRANSPORT);
                    break;
                case R.id.expenseCategoryRent:
                    DataService.setExpenseCategory(DataService.CATEGORY_RENT);
                    break;
                case R.id.gainCategoryATM:
                    DataService.setExpenseCategory(DataService.CATEGORY_ATM);
                    break;
                case R.id.gainCategoryAllowance:
                    DataService.setExpenseCategory(DataService.CATEGORY_ALLOWANCE);
                    break;
                case R.id.gainCategoryGift:
                    DataService.setExpenseCategory(DataService.CATEGORY_GIFT);
                    break;
                case R.id.gainCategorySalary:
                    DataService.setExpenseCategory(DataService.CATEGORY_SALARY);
                    break;
                case R.id.gainCategoryStocks:
                    DataService.setExpenseCategory(DataService.CATEGORY_STOCKS);
                    break;
            }
            mListener.onFragmentInteraction(ViewScreen.VIEW_CREATE_EXPENSE_FORM, false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_choose_expense:
                toggleExpenseCategoryType(TYPE_EXPENSE);
                break;
            case R.id.category_choose_gain:
                toggleExpenseCategoryType(TYPE_GAIN);
                break;
            case R.id.form_category_cancel:
                mListener.onFragmentInteraction(null, true);
                break;
        }
    }

    private final String TYPE_EXPENSE = "expense";
    private final String TYPE_GAIN = "gain";

    private void toggleExpenseCategoryType(String expenseType) {
        switch (expenseType) {
            case TYPE_EXPENSE:
                expenseOptionsBtn.setBackground(getResources().getDrawable(R.drawable.expense_rounded_corner_active));
                gainOptionsBtn.setBackground(getResources().getDrawable(R.drawable.expense_gain_rounded_corner));
                expenseOptionsTxt.setTextColor(getResources().getColor(R.color.treasurerAppTheme));
                gainOptionsTxt.setTextColor(getResources().getColor(R.color.white_tint));
                includeGainOptions.setVisibility(View.INVISIBLE);
                includeExpenseOptions.setVisibility(View.VISIBLE);
                DataService.setExpenseType(DataService.EXPENSE_TYPE);
                break;
            case TYPE_GAIN:
                expenseOptionsBtn.setBackground(getResources().getDrawable(R.drawable.expense_rounded_corner));
                gainOptionsBtn.setBackground(getResources().getDrawable(R.drawable.expense_gain_rounded_corner_active));
                gainOptionsTxt.setTextColor(getResources().getColor(R.color.treasurerAppTheme));
                expenseOptionsTxt.setTextColor(getResources().getColor(R.color.white_tint));
                includeGainOptions.setVisibility(View.VISIBLE);
                includeExpenseOptions.setVisibility(View.INVISIBLE);
                DataService.setExpenseType(DataService.GAIN_TYPE);
                break;
        }
    }
}
