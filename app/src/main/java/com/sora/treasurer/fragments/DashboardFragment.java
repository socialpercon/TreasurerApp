package com.sora.treasurer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sora.treasurer.AddExpenseActivity;
import com.sora.treasurer.animators.Animator;
import com.sora.treasurer.CreateExpenseActivity;
import com.sora.treasurer.R;
import com.sora.treasurer.TAppEnums.RequestType;
import com.sora.treasurer.adapters.ExpenseListAdapter;
import com.sora.treasurer.adapters.adapter_models.ExpenseArrayAdapterEntity;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DashboardFragment extends Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
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
    private ArrayList<ExpenseArrayAdapterEntity> expenseArrayAdapterEntitiesList;
    private ArrayList<ExpenseEntity> expenseEntities;
    private AppDatabase appDB;
    private Handler mHandler = new Handler();
    private LinearLayoutCompat graphLayout;
    // private Button addExpenseItemBtn, addGainItemBtn;
    private AlertDialog alertDialog, removeDialog;
    private SimpleAdapter adapter;
    private ArrayAdapter expenseListAdapter;
    private ListView listView;
    private TextView expenseTotalTxtView, TotalExpenseView, TotalGainView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static TRGateway mGateway;
    private Toast toast;

    private LineChart lineChart;
    private LineChartView chart;

    private FloatingActionButton fab;
    private LinearLayout fabGainLayout, fabExpenseLayout, fabMainLayout;


    private RelativeLayout TRAppReportSummary;
    private View TRAppReportSummaryFull;

    private Animator anim = new Animator();
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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        expenseTotalTxtView = fragmentView.findViewById(R.id.expenseTotalValTxt);
        TotalExpenseView = fragmentView.findViewById(R.id.TotalExpense);
        TotalGainView = fragmentView.findViewById(R.id.TotalGain);
        appDB = AppDatabase.getDatabase(getActivity().getApplicationContext());
        swipeRefreshLayout = fragmentView.findViewById(R.id.swipeToRefreshExpenses);
        swipeRefreshLayout.setOnRefreshListener(this);
        mGateway = new TRGateway(getContext(), null);
        graphLayout = fragmentView.findViewById(R.id.main_graph_container);
        fab = (FloatingActionButton) fragmentView.findViewById(R.id.fabMain);
//        fabGain = fragmentView.findViewById(R.id.fabGain);
//        fabExpense = fragmentView.findViewById(R.id.fabExpense);
        fab.setOnClickListener(this);
//        fabGain.setOnClickListener(this);
//        fabExpense.setOnClickListener(this);
        fabExpenseLayout = fragmentView.findViewById(R.id.layoutFabExpense);
        fabGainLayout = fragmentView.findViewById(R.id.layoutFabGain);
        fabMainLayout = fragmentView.findViewById(R.id.layoutMainFab);

        chart = fragmentView.findViewById(R.id.line_chart);

