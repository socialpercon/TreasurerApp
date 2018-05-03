package com.sora.treasurer.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sora.treasurer.TAppEnums.RequestType;
import com.sora.treasurer.http.pojo.GatewayResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

class Gateway {

    private Context mContext;
    RequestQueue mRequestQueue;
    private Gson mGson;
    GatewayListener mGatewayListener;
    GatewayResponse mGatewayResponse;
    private String TAG = "gateway";
    private HashMap<RequestType, Class<? extends GatewayResponse>> mRequestMap;

    Gateway(Context pContext, GatewayListener callback) {
        this.mContext = pContext;
        this.mRequestQueue = Volley.newRequestQueue(this.mContext);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        this.mGson = gsonBuilder.create();
        this.mGatewayListener = callback;
        this.mGatewayResponse = new GatewayResponse();
        this.mRequestMap = new HashMap<>();
    }

    // this updates the request mapping if used outside
    void setRequestMapping(HashMap<RequestType, Class<? extends GatewayResponse>> hmap) {
        this.mRequestMap.clear();
        this.mRequestMap.putAll(hmap);
    }

    // generic handling of request success response based from request mapping
    final Response.Listener<String> onRequestLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                RequestType requestType = mGatewayResponse.getRequestType();
                Log.i(TAG, "onResponse: " +  response);
                Log.i(TAG, "onResponse: " + requestType.toString());
                mGatewayResponse = mGson.fromJson(response, mRequestMap.get(requestType));
                mGatewayResponse.setRequestType(requestType);
                if (mGatewayListener != null) mGatewayListener.onResponse(mGatewayResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    final Response.Listener<JSONObject> onJsonRequestLoaded = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                RequestType requestType = mGatewayResponse.getRequestType();
                Log.i(TAG, "onResponse: " +  response);
                Log.i(TAG, "onResponse: " + requestType.toString());
                mGatewayResponse = mGson.fromJson(response.toString(), mRequestMap.get(requestType));
                mGatewayResponse.setRequestType(requestType);
                if (mGatewayListener != null) mGatewayListener.onResponse(mGatewayResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // generic handling of request error response
    final Response.ErrorListener onRequestError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if (mGatewayListener != null)
                mGatewayListener.onErrorResponse(error, mGatewayResponse.getRequestType());
        }
    };
}
