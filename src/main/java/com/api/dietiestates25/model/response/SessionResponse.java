package com.api.dietiestates25.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class SessionResponse {
    private String sessionid;
    private String message;

    public SessionResponse() { }

    public ResponseEntity<SessionResponse> getFormattedResponse() {
        if (sessionid != null) {
            return ResponseEntity.ok(this);
        } else if (message.contains("Invalid credentials.")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(this);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this);
        }

    }
}
