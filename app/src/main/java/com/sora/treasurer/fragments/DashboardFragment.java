package com.sora.treasurer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sora.treasurer.AddExpenseActivity;
import com.sora.treasurer.CreateExpenseActivity;
import com.sora.treasurer.R;
import com.sora.treasurer.TAppEnums.RequestType;
import com.sora.treasurer.database.AppDatabase;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.enums.ViewScreen;
import com.sora.treasurer.http.GatewayListener;
import com.sora.treasurer.http.TRGateway;
import com.sora.treasurer.http.pojo.ExpenseEntityResponse;
import com.sora.treasurer.http.pojo.GatewayResponse;
import com.sora.treasurer.interfaces.RequestCallback;
import com.sora.treasurer.listeners.OnFragmentInteractionListener;
import com.sora.treasurer.server.TRServer;
import com.sora.treasurer.utils.DataService;
import com.sora.treasurer.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DashboardFragment extends Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final int EXPENSE_REQUEST = 101;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private ArrayList<HashMap<String, String>> expenseList;
    private AppDatabase appDB;
    private Handler mHandler = new Handler();
    private LinearLayoutCompat graphLayout;
    // private Button addExpenseItemBtn, addGainItemBtn;
    private AlertDialog alertDialog, removeDialog;
    private SimpleAdapter adapter;
    private ListView listView;
    private TextView expenseTotalTxtView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static TRGateway mGateway;
    private Toast toast;
    private LinearLayout mExpenseFood, mExpenseRent, mExpenseTransport, mGainMore, mExpenseMore;


    private FloatingActionButton fab, fabGain, fabExpense;
    private boolean fabExpand = false;
    private LinearLayout fabGainLayout, fabExpenseLayout, fabMainLayout;

    private final String HASHMAP_E_ID = "EXPENSE_ID";
    private final String HASHMAP_E_VALUE = "EXPENSE_VALUE";
    private final String HASHMAP_E_DESCRIPTION = "EXPENSE_DESCRIPTION";
    private final String HASHMAP_E_TYPE = "EXPENSE_TYPE";
    private final String HASHMAP_E_DATE = "EXPENSE_DATE";
    private final String HASHMAP_E_DAY = "EXPENSE_DAY";
    private final String HASHMAP_E_MONTH = "EXPENSE_MONTH";


    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fabExpand = false;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View setupUI(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // addExpenseItemBtn = fragmentView.findViewById(R.id.addExpenseBtnItem);
        // addExpenseItemBtn.setOnClickListener(this);
        // addGainItemBtn = fragmentView.findViewById(R.id.addGainBtnItem);
        // addGainItemBtn.setOnClickListener(this);
        listView = fragmentView.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        expenseTotalTxtView = fragmentView.findViewById(R.id.expenseTotalValTxt);
        appDB = AppDatabase.getDatabase(getActivity().getApplicationContext());
        swipeRefreshLayout = fragmentView.findViewById(R.id.swipeToRefreshExpenses);
        swipeRefreshLayout.setOnRefreshListener(this);
        mGateway = new TRGateway(getContext(), null);
        graphLayout = fragmentView.findViewById(R.id.main_graph_container);
        fab = (FloatingActionButton) fragmentView.findViewById(R.id.fabMain);
        fabGain = fragmentView.findViewById(R.id.fabGain);
        fabExpense = fragmentView.findViewById(R.id.fabExpense);
        fab.setOnClickListener(this);
        fabGain.setOnClickListener(this);
        fabExpense.setOnClickListener(this);
        fabExpenseLayout = fragmentView.findViewById(R.id.layoutFabExpense);
        fabGainLayout = fragmentView.findViewById(R.id.layoutFabGain);
        fabMainLayout = fragmentView.findViewById(R.id.layoutMainFab);

        mExpenseFood = fragmentView.findViewById(R.id.expenseFood);
        mExpenseFood.setOnClickListener(this);
        return fragmentView;
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
        appDB = AppDatabase.getDatabase(context);

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
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.destroyDrawingCache();
        }
        if (toast != null) {
            toast.cancel();
        }
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        toast = Toast.makeText(getContext(), "Retrieving list of expenses", Toast.LENGTH_SHORT);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                TRServer.ServerSync(context, new RequestCallback() {
                    @Override
                    public void onResponse(Object response) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                populateList();
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("DamnResponse", "onErrorResponse: " + error.getMessage());
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
                // intent = new Intent(this, AddExpenseActivity.class);
                // intent.putExtra("expenseType", "Gain");
                DataService.setExpenseType(DataService.GAIN_TYPE);
                // startActivityForResult(intent, 101);
            case R.id.fabExpense:
                // case R.id.addExpenseBtnItem:
                // intent = new Intent(this, AddExpenseActivity.class);
                // intent.putExtra("expenseType", "Expense");
                DataService.setExpenseType(DataService.EXPENSE_TYPE);
                // startActivityForResult(intent, 101);
                mListener.onFragmentInteraction(ViewScreen.VIEW_CREATE_EXPENSE, false);
                break;

            case R.id.expenseFood:
                Intent intent = new Intent(context, CreateExpenseActivity.class);
                intent.putExtra("expenseType", "Expense");
                startActivityForResult(intent, 101);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    //closes FAB submenus
    private void closeSubMenusFab() {
        fabGainLayout.setVisibility(View.INVISIBLE);
        fabExpenseLayout.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_add_black_24dp
        );
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
        String desc = (String) obj.get(HASHMAP_E_DESCRIPTION);
        String expenseIDs = (String) obj.get(HASHMAP_E_ID);
        String expenseType = (String) obj.get(HASHMAP_E_TYPE);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    /**
     *  METHODS
     */
    private void showAlertDialog(boolean show, @Nullable String message) {
        if (show) {
            if (alertDialog == null) {
                alertDialog = new AlertDialog.Builder(getContext()).create();
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


    @Override
    public void onStart() {
        expenseList = fetchData();
        setupGraph();
        populateList();
        super.onStart();
    }

    private void showAlertRemoveDialog(boolean show, boolean forListView, @Nullable String message, final ExpenseEntity expenseEntity, final int ExpenseType) {
        if (show) {
            if (removeDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if (ExpenseType == 0) builder.setTitle("Remove expense");
                else builder.setTitle("Remove gain");
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // appDB.expenseDao().deleteByID(ExpenseID);
                        expenseEntity.setActive(false);
                        expenseEntity.setUpdated(false);
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

    private void populateList() {
        expenseList = fetchData();

        ArrayList<HashMap<String, String>> tempList = new ArrayList<>();
        int limit = expenseList.size();
        for (int i = 0; i < limit; i++) {
            tempList.add(expenseList.get(i));
        }
        adapter = new SimpleAdapter(
                this.context,
                tempList,
                R.layout.expense_view,
                new String[]{HASHMAP_E_TYPE, HASHMAP_E_VALUE, HASHMAP_E_DESCRIPTION, HASHMAP_E_DAY, HASHMAP_E_MONTH, HASHMAP_E_ID},
                new int[]{R.id.list_item_type, R.id.list_item_value, R.id.list_item_description, R.id.list_item_date_day, R.id.list_item_date_month});

        SimpleAdapter.ViewBinder binder = new SimpleAdapter.ViewBinder() {
            int curType = 0;

            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof TextView) {
                    if (view.getId() == R.id.list_item_type) {
                        TextView itemType = (TextView) view.findViewById(view.getId());
                        ;
                        if (data.toString() == "Gain") {
                            curType = 1;
                            itemType.setBackgroundTintList(getResources().getColorStateList(R.color.colorGain));
                            itemType.setTextColor(getResources().getColor(R.color.colorGain));
                        } else if (data.toString() == "Expense") {
                            curType = 0;
                            itemType.setBackgroundTintList(getResources().getColorStateList(R.color.colorExpense));
                            itemType.setTextColor(getResources().getColor(R.color.colorExpense));
                        }
                    }
                    if (view.getId() == R.id.list_item_value) {
                        TextView lV = (TextView) view.findViewById(R.id.list_item_value);
                        if (curType == 0) {
                            // lV.setTextColor(getResources().getColor(R.color.colorAccent));
                        } else {
                            // lV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
            listView.setEmptyView(getActivity().findViewById(R.id.emptyElement));
            showAlertDialog(true, "No expenses added.");
        }
    }

    private ArrayList<HashMap<String, String>> fetchData() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        double totalVal = 0;
        for (ExpenseEntity expenseEntity : appDB.expenseDao().findAllActive()) {
            if (expenseEntity.getExpenseType() == 0) {
                totalVal -= expenseEntity.getExpenseValue();
            } else {
                totalVal += expenseEntity.getExpenseValue();
            }
            data.add(makeExpenseEntityHashMap(expenseEntity));
        }
        expenseTotalTxtView.setText(String.valueOf(totalVal));
        return data;
    }

    private HashMap<String, String> makeExpenseEntityHashMap(ExpenseEntity expenseEntity) {
        String currencyString = getString(R.string.currencyPHP);
        HashMap<String, String> temp = new HashMap<>();
        temp.put(HASHMAP_E_ID, String.valueOf(expenseEntity.getExpenseID()));
        temp.put(HASHMAP_E_VALUE, String.valueOf(expenseEntity.getExpenseValue()) + "PHP");
        temp.put(HASHMAP_E_DESCRIPTION, expenseEntity.getExpenseDescription());
        temp.put(HASHMAP_E_DATE, Util.formatDateTime(expenseEntity.getDateCreated()));
        temp.put(HASHMAP_E_DAY, Util.formatDateTimeGetDay(expenseEntity.getDateCreated()));
        temp.put(HASHMAP_E_MONTH, Util.formatDateTimeGetMonth(expenseEntity.getDateCreated()));
        if (expenseEntity.getExpenseType() == 0) {
            temp.put(HASHMAP_E_TYPE, "Expense");
        } else {
            temp.put(HASHMAP_E_TYPE, "Gain");
        }

        return temp;
    }

    private void setupGraph() {
        LineChartView chart = getActivity().findViewById(R.id.line_chart);

        List<PointValue> pointValues = new ArrayList<PointValue>();
        List<PointValue> duckTape = new ArrayList<PointValue>();
//        int index = 0;
//        double TotalOfWeek = 0;
//        for( Date date: Util.getDaysOfCurrentWeek()) {
//            double TotalOfDay = 0;
//            Log.i("gwaph", String.valueOf(date.toString()));
//            Log.i("gwaph", String.valueOf(date.getTime()).substring(0,5) + "%");
//            List<ExpenseEntity> expenseEntities = appDB.expenseDao().findExpensesPerDay(String.valueOf(date.getTime()).substring(0,5) + "%");
//            for (ExpenseEntity expenseEntity: expenseEntities) {
//                if(expenseEntity.getExpenseType() == 0) {
//                    TotalOfDay = TotalOfDay - expenseEntity.getExpenseValue();
//                } else {
//                    TotalOfDay = TotalOfDay + expenseEntity.getExpenseValue();
//                }
//
//            }
//            TotalOfWeek = TotalOfWeek + TotalOfDay;
//            pointValues.add(new PointValue(index, (float) TotalOfWeek));
//            axisValues.add(new AxisValue(index, Util.formatDateTimeGetMonthAndDay(date.getTime()).toCharArray()));
//            index++;
//        }
//        Axis axisX = new Axis(axisValues);


        int index = 0;
        double Total = 0;
        Long entryDate = appDB.expenseDao().findFirstEntryDate();
        Long endDate = appDB.expenseDao().findLastEntryDate();
        entryDate = Long.valueOf(String.valueOf(entryDate).substring(0, 5));
        endDate = Long.valueOf(String.valueOf(endDate).substring(0, 5));


        duckTape.add(new PointValue(0, 0));
        for (long start = entryDate; start <= endDate; start++) {
            double TotalOfDay = 0;
            List<ExpenseEntity> expenseEntities = appDB.expenseDao().findExpensesPerDay(String.valueOf(start) + "%");
            for (ExpenseEntity expenseEntity : expenseEntities) {
                if (expenseEntity.getExpenseType() == 0) {
                    TotalOfDay = TotalOfDay - expenseEntity.getExpenseValue();
                } else {
                    TotalOfDay = TotalOfDay + expenseEntity.getExpenseValue();
                }
            }
            Total = Total + TotalOfDay;
            pointValues.add(new PointValue(index, (float) Total));
            // axisValues.add(new AxisValue(index);
            index++;
        }
        Line line = new Line(pointValues).setColor(getActivity().getColor(R.color.colorGain)).setCubic(true).setStrokeWidth(1).setHasPoints(false);
        //.setStrokeWidth(1);

        List<Line> lines = new ArrayList<Line>();

        lines.add(line);
        LineChartData data = new LineChartData();
        // data.setAxisXBottom(axisX);
        data.setLines(lines);

        // Better to not modify viewport of any chart directly so create a copy.
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        // Make temp viewport smaller.
        float dx = tempViewport.width() / 4;
        float dy = tempViewport.height() / 4;
        tempViewport.inset(dx, dy);
        chart.setCurrentViewport(tempViewport);
        // chart.setViewportCalculationEnabled(false);
        chart.setZoomEnabled(false);
        chart.setInteractive(true);
        chart.setScrollEnabled(false);
        chart.setLineChartData(data);
        final Viewport v = new Viewport(chart.getMaximumViewport());
        float fivePercent = v.height() * 0.05f;
        v.top = v.top + fivePercent;
        v.bottom = v.bottom - fivePercent;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setViewportCalculationEnabled(false);

    }
}
