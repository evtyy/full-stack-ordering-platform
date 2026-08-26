package com.sky.service;

import com.sky.dto.WebUserLoginDTO;
import com.sky.entity.User;

public interface UserService {

    /**
     * Customer web app login: find the user by phone number, or create one if none exists
     * @param webUserLoginDTO
     * @return
     */
    User webLogin(WebUserLoginDTO webUserLoginDTO);
}
