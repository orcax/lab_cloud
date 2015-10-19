package com.prj.service.impl;

import com.prj.dao.LabDao;
import com.prj.entity.Lab;
import com.prj.service.LabService;
import com.prj.util.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabServiceImpl extends BaseServiceImpl<Lab, Long> implements LabService{
	LabDao ld;

	@Autowired
	public LabServiceImpl(LabDao ld) {
		super(Lab.class, ld);
		this.ld = ld;
	}

	public DataWrapper create(Lab l) {
		assertUniqueNumber(l.getNumber());
		return super.createEntity(l);
	}

	public DataWrapper listInfoByExp(long expId) {
		return ld.listInfoByExp(expId);
	}

}
