package com.prj.daoImpl;

import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.AdministratorLabReserveDao;
import com.prj.entity.TeacherLabReserve;

@Service("AdministratorLabReserveDaoImpl")
public class AdministratorLabReserveDaoImpl extends AbstractHibernateDao<TeacherLabReserve, Integer> implements AdministratorLabReserveDao{
	protected AdministratorLabReserveDaoImpl(){
		super(TeacherLabReserve.class);
	}

	@Override
	public Integer addAdministratorLabReserve(TeacherLabReserve alr) {
		return add(alr);
	}

	@Override
	public TeacherLabReserve findAdministratorLabReserveById(Integer id) {
		return findById(id);
	}

	@Override
	public TeacherLabReserve updateAdministratorLabReserve(TeacherLabReserve alr) {
		return saveOrUpdate(alr);
	}
}
