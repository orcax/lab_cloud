package com.prj.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.prj.util.DataWrapper;

public interface BaseDao<T, PK extends Serializable> {

	// void setType(Class<T> type) ;

	PK create(T o);

	T read(PK id);

	void update(T o);

	void merge(T o);
	
	void delete(T o);

	T findBy(String propertyName, Object value);

	void update(PK id, String propertyName, Object value);

	boolean isUnique(String propertyName, Object value);
	
	int countByConditions(List<Criterion> conditions);
	
	List<T> getAllByConditions(List<Criterion> conditions);

	DataWrapper getPageByConditions(int pageNum, int pageSize,
			List<Criterion> conditions);

	DataWrapper getPageByConditions(int pageNum, int pageSize,
			List<Criterion> conditions, List<Order> orders);

	Criteria createCriteria();

	DataWrapper getPageByCriteria(int pageNum, int pageSize, Criteria criteria);

	DataWrapper getall();

	DataWrapper getPage(int pageNum, int pagesize);
}
