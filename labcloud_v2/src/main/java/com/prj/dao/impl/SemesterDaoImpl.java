package com.prj.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prj.dao.SemesterDao;
import com.prj.entity.Semester;
import com.prj.entity.Slot;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;

@Repository
public class SemesterDaoImpl extends BaseDaoHibernateImpl<Semester, Long>
		implements SemesterDao {

	public SemesterDaoImpl() {
		super(Semester.class);
	}

	public DataWrapper getCurrentSemester() {
		DataWrapper data = new DataWrapper();

		List<?> list = listBy("status", Semester.Status.CURRENT);
		if (list.size() > 1) { // ����Ҫ�ж��Ƿ�Ψһ
			data.setErrorCode(ErrorCode.DUPLICATION);
		} else if (list.size() == 0) {
			data.setErrorCode(ErrorCode.NOT_FOUND);
		} else {
			data.setErrorCode(ErrorCode.NO_ERROR);
			data.setData(list.get(0));
		}
		return data;
	}

	public DataWrapper getSlots() {
		return new DataWrapper(getSession().createCriteria(Slot.class).list());
	}
}
