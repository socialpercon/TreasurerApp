package com.sora.treasurer.http;



import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.sora.treasurer.TAppEnums.RequestType;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.http.pojo.CreateResponse;
import com.sora.treasurer.http.pojo.ExpenseEntityResponse;
import com.sora.treasurer.http.pojo.ExpenseGetResponse;
import com.sora.treasurer.http.pojo.GatewayResponse;
import com.sora.treasurer.http.pojo.LoginResponse;
import com.sora.treasurer.http.pojo.UpdateResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class TRGateway extends Gateway {
    private static final String TAG = "TRGateway";
    private boolean local = true;
    private final String[] API_SERVER = new String[]{"test-", "stage-", ""};
    private final int API_MODE = 0;
    private final String API_BASE_ENDPOINT = local? "https://1d6213bb.ngrok.io" : "http://treasurerapp.fr.openode.io";
    private final String AUTH_BASE_ENDPOINT = "https://" + API_SERVER[API_MODE] + "auth.base.study";
    public TRGateway(Context context, GatewayListener callback) {
        super(context, callback);
        this.setReqMap();
    }

    // this creates a default request mapping intended for pleasant event app
    private void setReqMap() {
        HashMap<RequestType, Class<? extends GatewayResponse>> hmap = new HashMap<>();
        hmap.put(RequestType.REQ_LOGIN, LoginResponse.class);
        hmap.put(RequestType.REQ_EXPENSE_CREATE, CreateResponse.class);
        hmap.put(RequestType.REQ_EXPENSE_UPDATE, UpdateResponse.class);
        hmap.put(RequestType.REQ_EXPENSE_GET_ALL, ExpenseGetResponse.class);
        hmap.put(RequestType.REQ_EXPENSE_GET_BY_ID, ExpenseGetResponse.class);
        hmap.put(RequestType.REQ_EXPENSE_GET_BY_TYPE, ExpenseGetResponse.class);
        hmap.put(RequestType.REQ_EXPENSE_ENTITY, ExpenseEntityResponse.class);
        setRequestMapping(hmap);
    }

    public TRGateway setResponseListener(GatewayListener l) {
        this.mGatewayListener = l;
        return this;
    }

    //sample request
    public void requestLogin(String username, String password) {
        String endpoint = AUTH_BASE_ENDPOINT + "/api/app/login?userNsame=" + username + "&password=" + password + "&appTag=GGU&APISecret=secretkey";
        this.mGatewayResponse.setRequestType(RequestType.REQ_LOGIN);
        Log.d(TAG, "requestLogin: " + endpoint);
        StringRequest getRequest = new StringRequest(Request.Method.GET, endpoint, onRequestLoaded, onRequestError) {
        };
        this.mRequestQueue.add(getRequest);
    }

    //get all
    public void expenseAPIGetAll() {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/get/all";
        Log.d(TAG, "expenseAPIGetAll: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_GET_ALL);
        StringRequest getRequest = new StringRequest(Request.Method.GET, endpoint, onRequestLoaded, onRequestError) {
        };
        this.mRequestQueue.add(getRequest);
    }

    //get except ID
    public void expenseAPIGetExcept(final List<Long> ExpenseIDs) {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/get/except";
        Log.d(TAG, "expenseAPIGetAll: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_GET_ALL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, endpoint, onRequestLoaded, onRequestError) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.i("WEW", "getParams: " + ExpenseIDs.toString());
                params.put("ExpenseIDs",ExpenseIDs.toString());
                return params;
            }
        };
        this.mRequestQueue.add(postRequest);
    }

    //get except ID
    public void expenseAPIGetExceptEntity(final List<ExpenseEntity> expenseEntities) {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/get/except";
        Log.i("WEW", endpoint);
        Log.d(TAG, "expenseAPICreate: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_GET_ALL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, endpoint, onRequestLoaded, onRequestError) {
            @Override
            protected Map<String, String> getParams() {
                List<Long> expenseDates = new ArrayList<>();
                Long CreatedBy = Long.valueOf(0);
                for(ExpenseEntity expenseEntity:expenseEntities){
                    expenseDates.add(expenseEntity.getDateCreated());
                }
                Map<String, String> params = new HashMap<>();
                Log.i("WEW", "getParams: " + expenseDates.toString());
                params.put("DatesCreated",expenseDates.toString());
                params.put("CreatedBy",CreatedBy.toString());
                return params;
            }
        };
        this.mRequestQueue.add(postRequest);
    }

    public void expenseAPIGetExceptEntityx(final List<ExpenseEntity> expenseEntities) {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/get/except";
        Log.d(TAG, "expenseAPIGetAll: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_GET_ALL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, endpoint, onRequestLoaded, onRequestError) {
            @Override
            protected Map<String, String> getParams() {
                List<Long> expenseDates = new ArrayList<>();
                Long CreatedBy = expenseEntities.get(0).getCreatedBy();
                for(ExpenseEntity expenseEntity:expenseEntities){
                    expenseDates.add(expenseEntity.getDateCreated());
                }
                Map<String, String> params = new HashMap<>();
                Log.i("WEW", "getParams: " + expenseDates.toString());
                params.put("DatesCreated",expenseDates.toString());
                params.put("CreatedBy",CreatedBy.toString());
                return params;
            }
        };
        this.mRequestQueue.add(postRequest);
    }

    //getby ID
    public void expenseAPIGetByID(long id) {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/get/byid?id=" + Long.toString(id);
        Log.d(TAG, "expenseAPIGetByID: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_GET_BY_ID);
        StringRequest getRequest = new StringRequest(Request.Method.GET, endpoint, onRequestLoaded, onRequestError) {
        };
        this.mRequestQueue.add(getRequest);
    }

    //create new
    public void expenseAPICreate(final ExpenseEntity expenseEntity) {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/create";
        Log.d(TAG, "expenseAPICreate: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_CREATE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, endpoint, onRequestLoaded, onRequestError) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ExpenseID",String.valueOf(expenseEntity.getExpenseID()));
                params.put("ExpenseType",String.valueOf(expenseEntity.getExpenseType()));
                params.put("ExpenseDescription", expenseEntity.getExpenseDescription());
                params.put("ExpenseValue", String.valueOf(expenseEntity.getExpenseValue()));
                params.put("CreatedBy", String.valueOf(expenseEntity.getCreatedBy()));
                params.put("DateCreated", String.valueOf(expenseEntity.getDateCreated()));
                params.put("DateModified", String.valueOf(expenseEntity.getDateModified()));
                return params;
            }
        };
        this.mRequestQueue.add(postRequest);
    }

    public void expenseAPICreate(final List<ExpenseEntity> expenseEntities) {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/create/many";
        Log.d(TAG, "expenseAPICreate: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_ENTITY);

        JSONArray arr = new JSONArray();
        for(ExpenseEntity expenseEntity: expenseEntities) {
            arr.put(expenseEntity.getJsonObject());
        }
        CustomJsonRequest jsonArrayRequest = new CustomJsonRequest(Request.Method.POST, endpoint, arr, onJsonRequestLoaded, onRequestError);
        this.mRequestQueue.add(jsonArrayRequest);
    }
    public void expenseAPIRemove(final ExpenseEntity expenseEntity) {
        String endpoint = API_BASE_ENDPOINT + "/api/expense/remove";
        Log.d(TAG, "expenseAPICreate: " + endpoint);
        this.mGatewayResponse.setRequestType(RequestType.REQ_EXPENSE_CREATE);
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, endpoint, onRequestLoaded, onRequestError) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ExpenseID",String.valueOf(expenseEntity.getExpenseID()));
                return params;
            }
        };
        this.mRequestQueue.add(postRequest);
    }
}
