package com.prj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.prj.security.AccountAuthorizeInterceptor;

@Configuration
@ComponentScan(basePackages = "com.prj.controller")
@EnableWebMvc
//@EnableAspectJAutoProxy(proxyTargetClass=true)
public class DispatcherConfig extends WebMvcConfigurerAdapter{
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccountAuthorizeInterceptor());
    }
	
	 @Override
	 public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		 configurer.enable();
	 }
	 
	 @Bean
	 public MultipartResolver multipartResolver() {
		 CommonsMultipartResolver mr = new CommonsMultipartResolver();
		 mr.setDefaultEncoding("utf-8");
		 mr.setMaxUploadSize(10000000L);
		 return mr;
	 }
}
