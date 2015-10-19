package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.LabDao;
import com.prj.entity.Lab;
import com.prj.util.DataWrapper;

@Repository
public class LabDaoImpl extends BaseDaoHibernateImpl<Lab, Long> implements LabDao {

	public LabDaoImpl() {
		super(Lab.class);
	}

	public DataWrapper listInfoByExp(long expId) {
		return new DataWrapper(getSession().createSQLQuery(
				"select l.id, l.name from lab l "
				+ "inner join lab_experiment le on le.lab_id = l.id "
				+ "where le.experiment_id = ?")
				.setLong(0, expId)
				.list());
	}

}
