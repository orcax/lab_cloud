package com.prj.service;

import java.util.List;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Account;
import com.prj.entity.Administrator;
import com.prj.entity.ClassReservation;
import com.prj.entity.InfoEntry;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.Reservation.Type;
import com.prj.entity.SlotReservation;
import com.prj.entity.StudentReservation;
import com.prj.entity.TeacherLabReserve;
import com.prj.util.AdministratorApproval;
import com.prj.util.DataWrapper;
import com.prj.util.Page;
import com.prj.util.PasswordReset;

public interface AdministratorService {
//	DataWrapper<Administrator> login(Administrator administrator);

//	void logout(Integer id);

	DataWrapper<Administrator> addAdministrator(Administrator administrator);

	public DataWrapper<Administrator> disableAdministratorById(Integer id);

	DataWrapper<List<Administrator>> getAllAdministrator();

	DataWrapper<Administrator> getAdministratorById(int id);

	DataWrapper<Administrator> updateAdministrator(Integer id, Administrator entity, boolean isAdmin);

	DataWrapper<Administrator> reset(PasswordReset data);

	DataWrapper<List<Administrator>> getAdministratorbyPage(int pagenumber, int pagesize);

	Page<Administrator> searchAdministrator(int pagenumber, int pagesize, String name);

	Page<Administrator> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);

	DataWrapper<ClassReservation> getClassReservation(Integer reservationId);

//	DataWrapper<List<Administrator>> getAvailableAdministrators(
//			Integer reservationId);

	DataWrapper<List<ClassReservation>> getClassReservationByPageDur(
			ApprovalStatus approvaltatus, Integer pageSize, Integer pageNumber, String startDate, String endDate);

	
//	DataWrapper<List<ClassReservation>> getPendingClassReservationByPageDur(
//			Integer pageSize, Integer pageNumber, String startDate, String endDate);

	DataWrapper<Account> resetById(PasswordReset data);

	DataWrapper<ClassReservation> classReserver(ClassReservation cr, int labid, int expid, int classid);

//	DataWrapper<Semester> getCurSemester();
	DataWrapper<List<LabPlan>> getPlanByWeekIndex(int index);
	
	DataWrapper<List<LabPlan>> getPlanByWeekIndexByLab(int index, int labid);
	
	DataWrapper<List<InfoEntry>> getExpInfoListByClassLab(Integer courseId,
			Integer labId);

	@Deprecated
	DataWrapper<List<StudentReservation>> getStudentReservationByPageDur(
			ApprovalStatus approvalStatus, Integer pageSize,
			Integer pageNumber, String startDate, String endDate);

	DataWrapper<SlotReservation> addSlotReservation(Integer accountId, int labId,
			int expId, SlotReservation slotReservation);

	DataWrapper<List<SlotReservation>> getSlotReservationPage(Integer pageSize,
			Integer pageNumber);

	DataWrapper<TeacherLabReserve> approveReservation(
			Integer reservationId, Type resType,
			AdministratorApproval administratorApproval);

	DataWrapper<List<SlotReservation>> getSlotReservationByPageDur(
			Integer pageSize, Integer pageNumber, String startDate,
			String endDate);

//	DataWrapper<Slot> setSlotTime(Integer adminId, Slot slot);
//
//	DataWrapper<Slot> getSlot(SlotNo slotNo);
}
