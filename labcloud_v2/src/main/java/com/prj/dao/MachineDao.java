package com.prj.dao;

import com.prj.entity.Machine;
import com.prj.util.DataWrapper;

public interface MachineDao extends BaseDao<Machine, Long> {

	DataWrapper getByMac(String mac);

}
