package com.prj.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CopyRequired {
	boolean create() default true;
	boolean update() default true;
	boolean copyWhenNull() default false;
}
