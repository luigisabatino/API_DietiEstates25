package com.api.dietiestates25.model.dto;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class ImageDTO {
    private String fileName;
    private String base64Image;

    public ImageDTO(String _fileName, String _base64Image) {
        fileName = _fileName;
        base64Image = _base64Image;
    }

}

