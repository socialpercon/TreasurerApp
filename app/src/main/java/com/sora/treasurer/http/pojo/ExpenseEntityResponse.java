package com.sora.treasurer.http.pojo;

import com.sora.treasurer.database.entities.ExpenseEntity;

import org.json.JSONObject;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class ExpenseEntityResponse extends GatewayResponse {


    private ExpenseEntity Data[];

    public ExpenseEntity[] getData() { return Data; }
    public void setData(ExpenseEntity[] data) { Data = data; }
}
