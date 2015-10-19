package com.prj.service.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.prj.dao.BaseDao;
import com.prj.exception.AppException;
import com.prj.service.BaseService;
import com.prj.service.FileService;
import com.prj.util.CopyRequired;
import com.prj.util.DataWrapper;

@Transactional
public abstract class BaseServiceImpl<T, PK extends Serializable> implements
		BaseService<T, PK> {
	/**
	 * for convince of dependency configuration move to inherited service
	 * finally
	 */
	@Autowired
	FileService fs;

	private BaseDao<T, PK> dao;
	private Class<T> type;

	public BaseServiceImpl(Class<T> type, BaseDao<T, PK> dao) {
		this.type = type;
		this.dao = dao;
	}

	public DataWrapper findByNumber(String number) {
		return findBy("number", number);
	}

	@SuppressWarnings("unchecked")
	protected DataWrapper createEntity(T input) {
		T target;
		try {
			target = (T) input.getClass().newInstance();
			merge(input, target, MergeType.CREATE);
			dao.create(target);
			return new DataWrapper(target);
		} catch (Exception e) {
			e.printStackTrace();
			throw AppException
					.newInstanceOfInternalException("File to create entity.");
		}

	}

	public DataWrapper read(PK id) {
		T entity = dao.read(id);
		if (entity == null) {
			throw AppException.newInstanceOfNotFoundException(type
					.getSimpleName());
		}
		return new DataWrapper(entity);
	}

	public DataWrapper update(PK id, T inputData) {
		T entity = dao.read(id);
		if (entity == null) {
			throw AppException.newInstanceOfNotFoundException(type
					.getSimpleName());
		} else {
			merge(inputData, entity, MergeType.UPDATE);
			dao.update(entity);
		}
		return DataWrapper.voidSuccessRet;
	}

	public DataWrapper delete(PK id) {
		T entity = dao.read(id);
		if (entity == null) {
			throw AppException.newInstanceOfNotFoundException(type
					.getSimpleName());
		} else {
			dao.delete(entity);
		}
		return DataWrapper.voidSuccessRet;
	}

	protected DataWrapper findBy(String propertyName, Object value) {
		T entity = dao.findBy(propertyName, value);
		if (entity == null) {
			// Don't throw exception, return voidSuccessRet
			// throw
			// AppException.newInstanceOfNotFoundException(type.getSimpleName());
		}
		return new DataWrapper(entity);
	}

	public DataWrapper update(PK id, String propertyName, Object value) {
		T entity = dao.read(id);
		if (entity == null) {
			throw AppException.newInstanceOfNotFoundException(type
					.getSimpleName());
		}
//		if (value instanceof Long) {
//			dao.update(id, propertyName,(Long) value);
//		}else {
//			dao.update(id, propertyName, value.toString());
//		}
		dao.update(id,propertyName,value);
		return DataWrapper.voidSuccessRet;
	}

	protected boolean isUnique(String propertyName, Object value) {
		return dao.isUnique(propertyName, value);
	}

	protected void assertUniqueNumber(String number) {
		if (!isUnique("number", number))
			throw AppException.newInstanceOfDuplicationException("number");
	}

	protected boolean isUniqueNumber(String number) {
		return isUnique("number", number);
	}

	public enum MergeType {
		CREATE, UPDATE
	}

	protected void merge(T input, T target, MergeType mergeType) {
		try {
			Method[] ms = type.getMethods();
			for (Method m : ms) {
				Annotation[] as = m.getAnnotations();
				for (Annotation a : as) {
					if (a instanceof CopyRequired) {
						CopyRequired cp = (CopyRequired) a;
						if (mergeType == MergeType.CREATE && cp.create()
								|| mergeType == MergeType.UPDATE && cp.update()) {
							Method setter = null, getter = null;
							String methodName = m.getName();
							if (methodName.matches("get\\w+")) {
								getter = m;
								setter = type.getMethod(
										"set" + methodName.substring(3),
										getter.getReturnType());
							} else if (methodName.matches("is\\w+")) {
								getter = m;
								setter = type.getMethod(
										"set" + methodName.substring(2),
										getter.getReturnType());
							} else {
								break;
							}
							Object field = getter.invoke(input);
							if (field != null || cp.copyWhenNull())
								setter.invoke(target, field);
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DataWrapper getall() {
		return dao.getall();

	}

	public DataWrapper getPage(int pageNum, int pageSize) {
		return dao.getPage(pageNum, pageSize);
	}
}
