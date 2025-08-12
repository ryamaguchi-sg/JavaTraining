package com.s_giken.traning.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {
    @Bean
    public BillingProcessor billingProcessor() {
        return new BillingProcessor();
    }
}
