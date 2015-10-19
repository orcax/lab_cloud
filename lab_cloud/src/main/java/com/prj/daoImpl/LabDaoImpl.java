package com.prj.daoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.LabDao;
import com.prj.entity.Constant;
import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

@Service("LabDaoImpl")
public class LabDaoImpl extends AbstractHibernateDao<Lab, Integer>implements LabDao {

	protected LabDaoImpl() {
		super(Lab.class);
	}

	public Integer addLab(Lab v) {
		//v.setCreationDate(new Date());
		return add(v);
	}

	public Lab deleteLabById(Integer id) {
		Lab a = findById(id);
		if (a == null)
			return null;
		a.setIsActive(false);
		a.setStatus(Lab.Status.CLOSED);
		return a;
	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Lab>> getAllLab() {
		List<Lab> result = getCurrentSession().createCriteria(Lab.class)
				.addOrder(Order.asc("labNumber"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Lab>> ret = new DataWrapper<List<Lab>>();
		ret.setData(result);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Lab>> getAllActiveLab() {
		List<Lab> result = getCurrentSession().createCriteria(Lab.class)
				.add(Restrictions.eq("isActive", true))
				.addOrder(Order.asc("labNumber"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Lab>> ret = new DataWrapper<List<Lab>>();
		ret.setData(result);
		return ret;
	}

	public Lab findLabById(int id) {
		return findById(id);
	}

	public Lab updateLab(Lab v) {
		return saveOrUpdate(v);
	}

	public Lab getLabByNumber(String labNumber) {
		Criteria criteria = getCurrentSession().createCriteria(Lab.class);
		criteria.add(Restrictions.eq("labNumber", labNumber));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Lab)ret.get(0);
		}
		return null;
	}

	@Override
	public Lab getActiveLabByNumber(String labNumber) {
		List<?> ret = getCurrentSession().createCriteria(Lab.class)
				.add(Restrictions.eq("labNumber", labNumber))
				.add(Restrictions.eq("isActive", true))
				.list();
		if (ret != null && ret.size() > 0) {
			return (Lab)ret.get(0);
		}
		return null;
	}

	@Override
	public boolean isFull() {
		Constant c = (Constant) getCurrentSession().createCriteria(Constant.class)
			.add(Restrictions.eq("name", "LAB_MAX_NUM"))
			.uniqueResult();
		
		return getCurrentSession().createCriteria(Lab.class)
				.add(Restrictions.eq("isActive", true))
				.list()
				.size() >= c.getIntValue();
	}
	
	public Page<Lab> getLabByPage(int pagenumber, int pagesize) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Lab> getByCondition(List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Lab> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addExperiment(int id, Experiment experiment) {
		Lab lab = findById(id);
		if(lab == null||lab.getIsActive() == false)
			return false;
		Experiment exp = (Experiment)this.getCurrentSession().load(Experiment.class, experiment.getId());
		if(exp !=null)
			lab.getExperiments().add(exp);
		else
			lab.getExperiments().add(experiment);
		return true;
	}

	@Override
	public boolean deleteExperiment(int id, Experiment experiment) {
		// TODO Auto-generated method stub
		Lab lab = findById(id);
		if(lab == null||lab.getIsActive()==false)
			return false;
		Experiment exp = (Experiment)this.getCurrentSession().load(Experiment.class, experiment.getId());
		if(exp !=null)
			return lab.getExperiments().remove(exp);
		else
			return lab.getExperiments().remove(experiment);
	}

	@Override
	public List<Experiment> getExperimentsOfLab(int id) {
		Lab lab = findById(id);
		if(lab == null||lab.getIsActive() == false)
			return null;
		
		Set<Experiment> sexp = lab.getExperiments();
		Iterator<Experiment> it = sexp.iterator();
		List<Experiment> list = new ArrayList<Experiment>();
		while(it.hasNext()){
			Experiment exp = it.next();
			if(exp.getIsActive() == true)
				list.add(exp);
		}
		return list;
	}

//	@Override
//	public boolean contains(Integer experimentId) {
//		createCriteria().add(Restrictions.)
//		return false;
//	}
//	
}
