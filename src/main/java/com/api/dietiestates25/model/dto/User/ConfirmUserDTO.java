package com.api.dietiestates25.model.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmUserDTO {
    private String email;
    private String pwd;
    private String temporaryPwd;
}
