package com.palette.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Address book
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //user id
    private Long userId;

    //consignee
    private String consignee;

    //phone number
    private String phone;

    //gender: 0 female, 1 male
    private String sex;

    //province code
    private String provinceCode;

    //province name
    private String provinceName;

    //city code
    private String cityCode;

    //city name
    private String cityName;

    //district code
    private String districtCode;

    //district name
    private String districtName;

    //detailed address
    private String detail;

    //label
    private String label;

    //whether it's the default: 0 no, 1 yes
    private Integer isDefault;
}
