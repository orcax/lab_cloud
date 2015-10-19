package com.prj.daoImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.StudentDao;
import com.prj.entity.Account.Status;
import com.prj.entity.Student;
import com.prj.util.DataWrapper;

@Service("StudentDaoImpl")
public class StudentDaoImpl extends AbstractHibernateDao<Student, Integer>
		implements StudentDao {
	// private static Logger logger = Logger.getLogger(StudentDaoImpl.class);

	protected StudentDaoImpl() {
		super(Student.class);
	}

	public Integer addStudent(Student v) {
		return add(v);
	}

//	public Student disableStudentById(Integer id) {
//		Student a = findById(id);
//		if (a == null)
//			return null;
//		a.setStatus(Status.INACTIVE);
////		a.setLoginToken(null);
//		return a;
//	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Student>> getAllStudent() {
		List<Student> result = createCriteria()
				.addOrder(Order.asc("number"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Student>> ret = new DataWrapper<List<Student>>();
		ret.setData(result);
		return ret;
	}

	public Student findStudentById(Integer id) {
		return findById(id);
	}

	public Student getStudentByNumber(String number) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("number", number));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Student) ret.get(0);
		}
		return null;
	}

	public Student findStudentByToken(String token) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("loginToken", token));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Student) ret.get(0);
		}
		return null;
	}

	public Student updateStudent(Student v) {
		return saveOrUpdate(v);
	}

	public DataWrapper<List<Student>> getStudentbyPage(int pagenumber, int pagesize) {
//		Criteria criteria = createCriteria()
//				.addOrder(Order.desc("modify_time"))
//				.add(Restrictions.or(Restrictions.eq("status", Account.Status.NEW), 
//						Restrictions.eq("status", Account.Status.ACTIVE)));
//		PageResult<Student> pr = findByCriteriaByPage(criteria, pagenumber, pagesize);
//		DataWrapper<List<Student>> ret = new DataWrapper<List<Student>>();
//		if (pr.getCurrPageNum() > pr.getTotalPageNum()) {
//			ret.setErrorCode(ErrorCodeEnum.Page_Number_Invaild);
//		} else {
//			ret.setData(pr.getData());
//		}
//		ret.setCurrPageNum(pr.getCurrPageNum());
//		ret.setNumPerPage(pr.getNumPerPage());
//		ret.setTotalItemNum(pr.getTotalItemNum());
//		ret.setTotalPageNum(pr.getTotalPageNum());
//		return ret;
		return findBySQLByPage("where status=0 or status=1 order by modify_time desc", pagenumber, pagesize);
	}
	
	@Override
	public List<Student> addStudents(List<Student> list) {
		for (Student student : list) {
			Student s = getStudentByNumber(student.getNumber());
			if (s == null) {
				student.setId(add(student));
			} else {
				student.setId(s.getId());
			}
		}
		return list;
	}

	@Override
	public List<Student> getStudentByStatus(Status as) {
		// TODO Auto-generated method stub
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("status", as));
		@SuppressWarnings("unchecked")
		List<Student> ret = criteria.list();
		return ret;
	}

	@Override
	public DataWrapper<List<Student>> getStudentPageBySlotRes(
			Integer slotResId, Integer pageSize, Integer pageNumber) {
		return super.findBySQLByPage("s inner join slot_reservation_student slrs on slrs.studentId=s.id where slrs.slotReservationId="+slotResId+" order by s.number asc", 
				pageNumber, pageSize);
	}

}
