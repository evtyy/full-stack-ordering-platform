package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    private Long id;
    //order status: 1 pending payment, 2 pending confirmation, 3 confirmed, 4 out for delivery, 5 completed, 6 cancelled, 7 refunded
    private Integer status;

}
