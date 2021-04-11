package com.microautumn.springcloud.service;

import org.springframework.stereotype.Component;


/**
 * 当CLOUD-PROVIDER-HYSTRIX-PAYMENT被调用  且失败时调用以下方法
 */

@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String paymentInfo_ok(Integer id) {
        return "********* PaymentFallbackService ,/(ㄒoㄒ)/~~";
    }

    @Override
    public String paymentInfo_timeout(Integer id) {
        return "********* PaymentFallbackService ,/(ㄒoㄒ)/~~";
    }
}
