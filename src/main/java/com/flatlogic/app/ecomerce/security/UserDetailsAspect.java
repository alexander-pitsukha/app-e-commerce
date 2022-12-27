package com.flatlogic.app.ecomerce.security;

import com.flatlogic.app.ecomerce.entity.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserDetailsAspect {

    private final UserCache userCache;

    @AfterReturning(pointcut = "execution(* com.flatlogic.app.ecomerce.service.UserService.updateUser(..))", returning = "retVal")
    public void removeUserFromCache(final JoinPoint joinPoint, final Object retVal) {
        User user = (User) retVal;
        userCache.removeUserFromCache(user.getEmail());
    }

}
