package com.api.dietiestates25.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserDTO {
    private String email;
    private String pwd;
    private String firstName;
    private String lastName;
    private String otp;
}
