package com.api.dietiestates25.model.dto;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class ImageDTO {
    private String fileName;
    private String base64Image;

    public ImageDTO(String fileName, String base64Image) {
        this.fileName = fileName;
        this.base64Image = base64Image;
    }

}

