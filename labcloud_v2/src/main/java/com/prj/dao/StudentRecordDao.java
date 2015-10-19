package com.prj.dao;

import com.prj.entity.Clazz;
import com.prj.entity.Experiment;
import com.prj.entity.FileRecord;
import com.prj.entity.StudentRecord;
import com.prj.util.DataWrapper;

import java.util.List;

public interface StudentRecordDao extends BaseDao<StudentRecord, Long> {

   List<StudentRecord> getRecordByStuClass(long stuId, long classId);

  List<FileRecord> getFileListByRecord(long recordId);

  List<StudentRecord> getRecordsByClazzExp(long clazzId, long expId);

  DataWrapper getRecordByClazzExpByPage(Clazz clazz, Experiment experiment, int pageSize, int pageNumber);

}
