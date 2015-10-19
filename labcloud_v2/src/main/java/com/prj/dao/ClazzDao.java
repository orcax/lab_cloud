package com.prj.dao;

import java.util.List;

import com.prj.entity.Clazz;
import com.prj.util.DataWrapper;

public interface ClazzDao extends BaseDao<Clazz, Long> {

	DataWrapper addClazz(Clazz clazz, long courseId, long teacherId,
			long semesterId);

	DataWrapper getClazzByTeacher(Long teaId);

  List<Clazz> getClazzByStudent(Long stuId);

  DataWrapper getStudentListByPage(Long clazzId, int pageNum, int pageSize);

	List<Clazz> listSutdentSemesterClazz(Long semesterId, Long accountId);

  boolean isStudentExistInClazz(long stuId, long clazzId);

}
