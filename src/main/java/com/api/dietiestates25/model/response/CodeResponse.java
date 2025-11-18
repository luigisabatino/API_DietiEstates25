package com.api.dietiestates25.model.response;

import com.api.dietiestates25.throwable.NoMatchCredentialsException;
import com.api.dietiestates25.throwable.RequiredParameterException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class CodeResponse  {
    private int code;
    private String message;

    public CodeResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public CodeResponse() {}
    protected void setMessageFromCode() {
        switch (code) {
            case 0, -5:
                message = "Operation successful.";
                break;
            case -1:
                message = "Error: Invalid session.";
                break;
            case -2:
                message = "Error: You don't have permission to perform the requested operation.";
                break;
            case -3:
                message = "Error: A user with this email already exists in our system.";
                break;
            case -4:
                message = "Error: Impossible to insert bid for this ad.";
                break;
            case -6:
                message = "Error: Invalid value.";
                break;
            case -7:
                message = "Error: temporary password must be changed";
                break;
            case -97:
                message = "Error: Invalid VAT Number.";
                break;
            case -20:
                message = "Error: Invalid ID.";
                break;
            default:
                if (code > 0) message = "Operation successful.";
                break;
        }
    }
    protected HttpStatus httpStatusFromCode() {
        switch (code) {
            case 0:
                return HttpStatus.OK;
            case -1, -2:
                return HttpStatus.UNAUTHORIZED;
            case -3, -4:
                return HttpStatus.CONFLICT;
            case -5:
                return HttpStatus.CREATED;
            case -6, -97, -98:
                return HttpStatus.BAD_REQUEST;
            case -7:
                return HttpStatus.CONTINUE;
            case -99:
                return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                if (code > 0) {
                    return HttpStatus.CREATED;
                } else
                    return HttpStatus.NOT_FOUND;
        }
    }
    public ResponseEntity<String> toHttpMessageResponse() {
        return toHttpMessageResponse(null);
    }
    public ResponseEntity<String> toHttpMessageResponse(Exception ex) {
        setCodeByException(ex);
        if(message == null || message.isBlank())
            setMessageFromCode();
        return ResponseEntity.status(httpStatusFromCode()).body(message);
    }
    protected void setCodeByException(Exception ex) {
        if(ex != null) {
            if(ex instanceof RequiredParameterException) {
                message = "Error: the parameter " + ex.getMessage() + " is required!";
                code = -98;
            }
            else if(ex instanceof NoMatchCredentialsException) {
                message = "Error: invalid credentials!";
                code = -2;
            }
            else if(ex instanceof EmptyResultDataAccessException) {
                code = -20;
            }
            else {
                code = -99;
                message = ex.toString();
            }
        }
    }
}
