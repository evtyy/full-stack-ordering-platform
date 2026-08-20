package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Customer web app login (find-or-create by phone number, no password)
 */
@Data
public class WebUserLoginDTO implements Serializable {

    private String name;

    private String phone;

}
