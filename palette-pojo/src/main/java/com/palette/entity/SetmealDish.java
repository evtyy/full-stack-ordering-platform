package com.palette.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Setmeal-dish relation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //setmeal id
    private Long setmealId;

    //dish id
    private Long dishId;

    //dish name (redundant field)
    private String name;

    //original dish price
    private BigDecimal price;

    //number of portions
    private Integer copies;
}
