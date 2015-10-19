package com.prj.service;

import java.util.Date;
import java.util.List;

import com.prj.entity.Class;
import com.prj.entity.FileRecord;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.SlotReservation;
import com.prj.entity.Student;
import com.prj.entity.StudentRecord;
import com.prj.entity.StudentReservation;
import com.prj.util.DataWrapper;
import com.prj.util.PasswordReset;

public interface StudentService {
	DataWrapper<Student> addStudent(Student student);

	public DataWrapper<Student> disableStudentById(Integer id);

	DataWrapper<List<Student>> getAllStudent();

	DataWrapper<Student> getStudentById(int id);

	DataWrapper<Student> updateStudent(Integer id, Student entity, boolean isAdmin);

	DataWrapper<Student> reset(PasswordReset data);

	DataWrapper<List<Student>> getStudentbyPage(int pagenumber, int pagesize);

	DataWrapper<List<Student>> addStudents(List<Student> list, Integer classId);
	
	DataWrapper<List<LabPlan>> getPlanByWeekIndexByLab(String token, int index, int labid);
	
//	DataWrapper<LabPlan> getPlanByDate(String token, Date date);

	DataWrapper<List<? extends Reservation>> getallReservation(int accountid);
	
	DataWrapper<List<Class>> getAllClass(int accountid);
	
	DataWrapper<List<LabPlan>> getPlanByWeekIndex(String token, int index);
	
	DataWrapper<String> getReservationByTime(int id, Date date, SlotNo slot, int labid);
	
	DataWrapper<Integer> getMaxStuOfExperiments(Date date, SlotNo slot, int labid);

	DataWrapper<List<StudentRecord>> getRecordByStuClass(Integer accountId,
			Integer classId);

	DataWrapper<List<FileRecord>> getFileRecordList(Integer srId);

	DataWrapper<StudentReservation> addStudentReservation(Integer studentId, Integer labId, Integer expId, StudentReservation studentReservation);

	DataWrapper<List<StudentReservation>> getStudentReservationPage(
			Integer stuId, ApprovalStatus as, Integer pageSize,
			Integer pageNumber);

	DataWrapper<List<SlotReservation>> getAvailableSlotReservationPage(
			Integer stuId, Integer pageSize, Integer pageNumber);

	DataWrapper<SlotReservation> applySlotReservation(Integer accountId, Integer slotResId);

	DataWrapper<List<Student>> getStudentPageBySlotRes(Integer slotResId,
			Integer pageSize, Integer pageNumber);

	DataWrapper<List<SlotReservation>> getSlotReservationPageByStu(Integer stuId,
			Integer pageSize, Integer pageNumber);
}
