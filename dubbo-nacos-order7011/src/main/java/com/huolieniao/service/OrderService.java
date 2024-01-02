package com.huolieniao.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

// @DubboService 有token属性配置 令牌验证
@Component
@DubboService(version = "1.0.0", group = "order-create", filter = "dubboContextFilter")
//@DubboService(version = "1.0.0", group = "order-create")
//@DubboService(version = "1.0.0", group = "order-create")
public class OrderService implements OrderDubboService{

    @Override
    public String orderCreate(String openId) {
        System.out.println("orderCreate 下订单成功");
        return "下订单成功!! openId: " + openId;
    }

}
