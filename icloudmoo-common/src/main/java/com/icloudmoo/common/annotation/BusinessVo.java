package com.icloudmoo.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO:作为解释VO的注释
 * @author guguihe
 * @Date 2015年10月19日 上午8:57:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessVo {

    /**
     * TODO: 需要转换的PO属性名
     * 
     * @return
     */
    String[] vos() default {};

    /**
     * TODO: 不转换的PO属性名
     * 
     * @return
     */
    String[] igs() default {};

    /**
     * TODO: 日期格式化
     * 
     * @return
     */
    String format() default "yyyy-MM-dd HH:mm:ss";
}