//        TRAppReportSummaryFull = fragmentView.findViewById(R.id.TRAppReportSummaryFull);
//        TRAppReportSummary = fragmentView.findViewById(R.id.TRAppReportSummary);
//        TRAppReportSummary.setOnClickListener(this);
//        TRAppReportSummaryFull.setOnClickListener(this);
//        anim.scaleView(TRAppReportSummaryFull, 0f, 0f);
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
        // Toast.makeText(getContext(), "Retrieving list of expenses", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                TRServer.ServerSync(context, new RequestCallback() {
                    @Override
                    public void onResponse(Object response) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Successfully retrieved expenses", Toast.LENGTH_LONG).show();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                initializeExpenseList();
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Something wrong with the network", Toast.LENGTH_LONG).show();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                initializeExpenseList();
                            }
                        });
                    }
                });
            }
        }, 0);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.fabMain:
                intent = new Intent(context, CreateExpenseActivity.class);
                startActivityForResult(intent, 101);
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
                // mListener.onFragmentInteraction(ViewScreen.VIEW_CREATE_EXPENSE, false);
                break;

            case R.id.DB_expenseCategoryFood:
                intent = new Intent(context, CreateExpenseActivity.class);
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
                    // String expenseDesc = data.getStringExtra("expenseDesc");
                    if (DataService.getExpenseEntity() != null) {
                        Toast.makeText(getContext(), "Successfully added " + DataService.getExpenseEntity().getExpenseDescription(), Toast.LENGTH_LONG).show();
                        updateExpenseList(DataService.getExpenseEntity(), expenseEntities);
                        DataService.destroyExpenseEntity();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExpenseArrayAdapterEntity expenseArrayAdapterEntity = (ExpenseArrayAdapterEntity) expenseListAdapter.getItem(position);
        Log.i("ArrayAdapterClick", "onItemClick: " + expenseArrayAdapterEntity.toString());
        ExpenseEntity expenseEntity = appDB.expenseDao().findByID(Long.valueOf(expenseArrayAdapterEntity.getExpenseID())).get(0);
        showAlertRemoveDialog(true, true, "Delete " + expenseArrayAdapterEntity.getExpenseDescription() + " from the list?", expenseEntity, position);
    }

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
        initializeExpenseList();
        super.onStart();
    }

    private void showAlertRemoveDialog(boolean show, boolean forListView, @Nullable String message, final ExpenseEntity expenseEntity, final int position) {
        if (show) {
            if (removeDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if (expenseEntity.getExpenseType() == 0) builder.setTitle("Remove expense");
                else builder.setTitle("Remove gain");
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // appDB.expenseDao().deleteByID(ExpenseID);
                        if (position == -1) {
                            Toast.makeText(context, "Something went wrong while removing entry", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("WEWSU", "onClick: " + String.valueOf(expenseEntity.getActive()));
                            expenseEntity.setActive(false);
                            expenseEntity.setUpdated(false);
                            appDB.expenseDao().UpdateExpense(expenseEntity);
                            expenseEntities.subList(position, position + 1).clear();
                            Toast.makeText(context, "Removed " + expenseEntity.getExpenseDescription() + " from the list.", Toast.LENGTH_SHORT).show();
                            Log.i("WEWSU", "onClick: " + appDB.expenseDao().findEntity(expenseEntity.getDateCreated(), expenseEntity.getExpenseValue(), expenseEntity.getExpenseType()).toString());
                            updateExpenseList(null, expenseEntities);

                        }
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

    private void initializeExpenseList() {
        expenseArrayAdapterEntitiesList = fetchDataAdapterArray();
        if (expenseListAdapter == null) {
            expenseListAdapter = new ExpenseListAdapter(context, expenseArrayAdapterEntitiesList);
        }
        if (listView.getAdapter() == null) {
            listView.setAdapter(expenseListAdapter);
        } else {
            expenseListAdapter.clear();
            expenseListAdapter.addAll(expenseArrayAdapterEntitiesList);  //update adapter's data
            expenseListAdapter.notifyDataSetChanged(); //notifies any View reflecting data to refresh
        }
        setupGraph();

    }

    private void updateExpenseList(ExpenseEntity expenseEntity, ArrayList<ExpenseEntity> expenseEntities) {
        if (expenseEntity != null) {
            expenseEntities.add(0, expenseEntity);
        }
        expenseArrayAdapterEntitiesList = configureExpenseEntitiesForListView(expenseEntities);
        expenseListAdapter.clear();
        expenseListAdapter.addAll(expenseArrayAdapterEntitiesList);  //update adapter's data
        expenseListAdapter.notifyDataSetChanged(); //notifies any View reflecting data to refresh
        setupGraph();
    }

    private ArrayList<ExpenseArrayAdapterEntity> fetchSingleDataAdapterArray() {
        ArrayList<ExpenseArrayAdapterEntity> data = new ArrayList<>();
        String curMonth = "", curDay = "";
        boolean showDay = true, showMonthYear = true;
        for (ExpenseEntity expenseEntity : appDB.expenseDao().findAllActive()) {
            showDay = !curDay.equals(Util.formatDateTimeGetDayText(expenseEntity.getDateCreated()));
            showMonthYear = !curMonth.equals(Util.formatDateTimeGetMonth(expenseEntity.getDateCreated()));
            ExpenseArrayAdapterEntity expenseArrayAdapterEntity = new ExpenseArrayAdapterEntity(expenseEntity, showDay, showMonthYear);
            data.add(expenseArrayAdapterEntity);
            curMonth = Util.formatDateTimeGetMonth(expenseEntity.getDateCreated());
            curDay = Util.formatDateTimeGetDayText(expenseEntity.getDateCreated());
        }
        return data;
    }


    private ArrayList<ExpenseArrayAdapterEntity> fetchDataAdapterArray() {
        expenseEntities = (ArrayList<ExpenseEntity>) appDB.expenseDao().findAllActive();
        return configureExpenseEntitiesForListView(expenseEntities);
    }

    private ArrayList<ExpenseArrayAdapterEntity> configureExpenseEntitiesForListView(ArrayList<ExpenseEntity> expenseEntitiesArray) {
        ArrayList<ExpenseArrayAdapterEntity> data = new ArrayList<>();
        String curMonth = "", curDay = "";
        boolean showDay = true, showMonthYear = true;
        for (ExpenseEntity expenseEntity : expenseEntitiesArray) {
            showDay = !curDay.equals(Util.formatDateTimeGetDayText(expenseEntity.getDateCreated()));
            showMonthYear = !curMonth.equals(Util.formatDateTimeGetMonth(expenseEntity.getDateCreated()));
            ExpenseArrayAdapterEntity expenseArrayAdapterEntity = new ExpenseArrayAdapterEntity(expenseEntity, showDay, showMonthYear);
            data.add(expenseArrayAdapterEntity);
            curMonth = Util.formatDateTimeGetMonth(expenseEntity.getDateCreated());
            curDay = Util.formatDateTimeGetDayText(expenseEntity.getDateCreated());
        }
        return data;
    }


    private void setupGraph() {
        // LineChartView chart = getActivity().findViewById(R.id.line_chart);

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
        double TotalExpense = 0;
        double TotalGain = 0;
        Long entryDate = appDB.expenseDao().findFirstEntryDate();
        Long endDate = appDB.expenseDao().findLastEntryDate();

        if (entryDate != null) {
            entryDate = Long.valueOf(String.valueOf(entryDate).substring(0, 5));
            endDate = Long.valueOf(String.valueOf(endDate).substring(0, 5));


            duckTape.add(new PointValue(0, 0));
            for (long start = entryDate; start <= endDate; start++) {
                double TotalOfDay = 0;
                double TotalExpenseOfDay = 0;
                double TotalGainOfDay = 0;
                List<ExpenseEntity> expenseEntities = appDB.expenseDao().findExpensesPerDay(String.valueOf(start) + "%");
                for (ExpenseEntity expenseEntity : expenseEntities) {
                    if (expenseEntity.getExpenseDescription().equals("wewsu")) {
                        Log.i("WEWSU", "setupGraph: " + expenseEntity.toString());
                    }
                    if (expenseEntity.getExpenseType() == 0) {
                        TotalOfDay = TotalOfDay - expenseEntity.getExpenseValue();
                        TotalExpenseOfDay = TotalExpenseOfDay + expenseEntity.getExpenseValue();
                    } else {
                        TotalOfDay = TotalOfDay + expenseEntity.getExpenseValue();
                        TotalGainOfDay = TotalGainOfDay + expenseEntity.getExpenseValue();
                    }
                }
                Total = Total + TotalOfDay;
                TotalGain = TotalGain + TotalGainOfDay;
                TotalExpense = TotalExpense + TotalExpenseOfDay;
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
        expenseTotalTxtView.setText(String.valueOf(Total));
        TotalGainView.setText("+" + String.valueOf(TotalGain));
        TotalExpenseView.setText("-" + String.valueOf(TotalExpense));
    }
}
