package com.prj.service;

import com.prj.entity.Clazz;
import com.prj.util.DataWrapper;

public interface ClazzService extends BaseService<Clazz, Long> {

	DataWrapper create(Clazz clazz, long teacherId, long courseId,
			long semesterId);

	DataWrapper genInfoList(Long student);

	DataWrapper listByStuId(Long stuId);

	DataWrapper listByTeaId(Long teaId);

	DataWrapper listInfoPage();

	DataWrapper studentListById(Long clazzId, int pageNum, int pageSize);

	DataWrapper isStudentExistInClazz(long clazzId, long stuId);

	DataWrapper getClazzBySE(long stuId, long expId);
	
	DataWrapper getTeacherClazzList(long semesterId,long teacherId);
}
