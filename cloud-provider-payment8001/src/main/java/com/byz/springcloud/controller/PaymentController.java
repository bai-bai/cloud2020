package com.byz.springcloud.controller;

import com.byz.springcloud.entities.CommonResult;
import com.byz.springcloud.entities.Payment;
import com.byz.springcloud.serivce.PaymentService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

//    @Resource
//    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("result:{}",result);

        if(result > 0){
            return new CommonResult(200,"插入数据成功,serverPort："+serverPort,result);
        }else{
            return new CommonResult(444,"插入数据失败",null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("result:{}",payment);
        if(null != payment){
            return new CommonResult(200,"查询成功serverPort：" + serverPort,payment);
        }else{
            return new CommonResult(444,"没有对应记录，查询ID:{}"+id,null);
        }
    }

//    @GetMapping(value = "/payment/discovery")
//    public Object discovery(){
//        List<String> services = discoveryClient.getServices();
//        for (String element:services) {
//            log.info("******elements>>>>>>>{}",element);
//        }
//
//        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
//        for (ServiceInstance instance: instances) {
//            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
//        }
//
//        return this.discoveryClient;
//    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try { TimeUnit.SECONDS.sleep(3); }catch (Exception e) {e.printStackTrace();}
        return serverPort;
    }

}
