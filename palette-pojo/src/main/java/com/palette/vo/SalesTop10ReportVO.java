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
public class SalesTop10ReportVO implements Serializable {

    //item name list, comma-separated, e.g. Yuxiang Shredded Pork,Kung Pao Chicken,Boiled Fish
    private String nameList;

    //sales volume list, comma-separated, e.g. 260,215,200
    private String numberList;

}
