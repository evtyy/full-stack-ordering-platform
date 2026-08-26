package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.WebUserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * Customer web app login: find the user by phone number, or create one if none exists
     *
     * @param webUserLoginDTO
     * @return
     */
    @Override
    public User webLogin(WebUserLoginDTO webUserLoginDTO) {
        String phone = webUserLoginDTO.getPhone();
        if (phone == null || phone.isEmpty()) {
            throw new LoginFailedException(MessageConstant.PHONE_REQUIRED);
        }

        User user = userMapper.getByPhone(phone);
        if (user == null) {
            user = User.builder()
                    .name(webUserLoginDTO.getName())
                    .phone(phone)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }
}
