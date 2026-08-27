package com.palette.interceptor;

import com.palette.constant.JwtClaimsConstant;
import com.palette.context.BaseContext;
import com.palette.properties.JwtProperties;
import com.palette.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor for JWT token validation
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Validate the JWT
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Determine whether the current intercepted target is a Controller method or another resource
        if (!(handler instanceof HandlerMethod)) {
            //The current intercepted target is not a dynamic method, let it pass through
            return true;
        }

        //1. Get the token from the request header
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2. Validate the token
        try {
            log.info("JWT validation: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("Current user id: {}", userId);
            //3. Passed, let it through

            // Store the currently logged-in employee id
            BaseContext.setCurrentId(userId);

            return true;
        } catch (Exception ex) {
            //4. Failed, respond with status code 401
            response.setStatus(401);
            return false;
        }
    }
}
