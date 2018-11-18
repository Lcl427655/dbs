package com.cn.dbs.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class EmployeesAspect {

    private final static Logger logger = LoggerFactory.getLogger(EmployeesAspect.class);

    //匹配web下的所有方法
    @Pointcut("execution(* com.cn.dbs.web.*.*(..))")
    public void employeesAspect(){}

    @Before("employeesAspect()")
    public void deBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容  
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容  
        logger.error("URL : " + request.getRequestURL().toString());
        logger.error("HTTP_METHOD : " + request.getMethod());
        logger.error("IP : " + request.getRemoteAddr());
        logger.error("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.error("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "employeesAspect()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容  
        logger.error("方法的返回值 : " + ret);
    }

    //后置异常通知  
    @AfterThrowing("employeesAspect()")
    public void throwss(JoinPoint jp){
        logger.error("方法异常时执行....." + jp);
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行  
    @After("employeesAspect()")
    public void after(JoinPoint jp){
        logger.error("方法最后执行....." + jp);
    }

    //环绕通知,环绕增强，相当于MethodInterceptor  
    @Around("employeesAspect()")
    public Object arround(ProceedingJoinPoint pjp) {
        logger.error("方法环绕start.....");
        try {
            Object o =  pjp.proceed();
            logger.error("方法环绕proceed，结果是 :" + o);
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
