package com.prj.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prj.security.AccountUserDetails;

public class ControllerUtils {
	
	public static AccountUserDetails currentAccount() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
		        return (AccountUserDetails) auth.getPrincipal();
		}
		return null;
	}
}
