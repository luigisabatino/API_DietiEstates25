package com.api.dietiestates25.model.response;

import com.api.dietiestates25.model.dto.DetailEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
@Getter
@Setter
public class CodeEntitiesResponse<T> extends CodeResponse {
    private List<T> entities;
    public CodeEntitiesResponse() {
        entities = new ArrayList<>();
    }
    public CodeEntitiesResponse(CodeResponse cr) {
        this.setCode(cr.getCode());
        this.setMessage(cr.getMessage());
        entities = new ArrayList<>();
    }
    public void addInEntities(T value) {
        entities.add(value);
    }

    public ResponseEntity<DetailEntityDTO<T>> toHttpEntitiesResponse(Exception ex) {
        setCodeByException(ex);
        if((getMessage()==null)||(getMessage().isBlank()))
            setMessageFromCode();
        var response = new DetailEntityDTO<T>();
        response.setMessage(getMessage());
        response.setEntities(entities);
        return ResponseEntity.status(httpStatusFromCode()).body(response);
    }
    public ResponseEntity<DetailEntityDTO<T>> toHttpEntitiesResponse() {
        return toHttpEntitiesResponse(null);
    }
}
