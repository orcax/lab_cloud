package com.prj.service;

import java.util.Date;
import java.util.List;

import com.prj.entity.Account;
import com.prj.entity.StudentReservation;
import com.prj.util.DataWrapper;

public interface StudentReservationService extends
		BaseService<StudentReservation, Long> {

	DataWrapper findByLDS(long lab, Date date, long slot);

	DataWrapper cancelStudentReservationApplication(long id, Account account);

	DataWrapper listAllReservationStudents(long id);

	DataWrapper addStudentReservationApplication(long id, Account account);

	DataWrapper handle(long id, long[] labTeacherIds);

	DataWrapper addStuReservation(StudentReservation studentReservation,
			long semesterId, long experimentId, long labId, long slotId,
			long reserverId);

	DataWrapper deleteStuReservation(long id);
	
	DataWrapper cancelStudentReservation(long id) ;

	DataWrapper add(StudentReservation stuResProfile, long lab, Date date,
			int slotId);

	DataWrapper getReservationsBySemAndLab(Long semesterId, long labId);

	DataWrapper getStudentReservationBySemsterAndAccount(long semesterId,
			long accountId);

	DataWrapper getStudentReservationByAccountPage(long semesterId,
			long accountId, int pageSize, int pageNumber);

	DataWrapper getAvailableStudentReservation(long semesterId, long accountId,
			int pageSize, int pageNumber);

	List<StudentReservation> getReservationByDateSpan(long semesterId,
			Date startDate, Date endDate);

	DataWrapper getStudentReservationByTime(long stuId, Date date, int slot,
			long labid);
}
