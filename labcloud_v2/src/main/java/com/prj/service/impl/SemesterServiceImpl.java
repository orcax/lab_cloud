package com.prj.service.impl;

import com.prj.dao.SemesterDao;
import com.prj.entity.Semester;
import com.prj.service.SemesterService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SemesterServiceImpl extends BaseServiceImpl<Semester, Long> implements SemesterService{
	SemesterDao sd;

	@Autowired
	public SemesterServiceImpl(SemesterDao sd) {
		super(Semester.class, sd);
		this.sd = sd;
	}

	public DataWrapper create(Semester entity) {
		return super.createEntity(entity);
	}

	public DataWrapper setCurrent(long semester) {

		//sd.update(semester,"status","CURRENT");
		Semester s = sd.read(semester);//�����ֱ��дupdate semester set status = 'CURRENT' where id = ?��ȣ����ܲ�𲻴�
		if (s != null) {
			s.setStatus(Semester.Status.CURRENT);
			sd.update(s);
			return DataWrapper.voidSuccessRet;
		}else {
			DataWrapper data = new DataWrapper();
			data.setErrorCode(ErrorCode.NOT_FOUND);
			return data;
		}

	}

	public DataWrapper list() {
		// TODO Auto-generated method stub

		return null;
	}

	public DataWrapper getCurrent() {
		// TODO Auto-generated method stub
		return sd.getCurrentSemester();
	}

	public DataWrapper getall() {
		// TODO Auto-generated method stub
		return sd.getall();
	}

	public DataWrapper getSlots() {
		return sd.getSlots();
	}

}
