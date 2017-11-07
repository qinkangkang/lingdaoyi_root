package com.lingdaoyi.cloud.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class ControllerAOP {
	
	@Around("execution (* com.lingdaoyi.cloud.service..*.*(..))")
	public Object testAop(ProceedingJoinPoint pro) throws Throwable {
        //获取request请求提(需要时备用)
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //进入方法前执行的代码
        System.out.println("beginAround");
        //执行调用的方法
        Object proceed = pro.proceed();
        //方法执行完成后执行的方法
        System.out.println("endAround");
        return proceed;
    }

}
