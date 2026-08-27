package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SetmealPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    //category id
    private Integer categoryId;

    //status: 0 disabled, 1 enabled
    private Integer status;

}
