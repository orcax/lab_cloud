package com.prj.security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prj.exception.AppException;
import com.prj.util.DataWrapper;

@ControllerAdvice
public class AppContrllerAdvise {

	@ExceptionHandler(AppException.class)
	public void handleForbiddenException(HttpServletResponse response,
			AppException e) throws IOException {
		response.setStatus(e.getStatusCode());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(
				new DataWrapper(e.getErrorCode(), e.getMessage()).toString());
	}
}
