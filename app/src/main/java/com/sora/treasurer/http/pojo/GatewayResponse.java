package com.sora.treasurer.http.pojo;


import com.sora.treasurer.TAppEnums.RequestType;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class GatewayResponse {
    private RequestType requestType;

    public RequestType getRequestType() { return this.requestType; }
    public void setRequestType(RequestType requestType) { this.requestType = requestType; }
}
