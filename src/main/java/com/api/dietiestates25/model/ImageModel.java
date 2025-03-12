package com.api.dietiestates25.model;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class ImageModel {
    private String fileName;
    private String base64Image;

    public ImageModel(String _fileName, String _base64Image) {
        fileName = _fileName;
        base64Image = _base64Image;
    }

}

