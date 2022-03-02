package com.modusale.aop.retry;

import com.modusale.utils.TelegramAPI;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryAOP {
    private final TelegramAPI telegramAPI;
    public RetryAOP(TelegramAPI telegramAPI){
        this.telegramAPI=telegramAPI;
    }
    @Around("@annotation(com.modusale.aop.retry.Retry)")
    private Object retry(ProceedingJoinPoint proceedingJoinPoint) throws InterruptedException {
        for(int i=0;i<10;) {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                Thread.sleep(1000);
                i++;
            }
        }
//        System.out.println(proceedingJoinPoint.getArgs()[0].toString());
        telegramAPI.send(proceedingJoinPoint.getArgs()[0].toString());
        return null;
    }
}
