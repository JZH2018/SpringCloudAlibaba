package com.microautumn.springcloud.controller;

import com.microautumn.springcloud.service.Impl.PaymentServiceImpl;
import com.microautumn.springcloud.service.PaymentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverport;

    @GetMapping(value = "/payment/hystrix/ok/{id}")
    public String paymentInfo_ok(@PathVariable("id") Integer id){

        String result = paymentService.paymentInfo_ok(id);
        log.info("******* cloud-provider-hystrix-payment8001 result"+result);
        return result;
    }

    @GetMapping(value = "/payment/hystrix/timeout/{id}")
    public String paymentInfo_timeout(@PathVariable("id") Integer id){

        String result = paymentService.paymentInfo_timeout(id);
        log.info("******* cloud-provider-hystrix-payment8001 result"+result);
        return result;
    }

    //==========服务熔断
    @GetMapping(value = "/payment/Circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        String result = paymentService.paymentCircuitBreaker(id);
        log.info("******* cloud-provider-hystrix-payment8001 result"+result);
        return result;
    }



}
