package com.prj.dao;

import java.util.List;

import com.prj.entity.Class;
import com.prj.entity.Experiment;
import com.prj.entity.StudentRecord;
import com.prj.util.DataWrapper;

public interface StudentRecordDao {
	Integer addStudentRecord(StudentRecord sr);
	
	StudentRecord getStudentRecordByClassStuExp(Integer classId, Integer stuId, Integer ExpId);

	StudentRecord updateStudentRecord(StudentRecord sr);

	DataWrapper<List<StudentRecord>> getStudentRecordsByClassExpInPage(Class clazz,
			Experiment exp, Integer pageSize, Integer pageNumber);

	StudentRecord findStudentRecordById(Integer recordId);

	List<StudentRecord> getRecordByStuClass(Integer stuId, Integer classId);

//	StudentReservation findStudentRecordBySL(Integer studentId, Integer labId,
//			StudentReservation studentResrvation);
}
