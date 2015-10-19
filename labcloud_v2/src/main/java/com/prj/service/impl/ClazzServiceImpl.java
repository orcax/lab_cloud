package com.prj.service.impl;

import java.util.ArrayList;

import com.prj.dao.AccountDao;
import com.prj.dao.ClazzDao;
import com.prj.dao.SemesterDao;
import com.prj.entity.Clazz;
import com.prj.service.ClazzService;
import com.prj.util.DataWrapper;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClazzServiceImpl extends BaseServiceImpl<Clazz, Long> implements
		ClazzService {
	ClazzDao cld;

	@Autowired
	AccountDao accountDao;

	@Autowired
	SemesterDao semesterDao;

	@Autowired
	public ClazzServiceImpl(ClazzDao cld) {
		super(Clazz.class, cld);
		this.cld = cld;
	}

	public DataWrapper create(Clazz entity) {
		assertUniqueNumber(entity.getNumber());
		return super.createEntity(entity);
	}

	public DataWrapper create(Clazz clazz, long teacherId, long courseId,
			long semesterId) {
		assertUniqueNumber(clazz.getNumber());
		return cld.addClazz(clazz, courseId, teacherId, semesterId);
	}

	public DataWrapper genInfoList(Long student) {
		// TODO Auto-generated method stub
		return null;
	}

	public DataWrapper listByStuId(Long stuId) {
		// TODO Auto-generated method stub
		return new DataWrapper(cld.getClazzByStudent(stuId));
	}

	public DataWrapper listByTeaId(Long teaId) {
		// TODO Auto-generated method stub

		return cld.getClazzByTeacher(teaId);
	}

	public DataWrapper listInfoPage() {
		// TODO Auto-generated method stub
		return null;
	}

	public DataWrapper studentListById(Long clazzId, int pageNum, int pageSize) {
		return cld.getStudentListByPage(clazzId, pageNum, pageSize);
	}

	public DataWrapper isStudentExistInClazz(long clazzId, long stuId) {
		return new DataWrapper(cld.isStudentExistInClazz(stuId, clazzId));
	}

	public DataWrapper getClazzBySE(long stuId, long expId) {
		return new DataWrapper(cld
				.createCriteria()
				// .createCriteria("students")
				.createAlias("students", "stus")
				.add(Restrictions.eq("stus.id", stuId))
				// .createCriteria("course.experiments")
				.createAlias("course.experiments", "exps")
				.add(Restrictions.eq("exps.id", expId)).list());
	}

	public DataWrapper getTeacherClazzList(long semesterId, long teacherId) {
		ArrayList<Criterion> conditions = new ArrayList<Criterion>();
		conditions.add(Restrictions.eq("teacher", accountDao.read(teacherId)));
		conditions
				.add(Restrictions.eq("semester", semesterDao.read(semesterId)));
		return new DataWrapper(cld.getAllByConditions(conditions));
	}

}
