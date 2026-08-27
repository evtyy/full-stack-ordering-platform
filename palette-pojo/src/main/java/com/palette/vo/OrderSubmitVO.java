package com.palette.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVO implements Serializable {
    //order id
    private Long id;
    //order number
    private String orderNumber;
    //order amount
    private BigDecimal orderAmount;
    //order time
    private LocalDateTime orderTime;
}
