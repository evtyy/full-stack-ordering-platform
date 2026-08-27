package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordEditDTO implements Serializable {

    //employee id
    private Long empId;

    //old password
    private String oldPassword;

    //new password
    private String newPassword;

}
