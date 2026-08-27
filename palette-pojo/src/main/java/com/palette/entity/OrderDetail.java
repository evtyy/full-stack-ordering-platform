package com.palette.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Order detail
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //name
    private String name;

    //order id
    private Long orderId;

    //dish id
    private Long dishId;

    //setmeal id
    private Long setmealId;

    //flavor
    private String dishFlavor;

    //quantity
    private Integer number;

    //amount
    private BigDecimal amount;

    //image
    private String image;
}
