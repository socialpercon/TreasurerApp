package com.sora.treasurer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sora.treasurer.database.AppDatabase;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.utils.DataService;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    EditText expenseDescTxt, expenseValTxt;
    AlertDialog alertDialog;
    String expenseType;
    TextView expenseVal;
    int expenseTypeForEntity;
    Button addExpenseBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        expenseDescTxt = findViewById(R.id.expenseDescEditTxt);
        expenseValTxt = findViewById(R.id.expenseValEditTxt);
        addExpenseBtn = findViewById(R.id.addExpenseBtn);
        expenseVal = findViewById(R.id.expenseVal);
        addExpenseBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        Intent intent = getIntent();
        expenseType = intent.getStringExtra("expenseType");
        if(expenseType.equals("Expense")) {
            addExpenseBtn.setText("Add Expense");
            addExpenseBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expenseVal.setText("Expense Value: ");
            expenseTypeForEntity = 0;
        } else if (expenseType.equals("Gain")) {
            expenseVal.setText("Gain Value: ");
            addExpenseBtn.setText("Add Gain");
            addExpenseBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            expenseTypeForEntity = 1;
        }
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addExpenseBtn:
                String expenseDesc = expenseDescTxt.getText().toString();
                if (expenseValTxt.getText().toString().equals("")) {
                    showAlertDialog(true, expenseType + " value should not be blank");
                } else {
                    double expenseVal = Double.valueOf(expenseValTxt.getText().toString());
                    ExpenseEntity expenseEntity = new ExpenseEntity(expenseVal, expenseTypeForEntity, DataService.CATEGORY_OTHERS, "", expenseDesc, 0);
                    AppDatabase.getDatabase(this).expenseDao().CreateExpense(expenseEntity);
                    expenseDescTxt.setText("");
                    expenseValTxt.setText("");
                    expenseDescTxt.clearFocus();
                    expenseValTxt.clearFocus();
                    Intent intent = new Intent();
                    intent.putExtra("expenseType",0);
                    intent.putExtra("expenseDesc",expenseDesc);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                break;

        }
    }


    private void showAlertDialog(boolean show, @Nullable String message) {
        if (show) {
            if (alertDialog == null) {
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setCancelable(true);
            }
            alertDialog.setMessage(message);
            alertDialog.show();
        } else {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }
}
