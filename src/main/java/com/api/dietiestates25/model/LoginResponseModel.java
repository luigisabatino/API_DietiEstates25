package com.api.dietiestates25.model;

public class LoginResponseModel {
    private String sessionid;
    private String message;

    public LoginResponseModel() { }

    public String getSessionid() { return sessionid; }
    public void setSessionid(String _sessionid) { sessionid = _sessionid; }
    public String getMessage() { return message; }
    public void setMessage(String _message) { message = _message; }
}
