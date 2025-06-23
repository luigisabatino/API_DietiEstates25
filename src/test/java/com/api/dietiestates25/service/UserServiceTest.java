package com.api.dietiestates25.service;

import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.throwable.NoMatchCredentialsException;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private UserModel user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserModel();
        user.setEmail("mail@test.com");
        user.setPwd("testpwd");
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setOtp("123456");
    }

    @Test
    void testLoginSuccess() {
        String hashedPwd = new BCryptPasswordEncoder().encode(user.getPwd());
        when(jdbcTemplate.queryForObject(eq("SELECT PWD FROM USERS WHERE email = ?"),
                eq(String.class), any())).thenReturn(hashedPwd);
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM LOGIN(?, ?)"),
                any(RowMapper.class),
                eq(user.getEmail()),
                anyString()))
                .thenReturn(new CodeResponse(0, "SESSIONID123"));
        CodeResponse response = userService.login(jdbcTemplate, user);
        assertNotNull(response);
        assertEquals("SESSIONID123", response.getMessage());
        assertEquals(0, response.getCode());
    }
    @Test
    void testLoginFailsWrongPassword() {
        String hashedPwd = new BCryptPasswordEncoder().encode("errpwd");
        when(jdbcTemplate.queryForObject(eq("SELECT PWD FROM USERS WHERE email = ?"),
                eq(String.class), any())).thenReturn(hashedPwd);
        assertThrows(NoMatchCredentialsException.class, () -> {
            userService.login(jdbcTemplate, user);
        });
    }
    @Test
    void TestCreateUserSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT CREATE_TEMP_USER(?,?,?,?,?)"),
                eq(Integer.class), any(), any(), any(), any(), any()))
                .thenReturn(0);
        int response = userService.createUser(jdbcTemplate, user);
        assertEquals(0, response);
    }
    @Test
    void TestCreateUserNoRequiredValues() {
        user.setEmail("");
        assertThrows(RequiredParameterException.class, () -> {
            userService.createUser(jdbcTemplate, user);
        });
    }
    @Test
    void TestConfirmUserSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM CONFIRM_USER(?, ?)"),
                eq(Integer.class), any(), any()))
                .thenReturn(0);
        int response = userService.confirmUser(jdbcTemplate, user);
        assertEquals(0, response);
    }
    @Test
    void TestConfirmManagerOrAgentSuccess() {
        user.setCompany("0001");
        user.setUserType("A");
        String hashedPwd = new BCryptPasswordEncoder().encode(user.getPwd());
        when(jdbcTemplate.queryForObject(eq("SELECT PWD FROM USERS WHERE email = ?"),
                eq(String.class), any())).thenReturn(hashedPwd);

        when(jdbcTemplate.queryForObject(eq("SELECT * FROM CONFIRM_MANAGERORAGENT(?, ?, ?)"),
                eq(Integer.class), any(),any(),any())).thenReturn(0);
        int response = userService.confirmManagerOrAgent(jdbcTemplate, user);
        assertEquals(0, response);
    }
    @Test
    public void TestCreateAgentSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM CREATE_TEMP_AGENT(?,?,?,?,?)"),
                eq(Integer.class), any(),any(),any(),any(),any())).thenReturn(0);
        int response = userService.createAgent(jdbcTemplate, user, "SESSIONID123");
        assertEquals(0,response);
    }
    @Test
    public void TestGetAgentsByCompanySuccess() {
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);
        try {
            Mockito.when(resultSetMock.next()).thenReturn(true, false); // Solo una riga
            Mockito.when(resultSetMock.getString("EMAIL")).thenReturn("agent1@test.com");
            Mockito.when(resultSetMock.getString("FIRST_NAME")).thenReturn("Mario");
            Mockito.when(resultSetMock.getString("LAST_NAME")).thenReturn("Rossi");
        }
        catch(SQLException sqlEx) {}
        when(jdbcTemplate.query(eq("SELECT * FROM USER_COMPANY WHERE COMPANY = ? AND CONFIRMED = TRUE"), any(Object[].class), any(RowMapper.class)))
                .thenAnswer(invocation -> {
                    RowMapper<UserModel> rowMapper = invocation.getArgument(2);
                    List<UserModel> userList = new ArrayList<>();
                    userList.add(rowMapper.mapRow(resultSetMock, 1));
                    return userList;
                });
        var response = userService.getAgentsByCompany(jdbcTemplate, "0001");
        assertNotNull(response);
        assertEquals(1, response.size());
    }
    @Test
    public void TestGetUserByEmailSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM USER_COMPANY WHERE EMAIL = ?"),
                any(RowMapper.class), eq(user.getEmail())))
                .thenReturn(user);
        var response = userService.getUserByEmail(jdbcTemplate, user.getEmail());
        assertEquals(user,response);
    }
    @Test
    public void TestLogoutSuccess() {
        when(jdbcTemplate.update("DELETE FROM SESSIONS WHERE SESSIONID = ?", "SESSIONID123"))
                .thenReturn(1);
        var response = userService.logout(jdbcTemplate, "SESSIONID123");
        assertTrue(response);
    }
    @Test
    public void TestChangePwdSuccess() {
        when(jdbcTemplate.update(eq("UPDATE USERS SET PWD = ? WHERE EMAIL = ? AND OTP = ?"), any(), any(), any()))
                .thenReturn(1);
        var response = userService.changePwd(jdbcTemplate, user);
        assertEquals(1,response);
    }
    @Test
    public void TestLoad3PartUserSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM USER_COMPANY WHERE EMAIL = ?"),
                any(RowMapper.class), eq(user.getEmail())))
                .thenReturn(user);
        String hashedPwd = new BCryptPasswordEncoder().encode(user.getPwd());
        when(jdbcTemplate.queryForObject(eq("SELECT PWD FROM USERS WHERE email = ?"),
                eq(String.class), any())).thenReturn(hashedPwd);
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM LOGIN(?, ?)"),
                any(RowMapper.class),
                eq(user.getEmail()),
                anyString()))
                .thenReturn(new CodeResponse(0, "SESSIONID123"));
        CodeResponse response = userService.load3partUser(jdbcTemplate, user);
        assertNotNull(response);
        assertEquals("SESSIONID123", response.getMessage());
        assertEquals(0, response.getCode());
    }
    @Test
    public void TestLoad3PartUserSuccess_2() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM USER_COMPANY WHERE EMAIL = ?"),
                any(RowMapper.class), eq(user.getEmail())))
                .thenThrow(new EmptyResultDataAccessException(1));
        when(jdbcTemplate.queryForObject(eq("SELECT CREATE_TEMP_USER(?,?,?,?,?)"),
                eq(Integer.class), any(), any(), any(), any(), any()))
                .thenReturn(0);
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM CONFIRM_USER(?, ?)"),
                eq(Integer.class), any(), any()))
                .thenReturn(0);

        String hashedPwd = new BCryptPasswordEncoder().encode(user.getPwd());
        when(jdbcTemplate.queryForObject(eq("SELECT PWD FROM USERS WHERE email = ?"),
                eq(String.class), any())).thenReturn(hashedPwd);
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM LOGIN(?, ?)"),
                any(RowMapper.class),
                eq(user.getEmail()),
                anyString()))
                .thenReturn(new CodeResponse(0, "SESSIONID123"));
        CodeResponse response = userService.load3partUser(jdbcTemplate, user);
        assertNotNull(response);
        assertEquals("SESSIONID123", response.getMessage());
        assertEquals(0, response.getCode());
    }

}
