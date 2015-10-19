package com.prj.service;

import com.prj.entity.Machine;
import com.prj.util.DataWrapper;

public interface MachineService extends BaseService<Machine, Long> {

//  DataWrapper create(Machine entity, long labId);

	DataWrapper getByMac(String mac);

  public DataWrapper updateByMac(String mac, Machine m);
}
