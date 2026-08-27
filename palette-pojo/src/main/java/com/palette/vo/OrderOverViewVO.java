package com.palette.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Order overview data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {
    //number pending confirmation
    private Integer waitingOrders;

    //number pending delivery
    private Integer deliveredOrders;

    //number completed
    private Integer completedOrders;

    //number cancelled
    private Integer cancelledOrders;

    //all orders
    private Integer allOrders;
}
