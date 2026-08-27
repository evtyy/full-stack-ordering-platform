package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersCancelDTO implements Serializable {

    private Long id;
    //order cancellation reason
    private String cancelReason;

}
