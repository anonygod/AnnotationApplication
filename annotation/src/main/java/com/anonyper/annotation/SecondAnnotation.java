package com.anonyper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建注解 应用于类、方法、变量
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE_USE)//API 26上可以使用
//@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface SecondAnnotation {

}
