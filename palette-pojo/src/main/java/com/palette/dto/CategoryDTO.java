package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {

    //primary key
    private Long id;

    //type: 1 dish category, 2 setmeal category
    private Integer type;

    //category name
    private String name;

    //sort order
    private Integer sort;

}
