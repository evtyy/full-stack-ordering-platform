package com.palette.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //WeChat user unique identifier
    private String openid;

    //name
    private String name;

    //phone number
    private String phone;

    //gender: 0 female, 1 male
    private String sex;

    //ID card number
    private String idNumber;

    //avatar
    private String avatar;

    //registration time
    private LocalDateTime createTime;
}
