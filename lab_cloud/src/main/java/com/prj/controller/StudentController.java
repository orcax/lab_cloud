package com.prj.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.prj.entity.Class;
import com.prj.entity.FileRecord;
import com.prj.entity.FileRecord.Type;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.Reservation;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.SlotReservation;
import com.prj.entity.Student;
import com.prj.entity.StudentRecord;
import com.prj.entity.StudentReservation;
import com.prj.service.FileUploadService;
import com.prj.service.StudentService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.PasswordReset;

@Controller
@RequestMapping(value = "/Student")
public class StudentController {
	@Resource(name = "StudentServiceImpl")
	StudentService ss;

	@Resource(name = "FileUploadServiceImpl")
	FileUploadService fs;
	
//	@RequestMapping(value = "/table", method = RequestMethod.GET)
//	public String IndexView(Model model) {
//		return "Student/table";
//	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Student> getStudent(@RequestBody DataWrapper<?> wrapper) {
		return ss.getStudentById(wrapper.getAccountId());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Student> resetPassword(@RequestBody DataWrapper<PasswordReset> wrapper) {
		wrapper.getData().setId(wrapper.getAccountId());
		return ss.reset(wrapper.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Student> add(@RequestBody DataWrapper<Student> student) {
		return ss.addStudent(student.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Student> getStudent(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ss.getStudentById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Student>> getStudentList(@RequestBody DataWrapper<?> wrapper) {
		return ss.getAllStudent();
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public DataWrapper<Student> deleteStudent(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ss.disableStudentById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Student> updateStudent(@RequestBody DataWrapper<Student> student) {
		return ss.updateStudent(student.getAccountId(), student.getData(), false);
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Student> updateStudent(@RequestBody DataWrapper<Student> student,  @PathVariable int id) {
		return ss.updateStudent(id, student.getData(), true);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Student>> getStudentList(@RequestBody DataWrapper<?> wrapper, @PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return ss.getStudentbyPage(pageNumber, pageSize);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/weekplan/{weekIndex}/{labid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<LabPlan>> getWeekPlanByLab(@RequestBody DataWrapper<?> wrapper,@PathVariable Integer weekIndex,@PathVariable Integer labid)	{
		return ss.getPlanByWeekIndexByLab(wrapper.getToken(),weekIndex,labid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/weekplan/{weekIndex}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<LabPlan>> getWeekPlan(@RequestBody DataWrapper<?> wrapper,@PathVariable Integer weekIndex)	{
		return ss.getPlanByWeekIndex(wrapper.getToken(),weekIndex);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/reservation/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<? extends Reservation>> allreserve(@RequestBody DataWrapper<?> wrapper)	{
		return ss.getallReservation(wrapper.getAccountId());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/class/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Class>> getAllClass(@RequestBody DataWrapper<?> wrapper){
		return ss.getAllClass(wrapper.getAccountId());
	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
//	@RequestMapping(value = "/upload/expData/classReservation/{reservationId}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<String> uploadExpData(DataWrapper<MultipartFile> wrapper, @PathVariable Integer reservationId, HttpServletRequest request) {
//		String rootPath = request.getSession().getServletContext().getRealPath("/");
//		return fs.saveExpData(wrapper.getData(), reservationId, wrapper.getAccountId(), rootPath);
//	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/upload/{fileType}/classReservation/{reservationId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> upload(
			DataWrapper<MultipartFile> wrapper, 
			@PathVariable Type fileType,
			@PathVariable Integer reservationId, 
			HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return fs.saveFileRecord(wrapper.getData(), fileType, reservationId, wrapper.getAccountId(), rootPath);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/upload/{fileType}/slotReservation/{reservationId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> uploadBySlotRes(
			DataWrapper<MultipartFile> wrapper, 
			@PathVariable Type fileType,
			@PathVariable Integer reservationId, 
			HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return fs.saveFileRecordBySlotRes(wrapper.getData(), fileType, reservationId, wrapper.getAccountId(), rootPath);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/upload/{fileType}/class/{classId}/experiment/{expId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> upload(
			DataWrapper<MultipartFile> wrapper, 
			@PathVariable Type fileType,
			@PathVariable Integer classId, @PathVariable Integer expId, 
			HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return fs.saveFileRecord(wrapper.getData(), fileType, classId, expId, wrapper.getAccountId(), rootPath);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/delete/{fileType}/classReservation/{reservationId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> delete(
			@RequestBody DataWrapper<String> wrapper, 
			@PathVariable Type fileType,
			@PathVariable Integer reservationId, 
			HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return fs.deleteFileRecord(fileType, wrapper.getData(), reservationId, wrapper.getAccountId(), rootPath);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/delete/{fileType}/slotReservation/{reservationId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> deleteBySlotRes(
			@RequestBody DataWrapper<String> wrapper, 
			@PathVariable Type fileType,
			@PathVariable Integer reservationId, 
			HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return fs.deleteFileRecordBySlotRes(fileType, wrapper.getData(), reservationId, wrapper.getAccountId(), rootPath);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/delete/{fileType}/class/{classId}/experiment/{expId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> delete(
			@RequestBody DataWrapper<String> wrapper, 
			@PathVariable Type fileType,
			@PathVariable Integer classId, @PathVariable Integer expId, 
			HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return fs.deleteFileRecord(fileType, wrapper.getData(), classId, expId, wrapper.getAccountId(), rootPath);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/report/generate/studentRecord/{srId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> generateFile(
			@RequestBody DataWrapper<String> wrapper, 
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer srId) {
//		if (wrapper.getAccountCharacter() == AccountCharacter.STUDENT 
//				&& wrapper.getAccountId() != srId)
//			return new DataWrapper<String>(ErrorCodeEnum.Access_Denied);
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return fs.genReportTemplate(srId, rootPath);
		
	}
//	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
//	@RequestMapping(value = "/upload/photo/classReservation/{reservationId}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<String> uploadPhoto(DataWrapper<MultipartFile> wrapper, 
//			@PathVariable Integer reservationId, HttpServletRequest request) {
//		String rootPath = request.getSession().getServletContext().getRealPath("/");
//		return fs.savePhoto(wrapper.getData(), reservationId, wrapper.getAccountId(), rootPath);
//	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
//	@RequestMapping(value = "/upload/report/class/{classId}/experiment/{expId}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<String> uploadReport(DataWrapper<MultipartFile> wrapper, 
//			@PathVariable Integer classId, @PathVariable Integer expId, HttpServletRequest request) {
//		String rootPath = request.getSession().getServletContext().getRealPath("/");
//		return fs.saveReport(wrapper.getData(), classId, expId, wrapper.getAccountId(), rootPath);
//	}
	
	//TODO CHECK
//	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
//	@RequestMapping(value = "/dayplan/{date}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<LabPlan> getDayPlan(@RequestBody DataWrapper<?> wrapper,@PathVariable String date)	{
//		return vs.getPlanByDate(wrapper.getToken(),Date.valueOf(date));
//	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/reservation/{date}/{slot}/{labid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<String> getDayPlan(@RequestBody DataWrapper<?> wrapper,@PathVariable String date,@PathVariable SlotNo slot,@PathVariable int labid)	{
		return ss.getReservationByTime(wrapper.getAccountId(),DateUtils.getDateFromString(date),slot,labid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/reservation/maximum/{date}/{slot}/{labid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Integer> getMaximumStudent(@RequestBody DataWrapper<?> wrapper,@PathVariable String date,@PathVariable SlotNo slot,@PathVariable int labid)	{
		return ss.getMaxStuOfExperiments(DateUtils.getDateFromString(date), slot, labid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/class/{classId}/record/list", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<StudentRecord>> getRecordByStuClass(
			@RequestBody DataWrapper<Void> wrapper,
			@PathVariable Integer classId)	{
		return ss.getRecordByStuClass(wrapper.getAccountId(), classId);
	}
	
	@AccountAccess
	@RequestMapping(value = "/studentRecord/{srId}/fileRecord/list", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<FileRecord>> getFileRecords(
			@RequestBody DataWrapper<Void> wrapper,
			@PathVariable Integer srId) {
		return ss.getFileRecordList(srId);
	}
	
//	@AccountAccess
//	@RequestMapping(value = "/reserve/add/date/{date}/lab/{labId}/experiment/{expId}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<StudentReservation> addStudentReservation(
//			@RequestBody DataWrapper<StudentReservation> wrapper,
//			@PathVariable Integer labId, @PathVariable Integer expId) {
//		return ss.addStudentReservation(wrapper.getAccountId(), labId, expId,
//				wrapper.getData());
//	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/slotReserve/add/slotReservation/{slotResId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<SlotReservation> applySlotReservation(
			@RequestBody DataWrapper<StudentReservation> wrapper,
			@PathVariable Integer slotResId) {
		return ss.applySlotReservation(wrapper.getAccountId(), slotResId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/reserve/status/{approvalStatus}/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<StudentReservation>> getStudentReservationPage(
			@RequestBody DataWrapper<Void> wrapper,
			@PathVariable Integer stuId, @PathVariable ApprovalStatus as,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return ss.getStudentReservationPage(stuId, as, pageSize, pageNumber);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/slotReserve/available/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<SlotReservation>> getAvailableSlotReservationPage(
			@RequestBody DataWrapper<Void> wrapper,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return ss.getAvailableSlotReservationPage(wrapper.getAccountId(), pageSize, pageNumber);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/slotReserve/{slotResId}/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Student>> getStudentPage(
			@RequestBody DataWrapper<Void> wrapper,
			@PathVariable Integer slotResId,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return ss.getStudentPageBySlotRes(slotResId, pageSize, pageNumber);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.STUDENT)
	@RequestMapping(value = "/slotReserve/applied/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<SlotReservation>> getAppliedSlotReservationPage(
			@RequestBody DataWrapper<Void> wrapper,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return ss.getSlotReservationPageByStu(wrapper.getAccountId(), pageSize, pageNumber);
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
