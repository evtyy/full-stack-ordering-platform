package com.palette.dto;

import com.palette.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDTO implements Serializable {

    private Long id;

    //category id
    private Long categoryId;

    //setmeal name
    private String name;

    //setmeal price
    private BigDecimal price;

    //status: 0 disabled, 1 enabled
    private Integer status;

    //description
    private String description;

    //image
    private String image;

    //setmeal-dish relations
    private List<SetmealDish> setmealDishes = new ArrayList<>();

}
