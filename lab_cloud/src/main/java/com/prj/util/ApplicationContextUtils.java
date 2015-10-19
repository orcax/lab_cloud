package com.prj.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ApplicationContextUtils {
	private static WebApplicationContext webApplication = ContextLoader.getCurrentWebApplicationContext();

	public static WebApplicationContext getContext() {
		return webApplication;
	}
	
	public static Object getBean(String beanName) {
		return webApplication.getBean(beanName);
	}
}
