package com.api.dietiestates25.model.response;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DetailEntityResponse<T> {
    String message = "";
    List<T> entities;
}
