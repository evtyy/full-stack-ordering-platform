package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {

    //employee name
    private String name;

    //page number
    private int page;

    //number of records displayed per page
    private int pageSize;

}
