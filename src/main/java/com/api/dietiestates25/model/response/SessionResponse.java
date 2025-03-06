package com.api.dietiestates25.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.api.dietiestates25.throwable.*;

@Getter
@Setter
public class SessionResponse {
    private String sessionid;
    private String message;

    public SessionResponse() { }

    public ResponseEntity<SessionResponse> toHttpResponse() {
        if (sessionid != null)
            return ResponseEntity.ok(this);
        else if (message.contains("Invalid credentials."))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(this);
        else if (message.contains("required value"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this);
    }
    public ResponseEntity<SessionResponse> toHttpResponse(Exception ex) {
        if(ex instanceof  RequiredParameterException) {
            message = "Error: the parameter " + ex.getMessage() + " is required!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this);
        }
        message = ex.ToString();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this);
    }
}
