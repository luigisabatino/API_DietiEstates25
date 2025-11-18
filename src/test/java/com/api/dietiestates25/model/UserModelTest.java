package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.user.*;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserModelTest {

    @Test
    void testConstructorLoginDto() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("email@test.com");
        UserModel usr = new UserModel(dto);
        assertEquals("email@test.com", usr.getEmail());
    }
    @Test
    void testConstructorCreateUserDto() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setEmail("email@test.com");
        UserModel usr = new UserModel(dto);
        assertEquals("email@test.com", usr.getEmail());
    }
    @Test
    void testConstructorConfirmUserDto() {
        ConfirmUserDTO dto = new ConfirmUserDTO();
        dto.setEmail("email@test.com");
        UserModel usr = new UserModel(dto);
        assertEquals("email@test.com", usr.getEmail());
    }
    @Test
    void testConstructorEmail() {
        UserModel usr = new UserModel("email@test.com");
        assertEquals("email@test.com", usr.getEmail());
    }
    @Test
    void testVoidSetterAndEncoder() {
        UserModel usr = new UserModel();
        usr.setPwd();
        usr.setOtp();
        usr.encodePwd();
        usr.encodeOtp();
        assertTrue(true);
    }
    @Test
    void testConstructorRs() throws SQLException {
        ResultSet rsMock = mock(ResultSet.class);
        when(rsMock.getString("email")).thenReturn("test@test.com");
        when(rsMock.getString("firstName")).thenReturn("Luigi");
        when(rsMock.getString("lastName")).thenReturn("Sabatino");
        when(rsMock.getString("company")).thenReturn("IT123456");
        when(rsMock.getString("usertype")).thenReturn("U");
        when(rsMock.getBoolean("confirmed")).thenReturn(true);
        when(rsMock.getString("companyname")).thenReturn("Test Srl");
        UserModel usr = new UserModel(rsMock);
        assertEquals("Luigi", usr.getFirstName());
    }
}
