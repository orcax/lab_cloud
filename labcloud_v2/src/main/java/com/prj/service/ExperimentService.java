package com.prj.service;

import com.prj.entity.Experiment;
import com.prj.util.DataWrapper;

public interface ExperimentService extends BaseService<Experiment, Long> {

	DataWrapper listInfoByLab(Long lab);

	DataWrapper listInfoByCourse(Long course);

	DataWrapper listByLab(Long lab);

	DataWrapper addLab(long expId, long labId);

	DataWrapper listByClazz(Long clazzId);

	DataWrapper listInfoByClazz(Long courseId);

	DataWrapper listLabs(Long expId);

	DataWrapper deleteLabExp(long expId, long labId);

	DataWrapper statusListByClazz(long clazzId);
	
	DataWrapper getStudentAvailableExperimentOfSermester(
			long accountId, long semesterId); 
}
