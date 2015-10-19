package com.prj.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.prj.entity.Class;
import com.prj.entity.ExperimentStatus;
import com.prj.entity.Student;
import com.prj.entity.StudentRecord;
import com.prj.service.ClassService;
import com.prj.service.FileUploadService;
import com.prj.service.StudentService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.CallStatusEnum;
import com.prj.util.DataWrapper;

@Controller
@RequestMapping(value="Class")
public class ClassController {
	
	@Resource(name = "ClassServiceImpl")
	ClassService cs;
	
	@Resource(name = "StudentServiceImpl")
	StudentService ss;
	
	@Resource(name = "FileUploadServiceImpl")
	FileUploadService fs;

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/add/{semesterId}/{courseId}/{teacherId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Class> add(@RequestBody DataWrapper<Class> clazz, @PathVariable int semesterId, @PathVariable int courseId, @PathVariable int teacherId){
		return cs.addClass(clazz.getData(), semesterId, courseId, teacherId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Class> get(@RequestBody DataWrapper<Void> wrapper, @PathVariable Integer id){
		return cs.findClassById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Class>> all(@RequestBody DataWrapper<?> clazz){
		return cs.getAllClass();
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/page/curSemester/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Class>> page(@RequestBody DataWrapper<?> clazz, @PathVariable Integer pageSize, @PathVariable Integer pageNumber){
		return cs.getCurSemesterClassByPage(pageSize, pageNumber);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Class> delete(@RequestBody DataWrapper<?> clazz,@PathVariable int id){
		return cs.deleteClassById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ANY)
	@RequestMapping(value = "/{classid}/student/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Student>> studentPage(@RequestBody DataWrapper<?> wrapper,@PathVariable int classid,@PathVariable int pageSize,@PathVariable int pageNumber){
		return cs.getStudentByClassByPage(classid, pageSize, pageNumber);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Class> update(@RequestBody DataWrapper<Class> clazz, @PathVariable Integer id){
		return cs.updateClassById(id, clazz.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{classid}/update/teacher/{teacherid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Class> updateTeacher(@RequestBody DataWrapper<?> clazz,@PathVariable int classid,@PathVariable int teacherid){
		return cs.setTeacher(classid, teacherid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{classid}/update/course/{courseid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Class> updateCourse(@RequestBody DataWrapper<?> clazz,@PathVariable int classid,@PathVariable int courseid){
		return cs.setCourse(classid, courseid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{classid}/update/semester/{semesterid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Class> updateSemester(@RequestBody DataWrapper<?> clazz,@PathVariable int classid,@PathVariable int semesterid){
		return cs.setSemester(classid, semesterid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{classId}/upload/list", method = RequestMethod.POST) 
	@ResponseBody
	public DataWrapper<List<Student>> uploadStudentList(DataWrapper<MultipartFile> wrapper, @PathVariable Integer classId, HttpServletRequest request) {
		DataWrapper<List<Student>> ret = new DataWrapper<List<Student>>();
		MultipartFile file = wrapper.getData();
		String path = request.getSession().getServletContext().getRealPath("/files/student");
		System.out.println("save");
		DataWrapper<String> fsRet = fs.saveStudentListFile(path, file, wrapper.getAccountId().toString());
		if (fsRet.getCallStatus().equals(CallStatusEnum.FAILED)) {
			ret.setErrorCode(fsRet.getErrorCode());
			return ret;
		}
		System.out.println("load");
		DataWrapper<List<Student>> studentListRet = fs.getStudentsFromFile(fsRet.getData());
		if (fsRet.getCallStatus().equals(CallStatusEnum.FAILED)) {
			ret.setErrorCode(fsRet.getErrorCode());
			return ret;
		}
		System.out.println("add");
		studentListRet = ss.addStudents(studentListRet.getData(), classId);
		if (fsRet.getCallStatus().equals(CallStatusEnum.FAILED)) {
			ret.setErrorCode(fsRet.getErrorCode());
			return ret;
		}
		System.out.println("link");
		return cs.addStudents(studentListRet.getData(), classId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/{classId}/experiment/statusList", method = RequestMethod.POST) 
	@ResponseBody
	public DataWrapper<List<ExperimentStatus>> experimentStatusList(@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable Integer classId) {
		return cs.experimentStatusList(classId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.TEACHER)
	@RequestMapping(value = "/{classId}/experiment/{expId}/record/page/{pageSize}/{pageNumber}", method = RequestMethod.POST) 
	@ResponseBody
	public DataWrapper<List<StudentRecord>> getStudentRecords(@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable Integer classId, @PathVariable Integer expId,
			@PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return cs.getStudentRecordsByClassExpInPage(classId, expId, pageSize, pageNumber);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/lab/{labId}/teacher/{teacherId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Class>> getTeacherList(
			@RequestBody DataWrapper<?> wrapper, 
			@PathVariable Integer labId,
			@PathVariable Integer teacherId) {
		return cs.getClassList(labId, teacherId);
	}
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(AuthorityException.class)
	@ResponseBody
	public DataWrapper handleAuthorityException(AuthorityException ex) {
		System.out.println(ex.getErrorCode().getLabel());
		DataWrapper ret = new DataWrapper();
		ret.setErrorCode(ex.getErrorCode());
		return ret;
	}
}
