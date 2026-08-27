package com.palette.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentVO implements Serializable {

    private String nonceStr; //random string
    private String paySign; //signature
    private String timeStamp; //timestamp
    private String signType; //signature algorithm
    private String packageStr; //the prepay_id parameter value returned by the unified order API

}
