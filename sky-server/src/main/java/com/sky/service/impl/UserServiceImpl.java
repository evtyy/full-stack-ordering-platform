package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.WebUserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //Wechat API endpoint
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * Wechat login
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //Check whether openid is null; if login succeeds it won't be, o/w throw exception
        String openid = getOpenId(userLoginDTO.getCode());
        log.info("openid: {}", openid);
        log.info("UserLoginDto {}", userLoginDTO);
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //Check if new user
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            //Auto-register new users
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        //Return user obj
        return user;
    }

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

    private String getOpenId(String code) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("appid", weChatProperties.getAppid());
        reqParams.put("secret", weChatProperties.getSecret());
        reqParams.put("js_code", code);
        reqParams.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, reqParams);
        log.info("JSON returned by Wechat API: {}", json);

        JSONObject parseJson = JSON.parseObject(json);
        return parseJson.getString("openid");
    }
}
