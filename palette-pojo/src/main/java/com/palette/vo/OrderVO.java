package com.palette.vo;

import com.palette.entity.OrderDetail;
import com.palette.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    //order dish information
    private String orderDishes;

    //order details
    private List<OrderDetail> orderDetailList;

}
