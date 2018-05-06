package com.sora.treasurer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.treasurer.database.AppDatabase;
import com.sora.treasurer.enums.TRFragment;
import com.sora.treasurer.enums.ViewScreen;
import com.sora.treasurer.fragments.CreateExpenseFragment;
import com.sora.treasurer.fragments.DashboardFragment;
import com.sora.treasurer.fragments.FragmentStack;
import com.sora.treasurer.fragments.ReportingFragment;
import com.sora.treasurer.http.TRGateway;
import com.sora.treasurer.listeners.OnFragmentInteractionListener;
import com.sora.treasurer.utils.TRPref;
import com.sora.treasurer.utils.Util;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private final int EXPENSE_REQUEST = 101;


    private Toast toast;

    private AppDatabase appDB;
    private Intent intent;

    private Handler mHandler = new Handler();
    private static TRGateway mGateway;

    private DrawerLayout drawerLayout;

    private CreateExpenseFragment createExpenseFragment;
    private DashboardFragment dashboardFragment;
    private ReportingFragment reportingFragment;
    private FragmentStack mFragmentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        appDB = AppDatabase.getDatabase(getApplicationContext());
        mGateway = new TRGateway(this, null);

        setupToolbar();
        setupNavigationView();
        setupFragments(savedInstanceState);
        if (mFragmentStack == null) mFragmentStack = new FragmentStack();
        onFragmentInteraction(TRPref.getCurrentScreen(this), false);
        // showFragment(TRFragment.TR_DASHBOARD_FRAG);

        // createDialogWithoutDateField().show();

    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!mFragmentStack.isEmpty()) {
            ViewScreen screen = mFragmentStack.popFragmentFromStack();
            if (screen != null) onFragmentInteraction(screen, false);
            else this.finish();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        //set version text for TreatU
        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.header_version_textview)).setText(
                String.format("Inventory App ver. %1$s", BuildConfig.VERSION_NAME));
        //set first item selected
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setupFragments(Bundle savedInstanteState) {
        if (savedInstanteState != null) return;
        createExpenseFragment = new CreateExpenseFragment();
        dashboardFragment = new DashboardFragment();
        reportingFragment = new ReportingFragment();
    }

    @SuppressWarnings("ConstantConditions")
    private void showFragment(TRFragment trFragment) {
        Fragment fragment = null;
        if (findViewById(R.id.fragment_container) != null) {
            switch (trFragment) {
                case TR_CREATE_FRAG:
                    fragment = createExpenseFragment;
                    break;
                case TR_REPORT_FRAG:
                    fragment = reportingFragment;
                    break;
                case TR_DASHBOARD_FRAG:
                    fragment = dashboardFragment;
                    break;
            }
        }

        // fragment.setArguments(getIntent().getExtras());
        if (getSupportFragmentManager().getFragments().size() == 0) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main:
                break;
            case R.id.nav_dashboard:
                createDialogWithoutDateField().show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case EXPENSE_REQUEST:
                    String expenseDesc = data.getStringExtra("expenseDesc");
                    // showAlertDialog(true, "Successfully added " + expenseDesc);
                    // populateList();
                    // closeSubMenusFab();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
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
