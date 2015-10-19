package com.prj.dao;

import java.util.List;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface LabDao {
	Integer addLab(Lab v);

	Lab deleteLabById(Integer id);

	Lab findLabById(int id);

	DataWrapper<List<Lab>> getAllLab();

	DataWrapper<List<Lab>> getAllActiveLab();

	Lab updateLab(Lab v);

	Lab getLabByNumber(String number);

	Page<Lab> getLabByPage(int pagenumber, int pagesize);

	List<Lab> getByCondition(List<SimpleExpression> list);

	Page<Lab> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);

	Lab getActiveLabByNumber(String labNumber);

	boolean isFull();
	
	boolean addExperiment(int id, Experiment experiment);
	
	boolean deleteExperiment(int id, Experiment experiment);
	
	List<Experiment> getExperimentsOfLab(int id);

//	boolean contains(Integer experimentId);
}
