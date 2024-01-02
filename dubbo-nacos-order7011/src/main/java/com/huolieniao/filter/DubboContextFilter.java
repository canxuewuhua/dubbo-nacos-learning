package com.huolieniao.filter;

import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;
import java.util.Map;

public class DubboContextFilter implements Filter {

//    public static ThreadLocal<String> requestIdThreadLocal = new NamedThreadLocal<String>("requestId");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        MDC.put("","");

        Map<String, String> context = RpcContext.getContext().getAttachments();
        String requestId = context.get("requestId");
        System.out.println("requestId: " + requestId);
        return invoker.invoke(invocation);

    }
}
