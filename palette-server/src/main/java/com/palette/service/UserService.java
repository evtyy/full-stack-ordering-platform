package com.palette.service;

import com.palette.dto.WebUserLoginDTO;
import com.palette.entity.User;

public interface UserService {

    /**
     * Customer web app login: find the user by phone number, or create one if none exists
     * @param webUserLoginDTO
     * @return
     */
    User webLogin(WebUserLoginDTO webUserLoginDTO);
}
