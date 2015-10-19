package com.prj.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;

@Order(1)
public class SecurityWebApplicationInitializer extends
		AbstractSecurityWebApplicationInitializer implements  WebApplicationInitializer{

}
