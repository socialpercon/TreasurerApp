package com.sora.treasurer;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sora.treasurer.enums.TRFragment;
import com.sora.treasurer.enums.ViewScreen;
import com.sora.treasurer.fragments.CreateExpenseCategoryFragment;
import com.sora.treasurer.fragments.FragmentStack;
import com.sora.treasurer.listeners.OnFragmentInteractionListener;
import com.sora.treasurer.utils.Util;

public class CreateExpenseActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private CreateExpenseCategoryFragment createExpenseCategoryFragment;
    private FragmentStack mFragmentStack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        setupFragments(savedInstanceState);
        if (mFragmentStack == null) mFragmentStack = new FragmentStack();
        showFragment(TRFragment.TR_CREATE_EXPENSE_CATEGORY_FRAG);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setupFragments(Bundle savedInstanteState) {
        if (savedInstanteState != null) return;
        createExpenseCategoryFragment = new CreateExpenseCategoryFragment();
    }

    @SuppressWarnings("ConstantConditions")
    private void showFragment(TRFragment trFragment) {
        Fragment fragment = null;
        if (findViewById(R.id.createExpenseFragmentContainer) != null) {
            switch (trFragment) {
                case TR_CREATE_EXPENSE_CATEGORY_FRAG:
                    fragment = createExpenseCategoryFragment;
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
        switch (nextScreen) {
            case VIEW_CREATE_EXPENSE:
            case VIEW_DASHBOARD:
            case VIEW_REPORTS:
                mFragmentStack.addFragmentToStack(nextScreen);
                showFragment(Util.getFramentByScreen(nextScreen));
                break;
        }
    }
}
