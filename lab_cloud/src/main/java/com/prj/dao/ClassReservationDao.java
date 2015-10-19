package com.prj.dao;

import java.util.Date;
import java.util.List;

import com.prj.entity.ClassReservation;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Teacher;
import com.prj.util.DataWrapper;

public interface ClassReservationDao {
	Integer addClassReservation(ClassReservation cr);
	
	List<ClassReservation> getAllClassReservation();
	
	DataWrapper<List<ClassReservation>> getClassReservationByTeacherByPageAS(int pagenumber, int pagesize, Teacher teacher, ApprovalStatus as);

	ClassReservation deleteClassReservationById(int id);
	
	ClassReservation findClassReservationById(int id);
	
	//ClassReservation updateClassReservation(ClassReservation cr);
	//ClassReservation findClassReservationById(Integer id);
	
	ClassReservation updateClassReservation(ClassReservation cr);

	DataWrapper<List<ClassReservation>> getClassReservationByPageDur(
			ApprovalStatus approvalStatus, Integer pageSize, Integer pageNumber, String startDate, String endDate);

	DataWrapper<List<ClassReservation>> getPendingClassReservationByPageDur(
			Integer pageSize, Integer pageNumber, String startDate, String endDate);

	ClassReservation getClassReservationsByDateSlot(Date date, SlotNo slot);

	List<ClassReservation> getAllClassReservationByLabTeacher(Integer labTeaId);
}
