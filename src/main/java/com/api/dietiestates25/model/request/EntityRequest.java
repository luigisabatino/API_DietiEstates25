package com.api.dietiestates25.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityRequest<T> {
    private String sessionId;
    private T entity;
}
