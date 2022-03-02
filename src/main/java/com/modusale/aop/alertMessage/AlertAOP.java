package com.modusale.aop.alertMessage;

import com.modusale.utils.TelegramAPI;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AlertAOP {
    private final TelegramAPI telegramAPI;
    public AlertAOP(TelegramAPI telegramAPI){
        this.telegramAPI=telegramAPI;
    }
    @Around("@annotation(com.modusale.aop.alertMessage.Alert)")
    private Object retry(ProceedingJoinPoint proceedingJoinPoint){
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
//            System.out.println(proceedingJoinPoint.getTarget().toString());
            telegramAPI.send(proceedingJoinPoint.getTarget().toString());
        }
        return null;
    }
}
