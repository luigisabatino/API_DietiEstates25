package com.api.dietiestates25.model.request;

import com.api.dietiestates25.model.AdModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdRequest extends AdModel {
    private String sessionId;
}
