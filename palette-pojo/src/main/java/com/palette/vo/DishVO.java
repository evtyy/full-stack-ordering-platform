package com.palette.vo;

import com.palette.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishVO implements Serializable {

    private Long id;
    //Dish name
    private String name;
    //Dish category id
    private Long categoryId;
    //Dish price
    private BigDecimal price;
    //Dish image
    private String image;
    //Dish description
    private String description;
    //0 discontinued 1 on sale
    private Integer status;
    //Update time
    private LocalDateTime updateTime;
    //Category name
    private String categoryName;
    //Dish flavors
    private List<DishFlavor> flavors = new ArrayList<>();
}
