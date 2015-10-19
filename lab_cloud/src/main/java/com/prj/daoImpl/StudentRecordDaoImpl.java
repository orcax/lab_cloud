package com.prj.daoImpl;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.StudentRecordDao;
import com.prj.entity.Class;
import com.prj.entity.Experiment;
import com.prj.entity.StudentRecord;
import com.prj.util.DataWrapper;

@Service("StudentRecordDaoImpl")
public class StudentRecordDaoImpl extends AbstractHibernateDao<StudentRecord, Integer>
		implements StudentRecordDao {
	protected StudentRecordDaoImpl() {
		super(StudentRecord.class);
	}

	@Override
	public Integer addStudentRecord(StudentRecord sr) {
		return super.add(sr);
	}

	@Override
	public StudentRecord getStudentRecordByClassStuExp(Integer classId,
			Integer stuId, Integer expId) {
		return (StudentRecord) getCurrentSession()
				.createSQLQuery("call get_stu_record_by_class_stu_exp(:class_id, :stu_id, :exp_id)")
				.addEntity(StudentRecord.class)
				.setInteger("class_id", classId)
				.setInteger("stu_id", stuId)
				.setInteger("exp_id", expId)
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.uniqueResult();
	}

	@Override
	public StudentRecord updateStudentRecord(StudentRecord sr) {
		return super.saveOrUpdate(sr);
	}

	@Override
	public DataWrapper<List<StudentRecord>> getStudentRecordsByClassExpInPage(Class clazz,
			Experiment exp, Integer pageSize, Integer pageNumber) {
		return findBySQLByPage("where classId="+clazz.getId()+" and experimentId="+exp.getId()+" order by modify_time desc", pageNumber, pageSize);
	}

	@Override
	public StudentRecord findStudentRecordById(Integer recordId) {
		return super.findById(recordId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudentRecord> getRecordByStuClass(Integer stuId,
			Integer classId) {
		return createCriteria()
			.add(Restrictions.and(
					Restrictions.eq("student.id", stuId),
					Restrictions.eq("clazz.id", classId)))
			.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
			.list();
	}

//	@Override
//	public StudentReservation findStudentRecordBySL(Integer studentId,
//			Integer labId, StudentReservation studentResrvation) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
