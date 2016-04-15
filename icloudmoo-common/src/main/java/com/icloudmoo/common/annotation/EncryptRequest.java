/**
 * @Title: AppRequest.java
 * @Package com.gemdale.gmap.common.controller.databind
 * @Description: TODO（用一句话描述这个文件的用途）
 * @Copyright: Copyright (c) 2015-2018 此代码属于金地物业信息管理部，在未经允许的情况下禁止复制传播
 * @Company:金地物业
 * @author 信息管理部-guguihe
 * @date 2015年9月17日 下午4:33:03
 */
package com.icloudmoo.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptRequest {
}
