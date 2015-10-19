package com.prj.controller;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prj.entity.Class;
import com.prj.entity.ClassReservation;
import com.prj.entity.InfoEntry;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.StudentRecord;
import com.prj.entity.Teacher;
import com.prj.entity.Teacher.Role;
import com.prj.service.TeacherService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;
import com.prj.util.PasswordReset;

@Controller
@RequestMapping(value = "/Teacher")
public class TeacherController {
	@Resource(name = "TeacherServiceImpl")
	TeacherService ts;

	// @RequestMapping(value = "/table", method = RequestMethod.GET)
	// public String IndexView(Model model) {
	// return "Teacher/table";
	// }

	@AccountAccess(checkAccountCharacter = AccountCharacter.ALL_TEACHER)
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Teacher> getTeacher(@RequestBody DataWrapper<?> wrapper) {
		return ts.getTeacherById(wrapper.getAccountId());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ALL_TEACHER)
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Teacher> resetPassword(
			@RequestBody DataWrapper<PasswordReset> wrapper) {
		wrapper.getData().setId(wrapper.getAccountId());
		return ts.reset(wrapper.getData());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Teacher> add(@RequestBody DataWrapper<Teacher> teacher) {
		return ts.addTeacher(teacher.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Teacher> getTeacher(@RequestBody DataWrapper<?> wrapper,
			@PathVariable int id) {
		return ts.getTeacherById(id);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/classes/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Set<Class>> getTeacherClasses(
			@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ts.getTeacherClasses(id);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Teacher>> getTeacherList(
			@RequestBody DataWrapper<?> wrapper) {
		return ts.getAllTeacher();
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Teacher> deleteTeacher(
			@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ts.disableTeacherById(id);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ALL_TEACHER)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Teacher> updateTeacher(
			@RequestBody DataWrapper<Teacher> teacher) {
		return ts.updateTeacher(teacher.getAccountId(), teacher.getData(),
				false);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Teacher> updateTeacher(
			@RequestBody DataWrapper<Teacher> teacher, @PathVariable int id) {
		return ts.updateTeacher(id, teacher.getData(), true);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/role/{role}/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Teacher>> getTeacherList(
			@RequestBody DataWrapper<?> wrapper,
			@PathVariable Role role,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return ts.getTeacherByRolePage(role, pageNumber, pageSize);
	}

	
	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/weekplan/{weekIndex}/{labid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<LabPlan>> getWeekPlanByLab(
			@RequestBody DataWrapper<?> wrapper,
			@PathVariable Integer weekIndex, @PathVariable Integer labid) {
		return ts.getPlanByWeekIndexByLab(wrapper.getToken(), weekIndex, labid);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/weekplan/{weekIndex}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<LabPlan>> getWeekPlan(
			@RequestBody DataWrapper<?> wrapper, @PathVariable Integer weekIndex) {
		return ts.getPlanByWeekIndex(wrapper.getToken(), weekIndex);
	}

	// TODO CHECK
	// @AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	// @RequestMapping(value = "/dayplan/{date}", method = RequestMethod.POST)
	// @ResponseBody
	// public DataWrapper<LabPlan> getDayPlan(@RequestBody DataWrapper<?>
	// wrapper,@PathVariable String date) {
	// return vs.getPlanByDate(wrapper.getToken(),Date.valueOf(date));
	// }

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/reserve/add/{labid}/{expid}/{classid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<ClassReservation> addReservation(
			@RequestBody DataWrapper<ClassReservation> wrapper,
			@PathVariable int labid, @PathVariable int expid,
			@PathVariable int classid) {
		return ts.classReserver(wrapper.getAccountId(), wrapper.getData(), labid,
				expid, classid);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/reserve/status/{approvalStatus}/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<ClassReservation>> getAllReservation(
			@RequestBody DataWrapper<?> wrapper, 
			@PathVariable ApprovalStatus approvalStatus,
			@PathVariable int pageSize,
			@PathVariable int pageNumber) {
		return ts.getClassReservationByPageAS(pageNumber, pageSize,
				wrapper.getAccountId(), approvalStatus);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/reserve/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<ClassReservation> deleteReservation(
			@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ts.deleteClassReservationById(id);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/reserve/profile/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<ClassReservation> getReservation(
			@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ts.getClassReservationById(id);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/reserve/update/{id}/{labid}/{expid}/{classid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<ClassReservation> updateReservation(
			@RequestBody DataWrapper<ClassReservation> wrapper,
			@PathVariable int id, @PathVariable int labid,
			@PathVariable int expid, @PathVariable int classid) {
		return ts.updateClassReservationById(id, wrapper.getData(), labid,
				expid, classid);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/class/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Class>> getAllClass(
			@RequestBody DataWrapper<ClassReservation> wrapper) {
		return ts.getAllClass(wrapper.getAccountId());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/class/all/lab/{labId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Class>> getClassesInLabByTeacher(
			@RequestBody DataWrapper<Void> wrapper, @PathVariable Integer labId) {
		return ts.getClassesInLabByTeacher(wrapper.getAccountId(), labId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/experiment/infoList/class/{classId}/lab/{labId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<InfoEntry>> getExpInfoList(@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable Integer classId, @PathVariable Integer labId) {
		return ts.getExpInfoListByClassLab(classId, labId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/reserve/{reservationId}/labTeacher/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Teacher>> getAvailableTeachers(@RequestBody DataWrapper<Void> wrapper, @PathVariable Integer reservationId) {
		return ts.getAvailableTeachers(reservationId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.LAB_TEACHER)
	@RequestMapping(value = "/labTeacher/classReservation/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<ClassReservation>> getAllClassReservation(@RequestBody DataWrapper<Void> wrapper) {
		return ts.getAllClassReservation(wrapper.getAccountId());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.LAB_TEACHER)
	@RequestMapping(value = "/labTeaacher/classReservation", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<ClassReservation>> getOwnClassReservation(@RequestBody DataWrapper<Void> wrapper) {
		return ts.getAllClassReservation(wrapper.getAccountId());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/record/update/{recordId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<StudentRecord> updateStudentRecord(
			@RequestBody DataWrapper<StudentRecord> wrapper,
			@PathVariable Integer recordId) {
		return ts.updateStudentRecord(recordId, wrapper.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/list/lab/{labId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Teacher>> getTeacherList(
			@RequestBody DataWrapper<?> wrapper, 
			@PathVariable Integer labId) {
		return ts.getTeacherList(labId);
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
