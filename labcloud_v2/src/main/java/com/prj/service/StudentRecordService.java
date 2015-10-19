package com.prj.service;

import com.prj.entity.StudentRecord;
import com.prj.util.DataWrapper;

public interface StudentRecordService extends BaseService<StudentRecord, Long> {

	DataWrapper findByCES(long clazz, long experiment, long student);

	DataWrapper listInfoByCE(long clazzId, long expId);

  DataWrapper getRecordByStuClass(long clazzId, long stuId);

  // DataWrapper createStudentRecords(long clazzId, long expId);

  DataWrapper getFileListByRecord(long recordId);

  DataWrapper getRecordsByClazzExp(long clazzId, long expId, int pageSize, int pageNumber);

  DataWrapper updateStudentRecord(long id, StudentRecord studentRecord);

}
