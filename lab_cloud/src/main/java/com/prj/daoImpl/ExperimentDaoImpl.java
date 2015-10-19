package com.prj.daoImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.ExperimentDao;
import com.prj.entity.Experiment;
import com.prj.entity.InfoEntry;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

@Service("ExperimentDaoImpl")
public class ExperimentDaoImpl extends AbstractHibernateDao<Experiment, Integer>implements ExperimentDao {

	protected ExperimentDaoImpl() {
		super(Experiment.class);
	}

	public Integer addExperiment(Experiment v) {
		return add(v);
	}

	public Experiment deleteExperimentById(Integer id) {
		Experiment a = findById(id);
		if (a == null)
			return null;
		a.setIsActive(false);
		return a;
	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Experiment>> getAllExperiment() {
		List<Experiment> result = getCurrentSession().createCriteria(Experiment.class)
				.addOrder(Order.asc("experimentNumber"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Experiment>> ret = new DataWrapper<List<Experiment>>();
		ret.setData(result);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Experiment>> getAllActiveExperiment() {
		List<Experiment> result = getCurrentSession().createCriteria(Experiment.class)
				.add(Restrictions.eq("isActive", true))
				.addOrder(Order.asc("experimentNumber"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Experiment>> ret = new DataWrapper<List<Experiment>>();
		ret.setData(result);
		return ret;
	}

	public Experiment findExperimentById(int id) {
		return findById(id);
	}

	public Experiment updateExperiment(Experiment v) {
		return saveOrUpdate(v);
	}

	public Experiment getExperimentByNumber(String number) {
		Criteria criteria = getCurrentSession().createCriteria(Experiment.class);
		criteria.add(Restrictions.eq("experimentNumber", number));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Experiment)ret.get(0);
		}
		return null;
	}

	public DataWrapper<List<Experiment>> getExperimentByPage(int pagenumber, int pagesize) {
//		Criteria criteria = createCriteria()
//				//.addOrder(Order.desc("modify_time"))
//				.add(Restrictions.eq("isActive", true));
//		PageResult<Experiment> pr = findByCriteriaByPage(criteria, pagenumber, pagesize);
//		DataWrapper<List<Experiment>> ret = new DataWrapper<List<Experiment>>();
//		if (pr.getCurrPageNum() > pr.getTotalPageNum()) {
//			ret.setErrorCode(ErrorCodeEnum.Page_Number_Invaild);
//		} else {
//			ret.setData(pr.getData());
//		}
//		ret.setCurrPageNum(pr.getCurrPageNum());
//		ret.setNumPerPage(pr.getNumPerPage());
//		ret.setTotalItemNum(pr.getTotalItemNum());
//		ret.setTotalPageNum(pr.getTotalPageNum());
//		return ret;
		return findBySQLByPage("where isActive=true order by modify_time desc", pagenumber, pagesize);
	}

	public List<Experiment> getByCondition(List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Experiment> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InfoEntry> getExpInfoListByClassLab(
			Integer classId, Integer labId) {
		return super.getCurrentSession().createSQLQuery("call get_exp_infos_by_lab_class(:class_id, :lab_id)")
				.addEntity(InfoEntry.class)
				.setInteger("class_id", classId)
				.setInteger("lab_id", labId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Experiment> getExperimentByStudent(Integer studentId) {
		return createSQLQuery("call get_exp_by_stu(:stu_id")
				.setInteger("stu_id", studentId)
				.list();
	}
}
