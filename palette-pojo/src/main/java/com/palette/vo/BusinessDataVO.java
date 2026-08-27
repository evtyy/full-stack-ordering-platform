package com.palette.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data overview
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataVO implements Serializable {

    private Double turnover;//turnover

    private Integer validOrderCount;//number of valid orders

    private Double orderCompletionRate;//order completion rate

    private Double unitPrice;//average order value

    private Integer newUsers;//number of new users

}
