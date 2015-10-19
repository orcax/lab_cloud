package com.prj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.dao.ClazzDao;
import com.prj.dao.ExperimentDao;
import com.prj.dao.StudentRecordDao;
import com.prj.entity.StudentRecord;
import com.prj.service.StudentRecordService;
import com.prj.util.DataWrapper;

@Service
public class StudentRecordServiceImpl extends BaseServiceImpl<StudentRecord, Long> implements StudentRecordService{
	StudentRecordDao srecd;

  @Autowired
  ClazzDao cd;
  @Autowired
  ExperimentDao ed;

	@Autowired
	public StudentRecordServiceImpl(StudentRecordDao srecd) {
		super(StudentRecord.class, srecd);
		this.srecd = srecd;
	}

	public DataWrapper create(StudentRecord entity) {
		return super.createEntity(entity);
	}

	public DataWrapper findByCES(long clazz, long experiment, long student) {
    return new DataWrapper(srecd.createCriteria()
      .add(Restrictions.and(
        Restrictions.eq("clazz.id", clazz),
        Restrictions.eq("experiment.id", experiment),
        Restrictions.eq("student.id", student)))
      .uniqueResult());
	}

	public DataWrapper listInfoByCE(long clazzId, long expId) {

		return null;
	}

  public DataWrapper getRecordByStuClass(long clazzId, long stuId) {
    return new DataWrapper(srecd.getRecordByStuClass(stuId, clazzId));
  }

  public DataWrapper getFileListByRecord(long recordId) {
    return new DataWrapper(srecd.getFileListByRecord(recordId));
  }

  public DataWrapper getRecordsByClazzExp(long clazzId, long expId, int pageSize, int pageNumber) {
//    List<Criterion> conditions = new ArrayList<Criterion>();
//
//    conditions.add(Restrictions.eq("clazz", cd.read(clazzId)));
//    conditions.add(Restrictions.eq("experiment", ed.read(expId)));
//    return srecd.getPageByConditions(pageNumber,pageSize,conditions);
    return srecd.getRecordByClazzExpByPage(cd.read(clazzId), ed.read(expId), pageSize, pageNumber);
    //return new DataWrapper(srecd.getRecordsByClazzExp(clazzId, expId));
  }

  public DataWrapper updateStudentRecord(long id, StudentRecord studentRecord) {
    StudentRecord sr = srecd.read(id);
    sr.setStatus(StudentRecord.Status.COMPLETED);
    sr.setExperimentRecord(studentRecord.getExperimentRecord());
    sr.setExperimentComment(studentRecord.getExperimentComment());
    srecd.update(sr);
    return new DataWrapper(sr);
  }


}
