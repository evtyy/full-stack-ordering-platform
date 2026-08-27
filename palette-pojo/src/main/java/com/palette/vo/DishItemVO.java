package com.palette.vo;

import com.palette.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {

    //Dish id
    private Long dishId;

    //Dish name
    private String name;

    //Copies
    private Integer copies;

    //Dish image
    private String image;

    //Dish description
    private String description;

    //Dish flavors
    private List<DishFlavor> flavors;
}
