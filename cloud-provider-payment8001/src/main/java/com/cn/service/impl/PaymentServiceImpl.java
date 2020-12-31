package com.cn.service.impl;

import com.cn.dao.PaymentDao;
import com.cn.entities.Payment;
import com.cn.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {

 @Resource
 private PaymentDao paymentDao;

 @Override
 public int create(Payment payment) {
  return this.paymentDao.create(payment);
 }

 @Override
 public Payment getPaymentById(long id) {
  return this.paymentDao.getPaymentById(id);
 }
}
