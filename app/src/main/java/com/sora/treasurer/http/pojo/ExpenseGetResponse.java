package com.sora.treasurer.http.pojo;

import com.sora.treasurer.database.entities.ExpenseEntity;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class ExpenseGetResponse extends GatewayResponse {

    private int Status;
    private String Description;
    private int HTTPCode;
    private ExpenseEntity Data[];

    public int getStatus() { return Status; }
    public void setStatus(int status) { Status = status; }
    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }
    public int getHTTPCode() { return HTTPCode; }
    public void setHTTPCode(int HTTPCode) { this.HTTPCode = HTTPCode; }
    public ExpenseEntity[] getData() { return Data; }
    public void setData(ExpenseEntity[] data) { Data = data; }
}
