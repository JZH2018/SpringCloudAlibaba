package com.microautumn.springcloud.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.microautumn.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.concurrent.TimeUnit;

@Service
public class PaymentServiceImpl implements PaymentService {

    /**
     * 正常访问
     * @param id
     * @return
     */
    @Override
    public String paymentInfo_ok(Integer id) {
        return "线程池  :" +Thread.currentThread().getName() + " paymentInfo_ok,id :" + id;
    }

    /**
     * hystrix可以用于服务提供方  也可以用于服务消费方
     *
     * 以下用于自身  服务最多可以等待5秒
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String paymentInfo_timeout(Integer id) {
        int timeNumber =3;
        //暂停几秒钟
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池  :" +Thread.currentThread().getName() + " paymentInfo_timeout,id :" + id +"耗时"+timeNumber+"秒";
    }

    public String paymentInfo_TimeOutHandler(Integer id)
    {
        return "线程池  :" +Thread.currentThread().getName() + " paymentInfo_timeout,id :" + id + " /(ㄒoㄒ)/~~";
    }


    //=========================服务熔断

    @HystrixCommand(
            fallbackMethod = "paymentCircuitBreader_fallback",commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),   //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),  //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),//失败率到达多少后跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
     if(id<0){
         throw new RuntimeException("*****id  不能负数");
     }
        String s = IdUtil.simpleUUID();
     return Thread.currentThread().getName() + "调用成功,流水号：" +s;
    }

    public String paymentCircuitBreader_fallback(@PathVariable("id") Integer id){
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~"+id;
    }


}
