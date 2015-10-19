package com.prj.dao;

import java.util.List;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Experiment;
import com.prj.entity.InfoEntry;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface ExperimentDao {
	Integer addExperiment(Experiment v);

	Experiment deleteExperimentById(Integer id);

	Experiment findExperimentById(int id);

	DataWrapper<List<Experiment>> getAllExperiment();

	DataWrapper<List<Experiment>> getAllActiveExperiment();

	Experiment updateExperiment(Experiment v);

	Experiment getExperimentByNumber(String number);

	DataWrapper<List<Experiment>> getExperimentByPage(int pagenumber, int pagesize);

	List<Experiment> getByCondition(List<SimpleExpression> list);

	Page<Experiment> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);

	List<InfoEntry> getExpInfoListByClassLab(Integer courseId,
			Integer labId);

	List<Experiment> getExperimentByStudent(Integer studentId);
}
