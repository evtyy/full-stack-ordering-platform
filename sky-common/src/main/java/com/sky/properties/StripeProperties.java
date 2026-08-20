package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.stripe")
@Data
public class StripeProperties {

    private String secretKey; //Stripe API secret key (test mode)
    private String successUrl; //where Stripe redirects after successful checkout
    private String cancelUrl; //where Stripe redirects if customer cancels checkout

}
