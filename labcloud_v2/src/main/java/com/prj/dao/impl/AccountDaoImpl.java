package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.AccountDao;
import com.prj.entity.Account;

@Repository("AccountDaoImpl")
public class AccountDaoImpl extends BaseDaoHibernateImpl<Account, Long>
		implements AccountDao {

	public AccountDaoImpl() {
		super(Account.class);
	}


}
