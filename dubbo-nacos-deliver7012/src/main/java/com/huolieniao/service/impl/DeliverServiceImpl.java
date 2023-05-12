package com.huolieniao.service.impl;

import com.huolieniao.service.DeliverService;
import com.huolieniao.service.OrderDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * https://www.cnblogs.com/yangyongjie/p/15149379.html
 * 版本 dubbo：3.0.0，nacos：2.0.0
 */
@Service
public class DeliverServiceImpl implements DeliverService {

    /**
     * stub = "true"  本地存根 作为一些参数校验 当校验不通过时 会执行 降级的处理
     * mock = "true"  本地伪装 当提供者服务出现宕机时会执行 降级的处理
     *
     * 一般在api层写 本地存根  本地伪装
     * 本地存根是 接口名+Stub
     * 本地伪装是 接口名+Mock
     */
    @DubboReference(version = "1.0.0", group = "storage-order", stub = "true", mock = "force:return xyz")
    public OrderDubboService orderDubboService;

    /**
     * https://blog.csdn.net/begefefsef/article/details/126647045
     * mock属性
     * Dubbo 的 mock 的策略总共分为两大类：   fail策略 force策略
     * 一是当服务调用失败时，去进行 mock 调用；
     * 二是绕过服务调用，直接进行 mock 调用。
     *
     * 而具体的 mock 调用策略又分别 4 种：
     * 1、返回 mock 数据
     * 2、抛出自定义异常
     * 3、执行默认的 Mock 实现类
     * 4、执行指定的 Mock 实现类
     */

    public String orderCreateByBelow(String openId){
        System.out.println("orderCreateByBelow");
        String msg = orderDubboService.orderCreate(openId);
        return msg;
    }
}
