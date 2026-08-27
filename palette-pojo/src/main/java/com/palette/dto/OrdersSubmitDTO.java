package com.palette.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {
    //address book id
    private Long addressBookId;
    //payment method
    private int payMethod;
    //remark
    private String remark;
    //estimated delivery time
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;
    //delivery status: 1 deliver immediately, 0 choose a specific time
    private Integer deliveryStatus;
    //number of utensils
    private Integer tablewareNumber;
    //utensil quantity status: 1 provide based on meal count, 0 choose a specific quantity
    private Integer tablewareStatus;
    //packaging fee
    private Integer packAmount;
    //total amount
    private BigDecimal amount;
}
