package com.prj.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		// unauthenticated request not in permit list finally goes here
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(
				new DataWrapper(ErrorCode.UNAUTHORIZED,
						"Provide token or Post username & password.")
						.toString());
	}
}
