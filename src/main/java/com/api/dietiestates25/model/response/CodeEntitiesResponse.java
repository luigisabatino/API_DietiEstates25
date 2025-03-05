package com.api.dietiestates25.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
}
