package com.prj.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prj.entity.Account;
import com.prj.service.AccountService;

@Service
public class AccountUserDetailsService implements UserDetailsService {

	@Autowired
	AccountService as;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Account a = (Account) as.findByNumber(username).getData();
		if (a == null)
			throw new UsernameNotFoundException("The user could not be found.");
		return new AccountUserDetails(a);
	}

}
