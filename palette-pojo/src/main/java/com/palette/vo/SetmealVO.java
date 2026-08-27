package com.palette.vo;

import com.palette.entity.SetmealDish;
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
public class SetmealVO implements Serializable {

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

    //update time
    private LocalDateTime updateTime;

    //category name
    private String categoryName;

    //association between setmeal and dishes
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
