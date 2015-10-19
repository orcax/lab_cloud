package com.prj.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.prj.entity.Account;
import com.prj.entity.Teacher;
import com.prj.entity.Teacher.Role;
import com.prj.service.AccountService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.TokenTool;

@Component
@Aspect
public class AccountAspect {
	@Resource(name = "AccountServiceImpl")
	AccountService as;

//	@Resource(name = "AccountDaoImpl")
//	AccountDao dao;
		
	@Before("@annotation(accountAccess) && args(dataWrapper,..)")
	public void checkBefore(DataWrapper<?> dataWrapper, AccountAccess accountAccess) {
		check(dataWrapper, accountAccess);
	}
	
	private void check(DataWrapper<?> dataWrapper, AccountAccess aa) {
		String token = dataWrapper.getToken();
		AccountCharacter ac = dataWrapper.getAccountCharacter();
		if (token == null) {
			System.err.println("Token NULL");
			throw new AuthorityException(ErrorCodeEnum.Token_Invalid);
		} else if (ac == null) {
			throw new AuthorityException(ErrorCodeEnum.Account_Character_Null);
		} else {
			Account a = as.getAccountByToken(token, ac).getData();
			if (a == null) {
				System.err.println("Token: " + token);
				throw new AuthorityException(ErrorCodeEnum.Token_Invalid);
			} else if (!TokenTool.isTokenValid(token)) {
				throw new AuthorityException(ErrorCodeEnum.Token_Expired);
			} else {
				switch (aa.checkAccountCharacter()) {
				case ANY:
					break;
				case ALL_TEACHER:
					if (ac != AccountCharacter.TEACHER)
						throw new AuthorityException(ErrorCodeEnum.Access_Denied);
					break;
				case LAB_TEACHER:
					if (ac != AccountCharacter.TEACHER || (((Teacher)a).getRole() != Role.LAB && ((Teacher)a).getRole() != Role.ALL))
						throw new AuthorityException(ErrorCodeEnum.Access_Denied);
					break;
				case TEACHER:
					if (ac != AccountCharacter.TEACHER || (((Teacher)a).getRole() != Role.NORMAL && ((Teacher)a).getRole() != Role.ALL))
						throw new AuthorityException(ErrorCodeEnum.Access_Denied);
					break;
				case ADMINISTRATOR:
				case STUDENT:
				default:
					if (ac != aa.checkAccountCharacter()) {
						throw new AuthorityException(ErrorCodeEnum.Access_Denied);
					}
					break;
				}
				//TODO REMOVE TOKEN
//				dataWrapper.setAccountCharacter(null);
//				dataWrapper.setToken(null);
				dataWrapper.setAccountId(a.getId());
			}
		}
	}
}
