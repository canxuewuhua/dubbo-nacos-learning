package com.huolieniao.controller;

import com.huolieniao.service.DeliverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliverController {

    @Autowired
    private DeliverService deliverService;

    @GetMapping("order/create")
    public String orderGenerate(String openId){
        System.out.println("orderGenerate");
        return deliverService.orderCreateByBelow(openId);
    }
}
