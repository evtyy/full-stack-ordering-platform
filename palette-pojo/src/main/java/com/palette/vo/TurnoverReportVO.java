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
public class TurnoverReportVO implements Serializable {

    //dates, comma-separated, e.g. 2022-10-01,2022-10-02,2022-10-03
    private String dateList;

    //turnover, comma-separated, e.g. 406.0,1520.0,75.0
    private String turnoverList;

}
