package com.huolieniao.handler;

import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.springframework.core.NamedThreadLocal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalHandler {

//    public static ThreadLocal<String> requestIdThreadLocal = new NamedThreadLocal<>("requestId");

    // https://dhexx.cn/news/show-3619590.html?action=onClick
    public static void setRequestId(){
        String requestId = UUID.randomUUID().toString();
//        requestIdThreadLocal.set(requestId);
        System.out.println("requestId: "+requestId);
        // 同时将requestID也放到dubbo的上下文中
        Map<String, String> map = new HashMap<>();
        map.put("requestId", requestId);
        RpcContext.getContext().setAttachments(map);
        // 此时gateway模块（dubbo调用者在打印日志时（无论是配置的AOP 还是嵌入在代码里的日志），都可以直接从ThreadLocal中获取requestId
        // gateway模块（dubbo调用者）已经把requestId放到dubbo的Context中了
        // 接下来就需要在business模块（dubbo提供者）从Context中获取requestId 怎么获取呢？ 用Dubbo的Filter来获取
    }
}
