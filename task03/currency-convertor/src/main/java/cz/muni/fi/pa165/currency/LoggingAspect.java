package cz.muni.fi.pa165.currency;

import javax.inject.Named;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Named
@Aspect
public class LoggingAspect {

    @Around("execution(public * *(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {

        System.err.println("Calling method: "
                + joinPoint.getSignature());
        long start = System.nanoTime();

        Object result = joinPoint.proceed();

        System.err.println("Method finished: "
                + joinPoint.getSignature());
        long elapsed = (System.nanoTime() - start)/1000;

        System.out.println("Method execution time: " + elapsed + " microseconds.");

        return result;
    }

}