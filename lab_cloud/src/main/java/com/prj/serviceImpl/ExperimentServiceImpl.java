package com.prj.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.ExperimentDao;
import com.prj.dao.LabDao;
import com.prj.entity.Course;
import com.prj.entity.CourseExperiment;
import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.service.ExperimentService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.Page;

@Service("ExperimentServiceImpl")
public class ExperimentServiceImpl implements ExperimentService {

	@Resource(name = "ExperimentDaoImpl")
	ExperimentDao expDao;

	@Resource(name = "LabDaoImpl")
	LabDao labDao;
	
	public DataWrapper<Experiment> addExperiment(Experiment experiment) {
		experiment.setIsActive(true);
		DataWrapper<Experiment> ret = new DataWrapper<Experiment>();
		Experiment a = expDao.getExperimentByNumber(experiment.getExperimentNumber());
		if (a != null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Exist);
		} else if (expDao.addExperiment(experiment) != null) {
			ret.setData(experiment);
		} else {
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	public DataWrapper<Experiment> deleteExperimentById(Integer id) {
		Experiment a = expDao.deleteExperimentById(id);
		DataWrapper<Experiment> ret = new DataWrapper<Experiment>(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
		}
		return ret;
	}

	public DataWrapper<List<Experiment>> getAllExperiment() {
		return expDao.getAllExperiment();
	}

	public DataWrapper<List<Experiment>> getAllActiveExperiment() {
		return expDao.getAllActiveExperiment();
	}

	public DataWrapper<Experiment> getExperimentById(int id) {
		DataWrapper<Experiment> ret = new DataWrapper<Experiment>();
		Experiment a = expDao.findExperimentById(id);
		ret.setData(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
		}
		return ret;
	}

	public DataWrapper<Experiment> updateExperiment(Integer id, Experiment v) {
		// Only set attributes allowed
		Experiment e = expDao.findExperimentById(id);
		DataWrapper<Experiment> ret = new DataWrapper<Experiment>();
		if (e == null) {
			ret.setErrorCode(ErrorCodeEnum.Account_Not_Exist);
		} else {
			e.setExperimentName(v.getExperimentName());
			e.setExperimentNumber(v.getExperimentNumber());
			e.setMaximumStudent(v.getMaximumStudent());
			e.setMinimumStudent(v.getMinimumStudent());
			e.setExperimentNumber(v.getExperimentNumber());
			e = expDao.updateExperiment(e);
			ret.setData(e);
		}
		return ret;
	}

	public DataWrapper<List<Experiment>> getExperimentByPage(int pagenumber, int pagesize) {
		return expDao.getExperimentByPage(pagenumber, pagesize);
	}
	
	@Override
	public DataWrapper<List<Lab>> getLabList(int experimentId) {
		DataWrapper<List<Lab>> ret = new DataWrapper<List<Lab>>();
		Experiment e = expDao.findExperimentById(experimentId);
		if (e == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
		} else {
			ret.setData(new ArrayList<Lab>(e.getLabs()));
		}
		return ret;
	}

//	private Set<Lab> removeExperimentRefs(Experiment e) {
//		Set<Lab> labs = e.getLabs();
//		for (Lab lab : labs) {
//			lab.setExperiments(null);
//		}
//		return labs;
//	}
	
	@Override
	public DataWrapper<Experiment> addLab(int experimentId, int labId) {
		DataWrapper<Experiment> ret = new DataWrapper<Experiment>();
		Experiment e = expDao.findExperimentById(experimentId);
		if (e == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			return ret;
		}
		Lab l = labDao.findLabById(labId);
		if (l == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			return ret;
		}
		
		// check if lab already exists
		Set<Lab> labs = e.getLabs();
		for (Lab lab : labs) {
			if (l.getId() == lab.getId()) {
				ret.setErrorCode(ErrorCodeEnum.Lab_Exist);
				return ret; 
			}
		}
		
		if (e.getMaximumStudent() > l.getCapacity()) {
			return ret.setErrorCode(ErrorCodeEnum.Experiment_Max_Student_Out_Of_Range);
		}
		
		l.getExperiments().add(e);
		l = labDao.updateLab(l);
		
		if (l == null) {
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		} else {
			e.getLabs().add(l);
			//removeExperimentRefs(e);
			//e = expDao.updateExperiment(e);
			ret.setData(e);
		}
		return ret;
	}

	@Override
	public DataWrapper<Experiment> deleteLab(int experimentId, int labId) {
		DataWrapper<Experiment> ret = new DataWrapper<Experiment>();
		Experiment e = expDao.findExperimentById(experimentId);
		if (e == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			return ret;
		}
		Lab l = labDao.findLabById(labId);
		if (l == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Exist);
			return ret;
		}
		
		// check if lab exists
		Lab target = null;
		Set<Lab> labs = e.getLabs();
		for (Lab lab : labs) {
			if (l.getId() == lab.getId()) {
				target = lab;
			}
		}
		if (target == null) {
			ret.setErrorCode(ErrorCodeEnum.Lab_Not_Included);
		} else {
			e.getLabs().remove(target);
			target.getExperiments().remove(e);
			target = labDao.updateLab(target);
			if (target == null) {
				ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
			} else {
				e = expDao.findExperimentById(experimentId);
//				removeExperimentRefs(e);
//				e = expDao.updateExperiment(e);
				ret.setData(e);
			}
		}
		return ret;
	}
	
	@Override
	public DataWrapper<List<Course>> getCourseList(int experimentId) {
		DataWrapper<List<Course>> ret = new DataWrapper<List<Course>>();
		Experiment e = expDao.findExperimentById(experimentId);
		if (e == null) {
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
		} else {
			List<Course> courses = new ArrayList<Course>();
			Set<CourseExperiment> ces = e.getCourseExperiment();
			for (CourseExperiment ce : ces) {
				Course tmp = ce.getCourse();
//				tmp.setCourseExperiment(null);
				courses.add(tmp);
			}
			ret.setData(courses);
		}
		return ret;
	}
	
	// Methods Following Are Not Checked... YET!

	public Page<Experiment> searchExperiment(int pagenumber, int pagesize,
			String name) {
		//return dao.searchExperiment(pagenumber, pagesize, name);
		return null;
	}

	public Page<Experiment> getByPageWithConditions(int pagenumber,
			int pagesize, List<SimpleExpression> list) {
		return expDao.getByPageWithConditions(pagenumber, pagesize, list);
	}
}
