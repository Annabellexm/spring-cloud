package com.cn.controller;

import com.cn.entities.CommonResult;
import com.cn.entities.Payment;
import com.cn.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

 private static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";

 @Resource
 private RestTemplate restTemplate;

 @Resource
 private LoadBalancer loadBalancer;

 @Resource
 private DiscoveryClient discoveryClient;

 @GetMapping("/consumer/payment/create")
 public CommonResult<Payment> create(Payment payment){
  return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment, CommonResult.class);  //写操作
 }

 @GetMapping("/consumer/payment/get/{id}")
 public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
 return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id, CommonResult.class);

  //return restTemplate.getForEntity(PAYMENT_URL+"/payment/get/"+id, CommonResult.class).getBody();
 }

 @GetMapping("/consumer/payment/getForEntity/{id}")
 public CommonResult<Payment> getPayment2(@PathVariable("id")Long id){
  ResponseEntity<CommonResult>entity=restTemplate.getForEntity(PAYMENT_URL+"/payment/get/"+id, CommonResult.class);

  if(entity.getStatusCode().is2xxSuccessful()){
   return entity.getBody();
  }else{
   return new CommonResult<Payment>(444,"操作失败");
  }

 }

 @GetMapping("/consumer/payment/lb")
 public String getPaymentLB(){
  List<ServiceInstance> instances=discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
  if(instances==null||instances.size()<=0){
   return null;
  }
  ServiceInstance serviceInstance=loadBalancer.instance(instances);
  URI uri=serviceInstance.getUri();
  return restTemplate.getForObject(uri+"/payment/lb",String.class);
 }





}
