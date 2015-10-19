package com.prj.dao;

import java.util.List;
import java.util.Set;

import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.StudentReservation;
import com.prj.util.DataWrapper;

public interface StudentReservationDao {
	Set<StudentReservation> getStudentReservationByLabPlan(LabPlan lp);
	
	List<StudentReservation> getAllStudentReservation();

	Integer addStudentReservation(StudentReservation sr);

	DataWrapper<List<StudentReservation>> getStudentReservationPage(Integer stuId,
			ApprovalStatus as, Integer pageSize, Integer pageNumber);

	List<StudentReservation> getStudentReservationBySLE(Integer studentId, Integer labId,
			Integer expId);

	DataWrapper<List<StudentReservation>> getClassReservationByPageDur(
			ApprovalStatus approvaltatus, Integer pageSize, Integer pageNumber,
			String startDate, String endDate);
}
