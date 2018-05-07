package com.sora.treasurer;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sora.treasurer.enums.TRFragment;
import com.sora.treasurer.enums.ViewScreen;
import com.sora.treasurer.fragments.CreateExpenseCategoryFragment;
import com.sora.treasurer.fragments.CreateExpenseFormFragment;
import com.sora.treasurer.fragments.FragmentStack;
import com.sora.treasurer.listeners.OnFragmentInteractionListener;
import com.sora.treasurer.utils.DataService;
import com.sora.treasurer.utils.Util;

public class CreateExpenseActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private CreateExpenseCategoryFragment createExpenseCategoryFragment;
    private CreateExpenseFormFragment createExpenseFormFragment;
    private FragmentStack mFragmentStack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        setupFragments(savedInstanceState);
        if (mFragmentStack == null) mFragmentStack = new FragmentStack();
        onFragmentInteraction(ViewScreen.VIEW_CREATE_EXPENSE_CATEGORY, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setupFragments(Bundle savedInstanteState) {
        if (savedInstanteState != null) return;
        createExpenseCategoryFragment = new CreateExpenseCategoryFragment();
        createExpenseFormFragment = new CreateExpenseFormFragment();

    }

    @SuppressWarnings("ConstantConditions")
    private void showFragment(TRFragment trFragment) {
        Fragment fragment = null;
        if (findViewById(R.id.createExpenseFragmentContainer) != null) {
            switch (trFragment) {
                case TR_CREATE_EXPENSE_CATEGORY_FRAG:
                    fragment = createExpenseCategoryFragment;
                    break;
                case TR_CREATE_EXPENSE_CATEGORY_FORM_FRAG:
                    fragment = createExpenseFormFragment;
                    break;
            }
        }

        fragment.setArguments(getIntent().getExtras());
        if (getSupportFragmentManager().getFragments().size() == 0) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.createExpenseFragmentContainer, fragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.createExpenseFragmentContainer, fragment)
                    .commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onFragmentInteraction(ViewScreen nextScreen, boolean removeFragmentStack) {
        if (removeFragmentStack) mFragmentStack.removeFromStack();
        mFragmentStack.setCurrentScreen(nextScreen);
        if(nextScreen != null) {
            switch (nextScreen) {
                case VIEW_CREATE_EXPENSE_CATEGORY:
                case VIEW_CREATE_EXPENSE_FORM:
                    mFragmentStack.addFragmentToStack(nextScreen);
                    showFragment(Util.getFramentByScreen(nextScreen));
                    break;
            }
        } else {
            mFragmentStack.clearFragmentStack();
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    public void onBackPressed() {
        if (!mFragmentStack.isEmpty()) {
            ViewScreen screen = mFragmentStack.popFragmentFromStack();
            if (screen != null) onFragmentInteraction(screen, false);
            else this.finish();

        } else {
            super.onBackPressed();
        }
    }
}
