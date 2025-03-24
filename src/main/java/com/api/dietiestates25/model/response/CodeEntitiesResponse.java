package com.api.dietiestates25.model.response;

import com.api.dietiestates25.throwable.RequiredParameterException;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Getter
@Setter
public class CodeEntitiesResponse<T> extends CodeResponse {
    private List<T> entities;
    public CodeEntitiesResponse() {
        entities = new ArrayList<T>();
    }
    public void addInEntities(T value) {
        entities.add(value);
    }

    public ResponseEntity<DetailEntityResponse<T>> toHttpEntitiesResponse(Exception ex) {
        var response = new DetailEntityResponse<T>();
        if((getMessage().isBlank()))
            setMessageFromCode();
        response.setMessage(getMessage());
        response.setEntities(entities);
        return ResponseEntity.status(httpStatusFromCode(ex)).body(response);
    }
    public ResponseEntity<DetailEntityResponse<T>> toHttpEntitiesResponse() {
        return toHttpEntitiesResponse(null);
    }
}
