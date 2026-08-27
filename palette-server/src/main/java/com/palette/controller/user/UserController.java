package com.palette.controller.user;

import com.palette.constant.JwtClaimsConstant;
import com.palette.dto.WebUserLoginDTO;
import com.palette.entity.User;
import com.palette.properties.JwtProperties;
import com.palette.result.Result;
import com.palette.service.UserService;
import com.palette.utils.JwtUtil;
import com.palette.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "Customer-facing user API")
@RequestMapping("/user/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/webLogin")
    @ApiOperation("Customer web app login (phone + name)")
    public Result<UserLoginVO> webLogin(@RequestBody WebUserLoginDTO webUserLoginDTO) {
        User user = userService.webLogin(webUserLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO build = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        return Result.success(build);
    }
}
