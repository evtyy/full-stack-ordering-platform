package com.palette.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersRejectionDTO implements Serializable {

    private Long id;

    //order rejection reason
    private String rejectionReason;

}
