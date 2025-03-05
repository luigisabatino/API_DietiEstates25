package com.api.dietiestates25.model.response;

import lombok.Getter;
import lombok.Setter;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EntityResponse<T> extends CodeResponse {
    private List<T> entities;
    public EntityResponse() {
        entities = new ArrayList<T>();
    }
    public void addInEntities(T value) {
        entities.add(value);
    }
}
