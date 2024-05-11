package com.decade.usercenter.aop;

import com.decade.usercenter.annotation.CheckAuth;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.constant.UserConstant;
import com.decade.usercenter.enums.UserRoleEnum;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.exception.ThrowUtils;
import com.decade.usercenter.model.domain.User;
import com.decade.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class AuthIntercepter {

    @Resource
    UserService userService;

    /**
     * 权限校验拦截
     *
     * @param joinPoint 连接点的实例
     * @param checkAuth 权限注解
     */
    @Around("@annotation(checkAuth)")
    public void checkAuthInterceptor(ProceedingJoinPoint joinPoint, CheckAuth checkAuth) throws Throwable {
        // 注解中的必须用户
        String mustRole = checkAuth.mustRole();
        log.info("需要权限{}", mustRole);
        // 通过上下问获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User currentUser = userService.getCurrentUser(request);
        //权限校验
        if(StringUtils.isNotBlank(mustRole)){
            //如果是超级管理员，直接放行
            if(UserConstant.SUPER_ADMIN_ROLE.equals(currentUser.getUserRole().toString())){
                joinPoint.proceed();
                return;
            }
            // 如果不是超级管理员才能操作，则判断是否是管理员,管理员也直接放行
            if(!mustRole.equals(UserConstant.SUPER_ADMIN_ROLE)&&UserConstant.ADMIN_ROLE.equals(currentUser.getUserRole().toString())){
                joinPoint.proceed();
                return;
            }

            // 如果配置中就没有这个权限，那怎么写都不会有权限操作
            UserRoleEnum enumByValue = UserRoleEnum.getEnumByValue(mustRole);
            ThrowUtils.throwIf(enumByValue == null, ErrorCode.NO_AUTH);

            // 如果用户被封号，直接拒绝
            Integer userStatus = currentUser.getUserStatus();
            ThrowUtils.throwIf(userStatus.toString().equals(UserConstant.BAN), ErrorCode.NO_AUTH);
            log.info("当前用户权限为：{}", currentUser.getUserRole());
            //如果权限相同，则直接放行
            if(currentUser.getUserRole().toString().equals(mustRole)) {
                joinPoint.proceed();
                return;
            }
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
    }
}
