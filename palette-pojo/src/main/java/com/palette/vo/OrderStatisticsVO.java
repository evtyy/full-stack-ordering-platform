package com.palette.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatisticsVO implements Serializable {
    //number pending confirmation
    private Integer toBeConfirmed;

    //number pending delivery
    private Integer confirmed;

    //number out for delivery
    private Integer deliveryInProgress;
}
