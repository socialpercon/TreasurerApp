package com.sora.treasurer.http.pojo;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class DeleteResponse extends GatewayResponse {

    private int Status;
    private String Description;
    private int HTTPCode;
    private DeleteRes Data;

    class DeleteRes {
        private boolean Success;
        private String Message;

        public boolean isSuccess() { return Success; }
        public void setSuccess(boolean success) { Success = success; }
        public String getMessage() { return Message; }
        public void setMessage(String message) { Message = message; }
    }

    public int getStatus() { return Status; }
    public void setStatus(int status) { Status = status; }
    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }
    public int getHTTPCode() { return HTTPCode; }
    public void setHTTPCode(int HTTPCode) { this.HTTPCode = HTTPCode; }
    public DeleteRes getData() { return Data; }
    public void setData(DeleteRes data) { Data = data; }
}
