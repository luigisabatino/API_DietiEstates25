package com.api.dietiestates25.model.extention;

import com.api.dietiestates25.model.CompanyModel;
import com.api.dietiestates25.model.UserModel;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class InsertCompanyRequest extends CompanyModel {
    private UserModel manager;
}
