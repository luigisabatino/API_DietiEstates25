package com.api.dietiestates25.model.dto;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DetailEntityDTO<T> {
    String message = "";
    List<T> entities;
}
