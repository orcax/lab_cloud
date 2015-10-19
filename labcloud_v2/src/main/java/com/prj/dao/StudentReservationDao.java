package com.prj.dao;

import java.util.Date;
import java.util.List;

import com.prj.entity.Reservation;
import com.prj.entity.StudentReservation;
import com.prj.util.DataWrapper;

public interface StudentReservationDao extends
		BaseDao<StudentReservation, Long> {

	DataWrapper addStudentReservation(StudentReservation studentReservation,
			long semesterId, long experimentId, long labId, long reserverId,
			long slotId);

	List<Long> getTeachers(long reservationId);

	List<Long> getStudents(long reservationId);

	DataWrapper getClazzesBySemesterAndLabId(long semesterId, long labId);

	List<Reservation> getStudentReservationByAccIdAndSemeId(long accountId,
			long semesterId);

  List<StudentReservation> getStudentReservationByTime(Date date, int slot, long labid);
}
