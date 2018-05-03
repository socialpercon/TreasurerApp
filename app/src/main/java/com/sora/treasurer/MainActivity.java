package com.sora.treasurer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sora.treasurer.TAppEnums.RequestType;
import com.sora.treasurer.database.AppDatabase;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.http.GatewayListener;
import com.sora.treasurer.http.TRGateway;
import com.sora.treasurer.http.pojo.CreateResponse;
import com.sora.treasurer.http.pojo.ExpenseEntityResponse;
import com.sora.treasurer.http.pojo.ExpenseGetResponse;
import com.sora.treasurer.http.pojo.GatewayResponse;
import com.sora.treasurer.interfaces.RequestCallback;
import com.sora.treasurer.utils.Util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private final int EXPENSE_REQUEST = 101;
    private ArrayList<HashMap<String, String>> expenseList;
    private Button addBtn, addExpenseItemBtn, addGainItemBtn;
    private AlertDialog alertDialog, removeDialog;
    private SimpleAdapter adapter;
    private ListView listView;
    private EditText expenseValTxt, expenseDescTxt;
    private TextView expenseTotalTxtView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toast toast;
    private FloatingActionButton fab, fabGain, fabExpense;
    private boolean fabExpand = false;
    private LinearLayout fabGainLayout, fabExpenseLayout, fabMainLayout;
    private AppDatabase appDB;
    private Intent intent;

    private Handler mHandler = new Handler();
    private static TRGateway mGateway;
    private static int mSyncRetry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fabMain);
        fabGain = findViewById(R.id.fabGain);
        fabExpense = findViewById(R.id.fabExpense);
        fab.setOnClickListener(this);
        fabGain.setOnClickListener(this);
        fabExpense.setOnClickListener(this);
        // addExpenseItemBtn = findViewById(R.id.addExpenseBtnItem);
        // addGainItemBtn = findViewById(R.id.addGainBtnItem);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        expenseTotalTxtView = findViewById(R.id.expenseTotalValTxt);
        appDB = AppDatabase.getDatabase(getApplicationContext());
        swipeRefreshLayout = findViewById(R.id.swipeToRefreshExpenses);
        swipeRefreshLayout.setOnRefreshListener(this);


        fabExpenseLayout = findViewById(R.id.layoutFabExpense);
        fabGainLayout = findViewById(R.id.layoutFabGain);
        fabMainLayout = findViewById(R.id.layoutMainFab);
        expenseList = fetchData(false);

        mGateway = new TRGateway(this, null);

        ServerSync(new RequestCallback() {
            @Override
            public void onResponse(Object response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        populateList();
                    }
                });
            }
        });



        // updateServer();
        // upDet();
        // getServerData();

    }




    private void upDet() {
        List<ExpenseEntity> expenseEntities = appDB.expenseDao().findAllWithoutServerID();
        mGateway.setResponseListener(new GatewayListener() {
            @Override
            public void onResponse(GatewayResponse gatewayResponse) {
                Log.i("WEW", "onResponse: " + (gatewayResponse));
            }

            @Override
            public void onErrorResponse(VolleyError error, RequestType requestType) {

            }
        }).expenseAPICreate(expenseEntities);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private void showAlertRemoveDialog(boolean show, boolean forListView, @Nullable String message, final ExpenseEntity expenseEntity, final int ExpenseType) {
        if (show) {
            if (removeDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (ExpenseType == 0) builder.setTitle("Remove expense");
                else builder.setTitle("Remove gain");
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // appDB.expenseDao().deleteByID(ExpenseID);
                        expenseEntity.setActive(false);
                        appDB.expenseDao().UpdateExpense(expenseEntity);
                        populateList();
                        removeDialog = null;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeDialog = null;
                    }
                });
                removeDialog = builder.create();
                builder = null;
                removeDialog.setCanceledOnTouchOutside(false);
                removeDialog.setCancelable(true);
            }
            removeDialog.setMessage(message);
            removeDialog.show();
        } else {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fabMain:
                if (fabExpand == true) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
                break;

            case R.id.fabGain:
                // case R.id.addGainBtnItem:
                intent = new Intent(this, AddExpenseActivity.class);
                intent.putExtra("expenseType", "Gain");
                startActivityForResult(intent, 101);
                break;
            case R.id.fabExpense:
                // case R.id.addExpenseBtnItem:
                intent = new Intent(this, AddExpenseActivity.class);
                intent.putExtra("expenseType", "Expense");
                startActivityForResult(intent, 101);
                break;
        }
    }

    private void ServerSync(final RequestCallback requestCallback) {
        List<ExpenseEntity> expenseEntitiesWithoutServerID = appDB.expenseDao().findAllWithoutServerID();
        ServerCreateExpenseBulk(expenseEntitiesWithoutServerID, new RequestCallback() {
            @Override
            public void onResponse(Object response) {
                ExpenseEntity[] expenseEntities = ((ExpenseEntity[]) response);

                for(ExpenseEntity expenseEntity: expenseEntities) {
                    ExpenseEntity expenseEntity1 = appDB.expenseDao().findEntity(expenseEntity.getDateCreated(), expenseEntity.getExpenseValue(), expenseEntity.getExpenseType());
                    if(expenseEntity1 == null) {
                        appDB.expenseDao().CreateExpense(expenseEntity);
                    } else {
                        appDB.expenseDao().UpdateExpense(expenseEntity);
                    }
                }
                requestCallback.onResponse(response);
            }
        });

    }

    private void ServerCreateExpenseBulk(List<ExpenseEntity> expenseEntities, final RequestCallback requestCallback) {
        mGateway.setResponseListener(new GatewayListener() {
            @Override
            public void onResponse(GatewayResponse gatewayResponse) {
                Log.i("WEWSUS", "onResponse: " + ((ExpenseEntityResponse) gatewayResponse).getData());
                requestCallback.onResponse(((ExpenseEntityResponse) gatewayResponse).getData());
            }

            @Override
            public void onErrorResponse(VolleyError error, RequestType requestType) {
                Log.i("WEWZZ", "onResponse: " + error);
                mSyncRetry++;
            }
        }).expenseAPICreate(expenseEntities);
    }


    private void getServerData() {
        List<ExpenseEntity> expenseEntities = appDB.expenseDao().findAll();
        if (expenseEntities != null) {
            mSyncRetry = 0;
            if (mSyncRetry < 3) {
                mGateway.setResponseListener(new GatewayListener() {
                    @Override
                    public void onResponse(GatewayResponse gatewayResponse) {
                        ExpenseEntity[] expenseEntities = ((ExpenseGetResponse) gatewayResponse).getData();
                        for (ExpenseEntity expenseEntity : expenseEntities) {
                            if (appDB.expenseDao().findEntity(expenseEntity.getDateCreated(), expenseEntity.getExpenseValue(), expenseEntity.getExpenseType()) == null) {
                                ExpenseEntity expenseEntity1 = new ExpenseEntity(expenseEntity);
                                Log.i("WEW", "onResponse: " + expenseEntity1.getExpenseDescription() + " " + String.valueOf(expenseEntity1.getActive()));
                                appDB.expenseDao().CreateExpense(expenseEntity1);
                            }
                        }
                        populateList();
                    }

                    @Override
                    public void onErrorResponse(VolleyError error, RequestType requestType) {
                        Log.i("WEW", "onResponse: " + error);
                        mSyncRetry++;
                    }
                }).expenseAPIGetExceptEntity(expenseEntities);
            }
        }
    }

    private void getServerDataA() {
        mSyncRetry = 0;
        if (mSyncRetry < 3) {
            mGateway.setResponseListener(new GatewayListener() {
                @Override
                public void onResponse(GatewayResponse gatewayResponse) {
                    Log.i("WEW", "onResponse: " + ((ExpenseGetResponse) gatewayResponse).getData());

                }

                @Override
                public void onErrorResponse(VolleyError error, RequestType requestType) {
                    Log.i("WEW", "onResponse: " + error);
                    mSyncRetry++;
                }
            }).expenseAPIGetAll();
        }
    }

    private void updateServer() {
        mSyncRetry = 0;
        if (mSyncRetry < 3) {
            List<ExpenseEntity> expenseEntities = appDB.expenseDao().findAll();
            if (expenseEntities != null) {
                for (ExpenseEntity expenseEntity : appDB.expenseDao().findAll()) {
                    mGateway.setResponseListener(new GatewayListener() {
                        @Override
                        public void onResponse(GatewayResponse gatewayResponse) {
                            Log.i("WEW", "onResponse: " + ((CreateResponse) gatewayResponse).getData());

                        }

                        @Override
                        public void onErrorResponse(VolleyError error, RequestType requestType) {
                            Log.i("WEW", "onResponse: " + error);
                            mSyncRetry++;
                        }
                    }).expenseAPICreate(expenseEntity);
                }
            }
        }
    }

    //closes FAB submenus
    private void closeSubMenusFab() {
        fabGainLayout.setVisibility(View.INVISIBLE);
        fabExpenseLayout.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_attach_money_black_24dp);
        fabExpand = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab() {
        fabGainLayout.setVisibility(View.VISIBLE);
        fabExpenseLayout.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fab.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpand = true;
    }

    private void populateList() {
        expenseList = fetchData(false);

        ArrayList<HashMap<String, String>> tempList = new ArrayList<>();
        int limit = expenseList.size();
        for (int i = 0; i < limit; i++) {
            tempList.add(expenseList.get(i));
        }
        adapter = new SimpleAdapter(
                getApplicationContext(),
                tempList,
                R.layout.expense_view,
                new String[]{"expenseType", "expenseValues", "expenseDescription", "expenseDate", "expenseIDs"},
                new int[]{R.id.list_item_type, R.id.list_item_value, R.id.list_item_description, R.id.list_item_date});

        SimpleAdapter.ViewBinder binder = new SimpleAdapter.ViewBinder() {
            int curType = 0;

            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if (view instanceof TextView) {
                    if (view.getId() == R.id.list_item_type) {

                        if (data.toString() == "Gain") {
                            curType = 1;
                        } else if (data.toString() == "Expense") {
                            curType = 0;
                        }
                    }
                    if (view.getId() == R.id.list_item_value) {
                        TextView lV = (TextView) view.findViewById(R.id.list_item_value);
                        if (curType == 0) {
                            lV.setTextColor(getResources().getColor(R.color.colorAccent));
                        } else {
                            lV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                    }
                }
                return false;
            }
        };
        adapter.setViewBinder(binder);
        listView.setAdapter(adapter);
        if (expenseList.size() > 0) {
        } else {
            listView.setEmptyView(findViewById(R.id.emptyElement));
            showAlertDialog(true, "No expenses added.");
        }
    }

    private ArrayList<HashMap<String, String>> fetchData(boolean fetchPrevious) {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        double totalVal = 0;
        if (fetchPrevious) {
            List<Long> ExpenseIDs = appDB.expenseDao().findAllExpenseIDs();

            if (ExpenseIDs.size() > 0) {
                for (ExpenseEntity expenseEntity : appDB.expenseDao().findAllByIds(ExpenseIDs)) {
                    if (expenseEntity.getExpenseType() == 0) {
                        totalVal -= expenseEntity.getExpenseValue();
                    } else {
                        totalVal += expenseEntity.getExpenseValue();
                    }

                    data.add(makeExpenseEntityHashMap(expenseEntity));
                }
            }
        } else {
            for (ExpenseEntity expenseEntity : appDB.expenseDao().findAll()) {
                if (expenseEntity.getExpenseType() == 0) {
                    totalVal -= expenseEntity.getExpenseValue();
                } else {
                    totalVal += expenseEntity.getExpenseValue();
                }

                data.add(makeExpenseEntityHashMap(expenseEntity));
            }
        }
        expenseTotalTxtView.setText(String.valueOf(totalVal));
        return data;
    }

    private HashMap<String, String> makeExpenseEntityHashMap(ExpenseEntity expenseEntity) {
        HashMap<String, String> temp = new HashMap<>();
        temp.put("expenseIDs", String.valueOf(expenseEntity.getExpenseID()));
        temp.put("expenseValues", String.valueOf(expenseEntity.getExpenseValue()));
        temp.put("expenseDescription", expenseEntity.getExpenseDescription());
        temp.put("expenseDate", Util.formatDateTime(expenseEntity.getDateCreated()));
        if (expenseEntity.getExpenseType() == 0) {
            temp.put("expenseType", "Expense");
        } else {
            temp.put("expenseType", "Gain");
        }

        return temp;
    }

    @Override
    public void onRefresh() {
        toast = Toast.makeText(this, "Retrieving list of expenses", Toast.LENGTH_SHORT);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                ServerSync(new RequestCallback() {
                    @Override
                    public void onResponse(Object response) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                populateList();
                            }
                        });
                    }
                });
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        swipeRefreshLayout.destroyDrawingCache();
        if (toast != null) {
            toast.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
        String desc = (String) obj.get("expenseDescription");
        String expenseIDs = (String) obj.get("expenseIDs");
        String expenseType = (String) obj.get("expenseType");
        ExpenseEntity expenseEntity = appDB.expenseDao().findByID(Long.valueOf(expenseIDs)).get(0);
        int eType = 0;
        if (expenseType.equals("Gain")) {
            eType = 1;
        }
        if (desc.equals("")) {
            desc = "this";
        }

        showAlertRemoveDialog(true, true, "Delete " + expenseEntity.getExpenseDescription() + " from the list?", expenseEntity, eType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case EXPENSE_REQUEST:
                    String expenseDesc = data.getStringExtra("expenseDesc");
                    showAlertDialog(true, "Successfully added " + expenseDesc);
                    populateList();
                    closeSubMenusFab();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
