package com.flatlogic.app.generator.security;

import com.flatlogic.app.generator.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserDetailsAspect {

    @Autowired
    private UserCache userCache;

    @AfterReturning(pointcut = "execution(* com.flatlogic.app.generator.service.UserService.updateUser(..))", returning = "retVal")
    public void removeUserFromCache(final JoinPoint joinPoint, final Object retVal) {
        User user = (User) retVal;
        userCache.removeUserFromCache(user.getEmail());
    }

}
