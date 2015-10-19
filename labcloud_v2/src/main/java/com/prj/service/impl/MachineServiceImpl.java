package com.prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.dao.LabDao;
import com.prj.dao.MachineDao;
import com.prj.entity.Machine;
import com.prj.service.MachineService;
import com.prj.util.DataWrapper;

@Service
public class MachineServiceImpl extends BaseServiceImpl<Machine, Long> implements MachineService{
	MachineDao md;

  @Autowired
  LabDao ld;

	@Autowired
	public MachineServiceImpl(MachineDao md) {
		super(Machine.class, md);
		this.md = md;
	}

  public DataWrapper getByMac(String mac) {
    return md.getByMac(mac);
  }

  public DataWrapper create(Machine entity) {
    return super.createEntity(entity);
  }

  public DataWrapper updateByMac(String mac, Machine m){
    Machine machine = (Machine)md.getByMac(mac).getData();
    if(m.getLaunchPath()!=null){
      machine.setLaunchPath(m.getLaunchPath());
    }
    if(m.getLab()!=null){
      machine.setLab(ld.read(m.getLab().getId()));
    }
    if (m.getListenDirectories()!=null){
      machine.setListenDirectories(m.getListenDirectories());
    }
    return update(machine.getId(),machine);
  }
}
