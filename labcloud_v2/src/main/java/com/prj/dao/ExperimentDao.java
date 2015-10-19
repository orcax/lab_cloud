package com.prj.dao;

import com.prj.entity.Experiment;
import com.prj.util.DataWrapper;

import java.util.List;

public interface ExperimentDao extends BaseDao<Experiment, Long> {

	DataWrapper listLabsByExp(long expId);

	DataWrapper addLabExp(long labId, long expId);

	DataWrapper deleteLabExp(long labId, long expId);

	DataWrapper statusListByExpIds(List<Long> expIds, int totalNumber, long classId);

	List<Experiment> getStudentAvailableExperimentOfSermester(long accountId,
			long semesterId);
}
