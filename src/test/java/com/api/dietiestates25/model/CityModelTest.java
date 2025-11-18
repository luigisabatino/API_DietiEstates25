package com.api.dietiestates25.model;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityModelTest {

    @Test
    void testConstructorRs() throws SQLException {
        ResultSet rsMock = mock(ResultSet.class);
        when(rsMock.getString("name")).thenReturn("L£Aquila");
        when(rsMock.getString("istat_code")).thenReturn("123");
        CityModel city = new CityModel(rsMock);
        assertEquals("L'Aquila", city.getName());
    }
}
