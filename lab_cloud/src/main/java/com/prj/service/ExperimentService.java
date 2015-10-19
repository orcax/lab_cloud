package com.prj.service;

import java.util.List;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Course;
import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface ExperimentService {
	DataWrapper<Experiment> addExperiment(Experiment experiment);

	public DataWrapper<Experiment> deleteExperimentById(Integer id);

	DataWrapper<List<Experiment>> getAllExperiment();

	DataWrapper<List<Experiment>> getAllActiveExperiment();
	
	DataWrapper<Experiment> getExperimentById(int id);

	DataWrapper<Experiment> updateExperiment(Integer id, Experiment entity);

	DataWrapper<List<Experiment>> getExperimentByPage(int pagenumber, int pagesize);

	Page<Experiment> searchExperiment(int pagenumber, int pagesize, String name);

	Page<Experiment> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);

	DataWrapper<List<Lab>> getLabList(int experimentId);

	DataWrapper<Experiment> addLab(int experimentId, int labId);

	DataWrapper<Experiment> deleteLab(int experimentId, int labId);

	DataWrapper<List<Course>> getCourseList(int experimentId);
}
