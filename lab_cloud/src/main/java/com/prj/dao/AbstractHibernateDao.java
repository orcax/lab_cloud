package com.prj.dao;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import com.prj.entity.BaseEntity;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.PageResult;

public abstract class AbstractHibernateDao<E, I extends Serializable> {

	private final Class<E> entityClass;

	protected AbstractHibernateDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	@Autowired
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	private boolean setModifyTime(E e) {
		BaseEntity be = (BaseEntity) e;
		if (be instanceof BaseEntity) {
			be.setModify_time(new Date());
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public I add(E e) {
//		setModifyTime(e);
		return (I) getCurrentSession().save(e);
	}

	@SuppressWarnings("unchecked")
	public E findById(I id) {
		return (E) getCurrentSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public E saveOrUpdate(E e) {
		setModifyTime(e);
        return (E) getCurrentSession().merge(e);
	}

	public void delete(E e) {
//		setModifyTime(e);
		getCurrentSession().delete(e);
	}

	@SuppressWarnings("unchecked")
	public List<E> findByCriteria(List<Criterion> criterions) {
		Criteria criteria = createCriteria();
		if (criterions != null && criterions.size() > 0) {
			for(int i = 0; i < criterions.size(); i++) {
				criteria.add(criterions.get(i));
			}
		}
		return criteria.list();
	}
	
	@Deprecated
	public PageResult<E> findByDefaultPage() {		
		return findByCriteriaByPage(null, PageResult.DEFAULT_CURR_PAGE_NUM, PageResult.DEFAULT_NUM_PER_PAGE);
	}
	
	@Deprecated
	public PageResult<E> findByPage(int currPageNum, int numPerPage) {
		return findByCriteriaByPage(null, currPageNum, numPerPage);
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public PageResult<E> findByCriteriaByPage(Criteria criteria, int currPageNum, int numPerPage) {
		PageResult<E> pageResult = new PageResult<E>(currPageNum, numPerPage);
		
//		Criteria criteria = getCurrentSession().createCriteria(entityClass);
//		
//		if (criterions != null && criterions.size() > 0) {
//			for(int i = 0; i < criterions.size(); i++) {
//				criteria.add(criterions.get(i));
//			}
//		}
		if (criteria == null)
			criteria = createCriteria();
		
		criteria.setProjection(Projections.rowCount());
		pageResult.setTotalItemNum(((Long)criteria.uniqueResult()).intValue());
		
		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		criteria.setFirstResult(PageResult.getStartOfPage(pageResult.getCurrPageNum(), pageResult.getNumPerPage()));
		criteria.setMaxResults(pageResult.getNumPerPage());
		pageResult.setData(criteria.list());
		
		return pageResult;
	}
	
	/**
	 * sqlStr设为null或设为附加的条件where ... order by...等等
	 * @param sqlStr
	 * @param currPageNum
	 * @param numPerPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataWrapper<List<E>> findBySQLByPage(String sqlStr, Integer currPageNum, Integer numPerPage) {
		DataWrapper<List<E>> ret = new DataWrapper<List<E>>();		
		try {
			String tableName = getTableName();
			String countSQL = "select count(*) from " + tableName;
			String pageSQL = "select * from " + tableName;
			if (sqlStr != null) {
				countSQL = countSQL + " " + sqlStr;
				pageSQL = pageSQL + " " + sqlStr;
			}
			pageSQL = pageSQL + " limit :offset, :row_count";
			
			ret.setCurrPageNum(currPageNum);
			ret.setNumPerPage(numPerPage);
			BigInteger count = (BigInteger) getCurrentSession().createSQLQuery(countSQL)
					.uniqueResult();
			ret.setTotalItemNum(count.intValue());
			ret.setTotalPageNum(getTotalPageNum(ret.getTotalItemNum(), ret.getNumPerPage()));
			if (currPageNum < 0 || numPerPage < 0) {
				ret.setErrorCode(ErrorCodeEnum.Page_Parameter_Wrong);
//				ret.setData(new ArrayList<E>(0));
			} else {
				ret.setData(getCurrentSession().createSQLQuery(pageSQL)
						.addEntity(entityClass)
						.setInteger("offset", getStartOfPage(currPageNum, numPerPage))
						.setInteger("row_count", numPerPage)
						.list());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected DataWrapper<List<E>> convertToPageWrapper(List<E> data, Integer currPageNum, Integer numPerPage) {
		DataWrapper<List<E>> ret = new DataWrapper<List<E>>();		
		try {
			ret.setCurrPageNum(currPageNum);
			ret.setNumPerPage(numPerPage);
			ret.setTotalItemNum(data.size());
			ret.setTotalPageNum(getTotalPageNum(ret.getTotalItemNum(), ret.getNumPerPage()));
			if (currPageNum < 0 || numPerPage < 0) {
				ret.setErrorCode(ErrorCodeEnum.Page_Parameter_Wrong);
			} else {
				ret.setData(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected Criteria createCriteria() {
		return getCurrentSession().createCriteria(entityClass);
	}
	
	protected SQLQuery createSQLQuery(String queryString) {
		return getCurrentSession().createSQLQuery(queryString).addEntity(entityClass);
	}
	
	protected static int getStartOfPage(int pageNumber, int pageSize) {
		return (pageNumber - 1) * pageSize;
	}
	
	protected static int getTotalPageNum(Integer totalItemNum, Integer pageSize) {
		if(totalItemNum % pageSize == 0)
			return totalItemNum / pageSize;
		else
			return totalItemNum / pageSize + 1;
	}
	
	protected String getTableName() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Annotation[] as = entityClass.getAnnotations();
		Table t = null;
		if (as.length == 0)
			return entityClass.getName();
		for (Annotation a : as) {
			if (a instanceof Table) {
				t = (Table) a;
			}
		}
		if (t == null)
			return null;
		Method m = t.getClass().getMethod("name");
		return (String) m.invoke(t);
	}
}