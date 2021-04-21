package com.microautumn.springcloud.controller;

import com.microautumn.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_ok(@PathVariable("id") Integer id){
        String s = paymentHystrixService.paymentInfo_ok(id);
        return s;
    }

//    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
//    public String paymentInfo_timeout(@PathVariable("id") Integer id){
//        String s = paymentHystrixService.paymentInfo_timeout(id);
//        return s;
//    }

    /**
     * 调用hystrix 之后   服务提供方和服务消费房均可以使用hystrix
     *
     *
     * 这里服务消费方调用服务只等待1.5秒
     * 但是服务提供方需要3秒
     * 自动进入paymentInfo_TimeOutHandler
     * @param id
     * @return
     */
    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "3500")
    })
    public String paymentInfo_timeout(@PathVariable("id") Integer id){
        String s = paymentHystrixService.paymentInfo_timeout(id);
        return s ;
    }

    public String paymentInfo_TimeOutHandler(Integer id)
    {
        return " 消费者80，对方支付繁忙，请10秒钟后重试 /(ㄒoㄒ)/~~";
    }

    public String payment_Global_FallbackMethod(){
        return "Global 异常处理信息，请稍后再试≧ ﹏ ≦";
    }


}
