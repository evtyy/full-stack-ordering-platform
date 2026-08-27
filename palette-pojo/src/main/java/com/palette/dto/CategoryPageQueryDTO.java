package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {

    //page number
    private int page;

    //number of records per page
    private int pageSize;

    //category name
    private String name;

    //category type: 1 dish category, 2 setmeal category
    private Integer type;

}
