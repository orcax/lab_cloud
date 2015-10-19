package com.prj.service;

import com.prj.util.DataWrapper;

import java.io.Serializable;


public interface BaseService <T, PK extends Serializable>{

	DataWrapper create(T entity);
	DataWrapper read(PK id);
	DataWrapper update(PK id, T o);
	DataWrapper delete(PK id);

	//DataWrapper findBy(String propertyName, Object value);
	DataWrapper update(PK id, String propertyName, Object value);

	DataWrapper findByNumber(String number);

	DataWrapper getall();

	DataWrapper getPage(int pageNum,int pageSize);

}
