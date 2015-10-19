package com.prj.dao;

import com.prj.entity.Lab;
import com.prj.util.DataWrapper;

public interface LabDao extends BaseDao<Lab, Long> {

	DataWrapper listInfoByExp(long expId);
	
	

}
