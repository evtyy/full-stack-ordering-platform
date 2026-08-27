package com.palette.config;

import com.palette.interceptor.JwtTokenAdminInterceptor;
import com.palette.interceptor.JwtTokenUserInterceptor;
import com.palette.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Web MVC configuration class
 * Registers web-related components such as interceptors,
 * static resource handlers, message converters, and API documentation.
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * Registers custom interceptors for admin and user requests.
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering custom interceptors...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/webLogin")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * Allows the customer-web dev server to call the /user APIs from a different origin.
     *
     * @param registry
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/user/**")
                .allowedOriginPatterns("http://localhost:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * Configures Swagger/Knife4j documentation for admin APIs.
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket docket() {
        log.info("Generating API documentation...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Takeout API Documentation")
                .version("1.0")
                .description("RESTful API documentation for food delivery platform")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin Side APIs")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.palette.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * Configures Swagger/Knife4j documentation for user APIs.
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket docket1() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Takeout API Documentation")
                .version("1.0")
                .description("RESTful API documentation for food delivery platform")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("User Side APIs")
                .apiInfo(apiInfo)
                .select()
                //scans com.palette.controller.user package
                .apis(RequestHandlerSelectors.basePackage("com.palette.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * Configures mappings for static resources,
     * like Swagger/Knife4j documentation page.
     *
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Configuring static resource handlers...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Extends Spring MVC's message converters by adding a custom Jackson converter.
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Extend Spring MVC message converters...");
        //create a Jackson-based HTTP message converter
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //use custom ObjectMapper for JSON serialization/deserialization
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //add custom converter with the highest priority
        converters.add(0, messageConverter);
   }
}
