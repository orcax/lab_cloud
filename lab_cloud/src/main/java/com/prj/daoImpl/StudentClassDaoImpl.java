package com.prj.daoImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.StudentClassDao;
import com.prj.entity.Class;
import com.prj.entity.StudentClass;
import com.prj.util.DataWrapper;

@Service("StudentClassDaoImpl")
public class StudentClassDaoImpl extends AbstractHibernateDao<StudentClass,Integer> implements StudentClassDao{
	
	protected StudentClassDaoImpl(){
		super(StudentClass.class);
	}

	@Override
	public StudentClass findStudentClassById(Integer id) {
		return findById(id);
	}
	
	@Override
	public StudentClass getStudentClassBySC(Integer studentId, Integer classId) {
		Criteria criteria = createCriteria()
			.add(Restrictions.eq("student.id", studentId))
			.add(Restrictions.eq("clazz.id", classId));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (StudentClass)ret.get(0);
		}
		return null;
	}
	
	@Override
	public Integer addStudentClass(StudentClass sc) {
		return add(sc);
	}
	
	@Override
	public List<StudentClass> addStudentClasses(List<StudentClass> list) {
		for (StudentClass item : list) {
			StudentClass sc = getStudentClassBySC(item.getStudent().getId(), item.getClazz().getId());
			if (sc == null) {
				item.setId(add(item));
			} else {
				item.setId(sc.getId());
			}
		}
		return list;
	}

	@Override
	public DataWrapper<List<StudentClass>> getStudentClassByPage(
			int pagenumber, int pagesize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataWrapper<List<StudentClass>> getStudentByClassByPage(Class c,
			int pagenumber, int pagesize) {
//		// TODO Auto-generated method stub
//		Criteria criteria = createCriteria()
//				//.addOrder(Order.desc("modify_time"))
//				.add(Restrictions.sqlRestriction("classid = "+c.getId()));
//		PageResult<StudentClass> pr = findByCriteriaByPage(criteria, pagenumber, pagesize);
//		DataWrapper<List<StudentClass>> ret = new DataWrapper<List<StudentClass>>();
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
		return findBySQLByPage("where classid="+c.getId()+" order by modify_time desc", pagenumber, pagesize);
	}
}
