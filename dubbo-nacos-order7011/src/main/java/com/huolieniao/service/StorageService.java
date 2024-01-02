//package com.huolieniao.service;
//
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.stereotype.Component;
//
///**
// * 针对一个方法orderCreate 不同的实现 通过group进行区分
// */
//@Component
//@DubboService(version = "1.0.0",group = "storage-order")
//public class StorageService implements OrderDubboService{
//    @Override
//    public String orderCreate(String openId) {
//        System.out.println("orderCreate 下单 库存增减成功");
//        return "下订单 库存增减成功!! openId: " + openId;
//    }
//}
