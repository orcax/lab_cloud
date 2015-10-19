package com.prj.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Account;
import com.prj.entity.Administrator;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface AdministratorDao {
	Integer addAdministrator(Administrator v);

//	Administrator disableAdministratorById(Integer id);

	Administrator findAdministratorById(Integer id);

	DataWrapper<List<Administrator>> getAllAdministrator();

	Administrator updateAdministrator(Administrator v);

	DataWrapper<List<Administrator>> getAdministratorbyPage(int pagenumber, int pagesize);

	List<Administrator> getByCondition(List<SimpleExpression> list);

	Page<Administrator> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);

	Administrator getAdministratorByNumber(String number);

	Administrator findAdministratorByToken(String token);

	List<Administrator> getAdministratorByStatus(Account.Status as);

	Set<Administrator> getAdministratorsByIds(List<Integer> adminIds);

//	List<Administrator> getAvailableAdministratorsByCall(Integer classReservationId);
}
