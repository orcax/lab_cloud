package com.prj.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prj.entity.Account;
import com.prj.entity.Administrator;
import com.prj.entity.ClassReservation;
import com.prj.entity.InfoEntry;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.Reservation.Type;
import com.prj.entity.SlotReservation;
import com.prj.entity.TeacherLabReserve;
import com.prj.service.AdministratorService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AdministratorApproval;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;
import com.prj.util.PasswordReset;

@Controller
@RequestMapping(value = "/Administrator")
public class AdministratorController {
	@Resource(name = "AdministratorServiceImpl")
	AdministratorService as;
//	@Resource(name = "TeacherServiceImpl")
//	TeacherService ts;
	
//	@RequestMapping(value = "/table", method = RequestMethod.GET)
//	public String IndexView(Model model) {
//		return "Administrator/table";
//	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Administrator> getAdministrator(@RequestBody DataWrapper<?> wrapper) {
		return as.getAdministratorById(wrapper.getAccountId());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Administrator> resetPassword(@RequestBody DataWrapper<PasswordReset> wrapper) {
		wrapper.getData().setId(wrapper.getAccountId());
		return as.reset(wrapper.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reset/{accountCharacter}/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Account> resetPasswordById(@RequestBody DataWrapper<PasswordReset> wrapper, 
			@PathVariable AccountCharacter accountCharacter, @PathVariable Integer id) {
		wrapper.getData().setId(id);
		wrapper.getData().setAccountCharacter(accountCharacter);
		return as.resetById(wrapper.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Administrator> add(@RequestBody DataWrapper<Administrator> administrator) {
		return as.addAdministrator(administrator.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Administrator> getAdministrator(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return as.getAdministratorById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Administrator>> getAdministratorList(@RequestBody DataWrapper<?> wrapper) {
		return as.getAllAdministrator();
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public DataWrapper<Administrator> deleteAdministrator(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return as.disableAdministratorById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Administrator> updateAdministrator(@RequestBody DataWrapper<Administrator> administrator) {
		return as.updateAdministrator(administrator.getAccountId(), administrator.getData(), false);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Administrator> updateAdministrator(@RequestBody DataWrapper<Administrator> administrator,  @PathVariable int id) {
		return as.updateAdministrator(id, administrator.getData(), true);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Administrator>> getAdministratorList(
			@RequestBody DataWrapper<?> wrapper, 
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return as.getAdministratorbyPage(pageNumber, pageSize);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reserve/class/approve/{reservationId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<TeacherLabReserve> approveClassReservation(
			@RequestBody DataWrapper<AdministratorApproval> wrapper, 
			@PathVariable Integer reservationId) {
		return as.approveReservation(reservationId, Type.Class, wrapper.getData());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reserve/slot/approve/{reservationId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<TeacherLabReserve> approveSlotReservation(
			@RequestBody DataWrapper<AdministratorApproval> wrapper, 
			@PathVariable Type type,
			@PathVariable Integer reservationId) {
		return as.approveReservation(reservationId, Type.Slot, wrapper.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reserve/class/profile/{reservationId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<ClassReservation> getClassReservation(@RequestBody DataWrapper<Void> wrapper, @PathVariable Integer reservationId) {
		return as.getClassReservation(reservationId);
	}

//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/semester/profile/current", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<Semester> getCurSemester(@RequestBody DataWrapper<Void> wrapper) {
//		return vs.getCurSemester();
//	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reserve/page/{approvalStatus}/{pageSize}/{pageNumber}/start/{startDate}/end/{endDate}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<ClassReservation>> getAllReservationPage(
			@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable ApprovalStatus approvalStatus,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber,
			@PathVariable String startDate, @PathVariable String endDate) {
		return as.getClassReservationByPageDur(approvalStatus, pageSize, pageNumber, startDate, endDate);
//		DataWrapper<List<Reservation>> ret = new DataWrapper<List<Reservation>>();
//		DataWrapper<List<ClassReservation>> classRet = as.getClassReservationByPageDur(approvalStatus, pageSize, pageNumber, startDate, endDate);
//		List<Reservation> l = new ArrayList<Reservation>();
//		if (classRet.getErrorCode() != ErrorCodeEnum.No_Error)
//			return ret.setErrorCode(classRet.getErrorCode());
//		l.addAll(classRet.getData());

		/* Won't return slot reservation currently */
//		if (approvalStatus == ApprovalStatus.APPROVED || approvalStatus == ApprovalStatus.ALL) {
//			DataWrapper<List<SlotReservation>> slotRet = as.getSlotReservationByPageDur(pageSize, pageNumber, startDate, endDate);
//			if (slotRet.getErrorCode() != ErrorCodeEnum.No_Error)
//				return ret.setErrorCode(slotRet.getErrorCode());
//			l.addAll(slotRet.getData());
//		}
//		return ret.setData(l);
	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/reserve/page/pending/{pageSize}/{pageNumber}/start/{startDate}/end/{endDate}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<List<ClassReservation>> getPendingClassReservationPage(
//			@RequestBody DataWrapper<Void> wrapper, 
//			@PathVariable Integer pageSize, @PathVariable Integer pageNumber,
//			@PathVariable String startDate, @PathVariable String endDate) {
//		return as.getPendingClassReservationByPageDur(pageSize, pageNumber, startDate, endDate);
//	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reserve/add/{labid}/{expid}/{classid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<ClassReservation> addReservation(
			@RequestBody DataWrapper<ClassReservation> wrapper,
			@PathVariable int labid, @PathVariable int expid,
			@PathVariable int classid) {
//		ClassReservation cReservation =  wrapper.getData();
//		cReservation.setDate(DateUtils.getDateFormString(date));
		return as.classReserver(wrapper.getData(), labid,
				expid, classid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/weekplan/{weekIndex}/{labid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<LabPlan>> getWeekPlanByLab(
			@RequestBody DataWrapper<?> wrapper,
			@PathVariable Integer weekIndex, @PathVariable Integer labid) {
		return as.getPlanByWeekIndexByLab(weekIndex, labid);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/weekplan/{weekIndex}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<LabPlan>> getWeekPlan(
			@RequestBody DataWrapper<?> wrapper, @PathVariable Integer weekIndex) {
		return as.getPlanByWeekIndex(weekIndex);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/experiment/infoList/class/{classId}/lab/{labId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<InfoEntry>> getExpInfoList(@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable Integer classId, @PathVariable Integer labId) {
		return as.getExpInfoListByClassLab(classId, labId);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/slotReserve/add/lab/{labId}/experiment/{expId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<SlotReservation> addSlotReservation(
			@RequestBody DataWrapper<SlotReservation> wrapper, 
			@PathVariable int labId, @PathVariable int expId) {
		return as.addSlotReservation(wrapper.getAccountId(), labId, expId, wrapper.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/slotReserve/all/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<SlotReservation>> getSlotReservationPage(
			@RequestBody DataWrapper<Void> wrapper,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return as.getSlotReservationPage(pageSize, pageNumber);
	}
	
	@ExceptionHandler(AuthorityException.class)
	@ResponseBody
	public DataWrapper<Void> handleAuthorityException(AuthorityException ex) {
		System.out.println(ex.getErrorCode().getLabel());
		DataWrapper<Void> ret = new DataWrapper<Void>();
		ret.setErrorCode(ex.getErrorCode());
		return ret;
	}
}
