package com.prj.dao.impl;

import com.prj.dao.MachineDao;
import com.prj.entity.Machine;
import com.prj.util.DataWrapper;
import org.springframework.stereotype.Repository;

@Repository
public class MachineDaoImpl extends BaseDaoHibernateImpl<Machine, Long> implements MachineDao {

	public MachineDaoImpl() {
		super(Machine.class);
	}

  public DataWrapper getByMac(String mac) {
    return new DataWrapper(super.findBy("mac", mac));
  }
}
