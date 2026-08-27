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
public class OrderReportVO implements Serializable {

    //dates, comma-separated, e.g. 2022-10-01,2022-10-02,2022-10-03
    private String dateList;

    //daily order counts, comma-separated, e.g. 260,210,215
    private String orderCountList;

    //daily valid order counts, comma-separated, e.g. 20,21,10
    private String validOrderCountList;

    //total order count
    private Integer totalOrderCount;

    //valid order count
    private Integer validOrderCount;

    //order completion rate
    private Double orderCompletionRate;

}
