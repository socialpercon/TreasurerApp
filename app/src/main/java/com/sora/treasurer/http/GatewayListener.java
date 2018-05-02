package com.sora.treasurer.http;

import com.android.volley.VolleyError;
import com.sora.treasurer.TAppEnums.RequestType;
import com.sora.treasurer.http.pojo.GatewayResponse;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public interface GatewayListener {
    void onResponse(GatewayResponse gatewayResponse);
    void onErrorResponse(VolleyError error, RequestType requestType);
}
