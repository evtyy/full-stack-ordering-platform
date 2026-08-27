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
 * JWT token authentication interceptor
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * validate JWT token before accessing controller methods
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //Check whether intercepted resource is a Controller method
        if (!(handler instanceof HandlerMethod)) {
            //allow access to non-controller resources directly
            return true;
        }

        //1. retrieve JWT token from request header
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2. validate JWT token
        try {
            log.info("Validating JWT token: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            //extract the empId from JWT claims
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("Current employee ID：{}", empId);
            //store empId in ThreadLocal for current request
            BaseContext.setCurrentId(empId);

            //allow request to proceed
            return true;
        } catch (Exception ex) {
            //4. JWT validation failed, return HTTP 401 Unauthorized
            response.setStatus(401);
            return false;
        }
    }
}
