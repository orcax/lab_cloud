package com.prj.dao;

import com.prj.entity.TeacherLabReserve;

public interface AdministratorLabReserveDao {
	Integer addAdministratorLabReserve(TeacherLabReserve alr);

	TeacherLabReserve findAdministratorLabReserveById(Integer id);
	
	TeacherLabReserve updateAdministratorLabReserve(TeacherLabReserve alr);
}
