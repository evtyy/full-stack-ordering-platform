package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersPaymentDTO implements Serializable {
    //order number
    private String orderNumber;

    //payment method
    private Integer payMethod;

}
