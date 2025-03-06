package com.api.dietiestates25.model.response;

import com.api.dietiestates25.throwable.RequiredParameterException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class CodeResponse {
    private int code;
    private String message;

    public CodeResponse() {
    }

    public ResponseEntity<CodeResponse> toHttpResponse() {
        if (message.isBlank())
            setMessageFromCode();
        switch (code) {
            case 0:
                return ResponseEntity.status(HttpStatus.OK).body(this);
            case -1:
            case -2:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(this);
            case -3:
            case -4:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(this);
            case -5:
                return ResponseEntity.status(HttpStatus.CREATED).body(this);
            case -6:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this);
            case -7:
                return ResponseEntity.status(HttpStatus.CONTINUE).body(this);
            case -99:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this);
            default:
                if (code > 0) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(this);
                } else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this);
        }
    }

    public void setMessageFromCode() {
        switch (code) {
            case 0:
            case -5:
                message = "Operation successfull.";
            case -1:
                message = "Error: Invalid session.";
            case -2:
                message = "Error: Don't have permission to perform the requested operation.";
            case -3:
                message = "Already exist a user with this email in our systems.";
            case -4:
                message = "Impossible to insert bid for this ad.";
            case -6:
                message = "Invalid value.";
            case -7:
                message = "temporary password must be change";
            default:
                if (code > 0) {
                    message = "Operation successfull.";
                }
        }
    }
    public ResponseEntity<CodeResponse> toHttpResponse(Exception ex) {
        if(ex instanceof RequiredParameterException) {
            message = "Error: the parameter " + ex.getMessage() + " is required!";
            code = -98;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this);
        }
        code = -99;
        message = ex.toString();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this);
    }

}
