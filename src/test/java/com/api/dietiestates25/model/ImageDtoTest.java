package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.ImageDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageDtoTest {

    @Test
    void testConstructor() {
        ImageDTO image= new ImageDTO("test",null);

        assertEquals("test", image.getFileName());
    }

}
