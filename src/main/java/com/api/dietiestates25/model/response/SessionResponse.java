package com.api.dietiestates25.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionResponse {
    private String sessionid;
    private String message;

    public SessionResponse() { }

    public String getSessionid() { return sessionid; }
    public void setSessionid(String _sessionid) { sessionid = _sessionid; }
    public String getMessage() { return message; }
    public void setMessage(String _message) { message = _message; }
}
