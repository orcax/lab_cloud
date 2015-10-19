package com.prj.daoImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.AdministratorDao;
import com.prj.entity.Account;
import com.prj.entity.Administrator;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

@Service("AdministratorDaoImpl")
public class AdministratorDaoImpl extends AbstractHibernateDao<Administrator, Integer>
		implements AdministratorDao {
	// private static Logger logger = Logger.getLogger(AdministratorDaoImpl.class);

	protected AdministratorDaoImpl() {
		super(Administrator.class);
	}

	public Integer addAdministrator(Administrator v) {
		return add(v);
	}

//	public Administrator disableAdministratorById(Integer id) {
//		Administrator a = findById(id);
//		if (a == null)
//			return null;
//		a.setStatus(Status.INACTIVE);
////		a.setLoginToken(null);
//		return a;
//	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Administrator>> getAllAdministrator() {
		List<Administrator> result = createCriteria()
				.addOrder(Order.asc("number"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Administrator>> ret = new DataWrapper<List<Administrator>>();
		ret.setData(result);
		return ret;
	}

	public Administrator findAdministratorById(Integer id) {
		return findById(id);
	}

	public Administrator getAdministratorByNumber(String number) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("number", number));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Administrator) ret.get(0);
		}
		return null;
	}

	public Administrator findAdministratorByToken(String token) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("loginToken", token));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Administrator) ret.get(0);
		}
		return null;
	}

	public Administrator updateAdministrator(Administrator v) {
		return saveOrUpdate(v);
	}

	public DataWrapper<List<Administrator>> getAdministratorbyPage(int pagenumber, int pagesize) {
		return findBySQLByPage("where status=0 or status=1 order by modify_time desc", pagenumber, pagesize);
	}

	@Override
	public Set<Administrator> getAdministratorsByIds(List<Integer> adminIds) {
		Set<Administrator> admins = new HashSet<Administrator>();
		for (Integer id : adminIds) {
			admins.add(findAdministratorById(id));
		}
		return admins;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Administrator> getAvailableAdministrators(Set<Integer> adminIds) {
//		return createCriteria()
//				.add(Restrictions.not(Restrictions.in("id", adminIds)))
//				.list();
//	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Administrator> getAvailableAdministratorsByCall(Integer classReservationId) {
//		return getCurrentSession().createSQLQuery("call get_ava_admins(:id)")
//				.addEntity(Administrator.class)
//				.setInteger("id", classReservationId)
//				.list();
//		
////		return createCriteria()
////				.add(Restrictions.sqlRestriction("call get_ava_admins(?)", classReservationId, StandardBasicTypes.INTEGER))
////				.list();
//	}
	
	
	@Override
	public List<Administrator> getAdministratorByStatus(Account.Status as) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("status", as));
		@SuppressWarnings("unchecked")
		List<Administrator> ret = criteria.list();
		return ret;
	}

	public List<Administrator> getByCondition(List<SimpleExpression> list) {
		return null;
	}
	
	public Page<Administrator> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list) {
		return null;
	}
}
