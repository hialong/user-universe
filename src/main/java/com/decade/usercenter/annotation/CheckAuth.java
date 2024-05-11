package com.decade.usercenter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hailong
 * ‘@target’ ElementType.METHOD 表示这个用在方法上面
 * ‘@Retention’(RetentionPolicy.RUNTIME) 表示这个注解在运行时生效
 * 下面是他的枚举类
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuth {
    String mustRole() default "";
}
