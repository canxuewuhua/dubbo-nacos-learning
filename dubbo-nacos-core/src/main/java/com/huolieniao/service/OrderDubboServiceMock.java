package com.huolieniao.service;

public class OrderDubboServiceMock implements OrderDubboService{

    /**
     *
     * api层 写本地伪装
     * 本地伪装
     */
    @Override
    public String orderCreate(String openId) {
        // 你可以伪造容错数据，此方法只在出现RpcException时被执行
        System.out.println("这里是本地 伪造容错数据");
        return "返回本地伪造容错数据";
    }
}
