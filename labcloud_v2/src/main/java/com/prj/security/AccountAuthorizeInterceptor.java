package com.prj.security;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.prj.entity.Account.Role;
import com.prj.util.RequiredRole;
import com.prj.util.RootPath;

public class AccountAuthorizeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		RootPath.value = request.getServletContext().getRealPath("/");
		RequiredRole authsRequired = ((HandlerMethod) handler)
				.getMethodAnnotation(RequiredRole.class);
		if (authsRequired != null) {
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			Assert.notNull(authentication, "All request must be authenticated.");

			Collection<? extends GrantedAuthority> grantedAuths = authentication
					.getAuthorities();
			Role[] requiredAuths = authsRequired.value();
			for (Role requiredAuth : requiredAuths) {
				for (GrantedAuthority grantedAuth : grantedAuths) {
					if (grantedAuth.getAuthority().equals(
							requiredAuth.toRoleString())) {
						return true;
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}

}
