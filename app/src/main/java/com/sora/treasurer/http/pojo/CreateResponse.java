package com.sora.treasurer.http.pojo;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class CreateResponse extends GatewayResponse {
    private int Status;
    private String Description;
    private int HTTPCode;
    private String ExpenseServerID;

    public int getStatus() { return Status; }
    public void setStatus(int status) { Status = status; }
    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }
    public int getHTTPCode() { return HTTPCode; }
    public void setHTTPCode(int HTTPCode) { this.HTTPCode = HTTPCode; }
    public String getData() { return ExpenseServerID; }
    public void setData(String data) { ExpenseServerID = data; }
}
