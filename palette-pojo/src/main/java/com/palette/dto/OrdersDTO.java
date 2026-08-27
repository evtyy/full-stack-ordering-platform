package com.palette.dto;

import com.palette.entity.OrderDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdersDTO implements Serializable {

    private Long id;

    //order number
    private String number;

    //order status: 1 pending payment, 2 pending delivery, 3 out for delivery, 4 completed, 5 cancelled
    private Integer status;

    //id of the user who placed the order
    private Long userId;

    //address id
    private Long addressBookId;

    //order time
    private LocalDateTime orderTime;

    //checkout time
    private LocalDateTime checkoutTime;

    //payment method (currently unused, payment is only made via Stripe)
    private Integer payMethod;

    //amount actually received
    private BigDecimal amount;

    //remark
    private String remark;

    //username
    private String userName;

    //phone number
    private String phone;

    //address
    private String address;

    //consignee
    private String consignee;

    private List<OrderDetail> orderDetails;

}
