package com.palette.aspect;

import com.palette.annotation.AutoFill;
import com.palette.constant.AutoFillConstant;
import com.palette.context.BaseContext;
import com.palette.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Aspect class that automatically fills common fields
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //Pointcut: intercept all mapper methods annotated with @AutoFill
    @Pointcut("execution(* com.palette.mapper.*.*(..)) && @annotation(com.palette.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * Before advice that populates common fields
     * @param{JoinPoint} joinPoint the intercepted method
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("Starting automatic common field population...");

        //Get operation type (INSERT/UPDATE)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // Get method signature
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // Get the @AutoFill annotation
        OperationType operationType = autoFill.value(); // Get the database operation type
        
        //Get the intercepted method arguments (entity object)
        Object[] args = joinPoint.getArgs(); // Get all method arguments
        if (args == null || args.length == 0) {
            return;
        }
        
        Object entity = args[0]; // Get the 1st entity
        
        //Prepare values to assign
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        
        //Assign values to common fields via reflection based on operation type
        if (operationType == OperationType.INSERT) {
            //Set all 4 common fields
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //Invoke setters via reflection
            setCreateTime.invoke(entity, now);
            setCreateUser.invoke(entity, currentId);
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, currentId);
        } else if (operationType == OperationType.UPDATE) {
            //Set 2 common fields
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //Invoke setters via reflection
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, currentId);
        }
        
    }
}
