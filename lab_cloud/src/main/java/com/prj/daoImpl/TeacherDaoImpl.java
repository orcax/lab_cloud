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
import com.prj.dao.TeacherDao;
import com.prj.entity.Account.Status;
import com.prj.entity.Teacher;
import com.prj.entity.Teacher.Role;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

@Service("TeacherDaoImpl")
public class TeacherDaoImpl extends AbstractHibernateDao<Teacher, Integer>
		implements TeacherDao {
	// private static Logger logger = Logger.getLogger(TeacherDaoImpl.class);

	protected TeacherDaoImpl() {
		super(Teacher.class);
	}

	public Integer addTeacher(Teacher v) {
		return add(v);
	}

//	public Teacher disableTeacherById(Integer id) {
//		Teacher a = findById(id);
//		if (a == null)
//			return null;
//		a.setStatus(Status.INACTIVE);
////		a.setLoginToken(null);
//		return a;
//	}
	


	@SuppressWarnings("unchecked")
	public DataWrapper<List<Teacher>> getAllTeacher() {
		List<Teacher> result = createCriteria()
				.addOrder(Order.asc("number"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Teacher>> ret = new DataWrapper<List<Teacher>>();
		ret.setData(result);
		return ret;
	}

	public Teacher findTeacherById(Integer id) {
		return findById(id);
	}
	

	public Teacher getTeacherByNumber(String number) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("number", number));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Teacher) ret.get(0);
		}
		return null;
	}

	public Teacher findTeacherByToken(String token) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("loginToken", token));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Teacher) ret.get(0);
		}
		return null;
	}

	public Teacher updateTeacher(Teacher v) {
		return saveOrUpdate(v);
	}

	public DataWrapper<List<Teacher>> getTeacherByRolePage(Role role, int pagenumber, int pagesize) {
		String sql = null;
		if (role == Role.ALL) {
			sql = "where status<>2 order by number asc";
		} else {
			sql = "where status<>2 and role=" + role.ordinal() + " order by number asc";
		}
		return findBySQLByPage(sql, pagenumber, pagesize);
	}

	public List<Teacher> getByCondition(List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Teacher> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Teacher> getTeacherByStatus(Status as) {
		// TODO Auto-generated method stub
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("status", as));
		@SuppressWarnings("unchecked")
		List<Teacher> ret = criteria.list();
		return ret;
	}

	@Override
	public Set<Teacher> getTeachersByIds(List<Integer> teacherIds) {
		Set<Teacher> teachers = new HashSet<Teacher>();
		for (Integer id : teacherIds) {
			teachers.add(findTeacherById(id));
		}
		return teachers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> getAvailableTeachersByCall(Integer classReservationId) {
		return getCurrentSession().createSQLQuery("call get_ava_labteas(:id)")
				.addEntity(Teacher.class)
				.setInteger("id", classReservationId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> getTeacherList(Integer labId) {
		return super.createSQLQuery("call get_tea_list_by_lab(:lab_id)")
				.setInteger("lab_id", labId)
				.list();
	}
}
