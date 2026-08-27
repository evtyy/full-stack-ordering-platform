package com.palette.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //type: 1 dish category, 2 setmeal category
    private Integer type;

    //category name
    private String name;

    //sort order
    private Integer sort;

    //category status: 0 disabled, 1 enabled
    private Integer status;

    //creation time
    private LocalDateTime createTime;

    //update time
    private LocalDateTime updateTime;

    //creator
    private Long createUser;

    //last modifier
    private Long updateUser;
}
