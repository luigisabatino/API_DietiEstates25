package com.api.dietiestates25.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeResponse {
    private int code;
    private String message;

    public CodeResponse() { }
}
