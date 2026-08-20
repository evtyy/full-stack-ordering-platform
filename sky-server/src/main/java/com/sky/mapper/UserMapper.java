package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * Find a user by openid
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * Find a user by phone number
     * @param phone
     * @return
     */
    @Select("select * from user where phone = #{phone}")
    User getByPhone(String phone);

    /**
     * Insert new record
     * @param user
     */
    void insert(User user);

    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * Count users matching dynamic conditions
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
