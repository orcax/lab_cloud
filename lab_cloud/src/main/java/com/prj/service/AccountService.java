package com.prj.service;

import java.util.List;

import com.prj.entity.Account;
import com.prj.util.AccountCharacter;
import com.prj.util.DataWrapper;
import com.prj.util.LoginParameter;
import com.prj.util.Page;
import com.prj.util.SearchCriteria;

public interface AccountService {
	DataWrapper<Account> login(LoginParameter loginParameter, AccountCharacter ac);

	DataWrapper<Void> logout(Integer id, AccountCharacter ac);

//	@Deprecated
//	DataWrapper<Account> addAccount(Account account, AccountCharacter ac);

//	@Deprecated
//	DataWrapper<Account> disableAccountById(Integer id);

//	@Deprecated
//	DataWrapper<List<Account>> getAllAccount();
	
	DataWrapper<Account> getAccountByToken(String token, AccountCharacter ac);

//	DataWrapper<Account> getAccountById(int id);

//	DataWrapper<Account> updateAccount(Account entity);

//	DataWrapper<Account> updateAccountCharacter(Integer accountId,
//	AccountCharacter accountCharacter);

//	DataWrapper<Account> reset(PasswordReset data, AccountCharacter ac);

//	Page<Account> getAccountbyPage(int pagenumber, int pagesize);

	Page<Account> searchAccount(int pagenumber, int pagesize, String name);

//	Page<Account> getByPageWithConditions(int pagenumber, int pagesize,
//			List<SimpleExpression> list);

	DataWrapper<List<? extends Account>> searchAccount(SearchCriteria sc);
	
	DataWrapper<Account> getAccountByNumber(String number, AccountCharacter ac);

	DataWrapper<Account> getAccountById(Integer id, AccountCharacter ac);
	
	DataWrapper<Account> updateAccount(Account account, AccountCharacter ac);
	
	DataWrapper<List<? extends Account>> getAccountByRole(AccountCharacter ac);
	
	DataWrapper<List<? extends Account>> getAccountByStatus(Account.Status status);
}
