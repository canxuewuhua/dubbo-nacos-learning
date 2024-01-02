//package com.huolieniao.service;
//
//public class OrderDubboServiceStub implements OrderDubboService{
//
//    private final OrderDubboService orderDubboService;
//
//    /**
//     * api层 写本地存根
//     * 构造函数传入真正的远程代理对象
//     */
//    public OrderDubboServiceStub(OrderDubboService orderDubboService){
//        this.orderDubboService = orderDubboService;
//    }
//    @Override
//    public String orderCreate(String openId) {
//        if (openId.contains("token")){
//            return orderDubboService.orderCreate(openId);
//        }
//        return "本地存根 校验参数 openId中不含 token 返回失败！！";
//    }
//}
