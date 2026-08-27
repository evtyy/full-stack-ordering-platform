package com.palette.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "palette.jwt")
@Data
public class JwtProperties {

    /**
     * Configuration for generating jwt tokens for admin-side employees
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * Configuration for generating jwt tokens for end users
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
