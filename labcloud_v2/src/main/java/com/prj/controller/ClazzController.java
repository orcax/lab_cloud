package com.prj.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.entity.Clazz;
import com.prj.util.DataWrapper;
import com.prj.util.RequiredRole;

@RestController
public class ClazzController extends BaseController {

	/**
	 * POST /classes?course={courseId}&teacher={teaId}
	 * <p>
	 * BODY Clazz clazz profile
	 * 
	 * @param courseId
	 * @param teaId
	 * @param clazz
	 * @return
	 */
	@RequestMapping(value = "/clazz", method = RequestMethod.POST)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper add(@RequestParam("course") long courseId,
			@RequestParam("teacher") long teaId,
			@RequestParam("semester") long semesterId, @RequestBody Clazz clazz) {
		return cls.create(clazz, teaId, courseId, semesterId);
	}

	/**
	 * GET /classes/{id}
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/clazz/{id}", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper get(@PathVariable long id) {
		return cls.read(id);
	}

	/**
	 * PUT /classes/{id}
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/clazz/{id}", method = RequestMethod.PUT)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper update(@RequestBody Clazz clazz) {
		if (clazz.getTeacher() != null) {
			cls.update(clazz.getId(), "teacher_id", clazz.getTeacher().getId());
		}
		if (clazz.getCourse() != null) {
			cls.update(clazz.getId(), "course_id", clazz.getCourse().getId());
		}
		if (clazz.getSemester() != null) {
			cls.update(clazz.getId(), "semester_id", clazz.getSemester()
					.getId());
		}
		return cls.update(clazz.getId(), clazz);
	}

	/**
	 * DELETE /classes/{id}
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/clazz/{id}", method = RequestMethod.DELETE)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper delete(@PathVariable long id) {
		return cls.delete(id);
	}

	/**
	 * GET /classes
	 * <p>
	 * Get classes of teacher or student.
	 * 
	 * @param stuId
	 * @param teaId
	 * @return
	 */
	@RequestMapping(value = "/clazz", method = RequestMethod.GET)
	@RequiredRole({ Role.STUDENT, Role.ALL_TEACHER, Role.NOR_TEACHER })
	DataWrapper search() {
		Account a = currentAccount();
		if (a.getRole() == Role.STUDENT) {
			return cls.listByStuId(a.getId());
		} else {
			return cls.listByTeaId(a.getId());
		}
	}

	/**
	 * GET /clazzs/page/10/1
	 * 
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	@RequestMapping(value = "/clazz/list/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
	DataWrapper getClazzByPage(@PathVariable int pageSize,
			@PathVariable int pageNumber) {
		return cls.getPage(pageNumber, pageSize);
	}

	/**
	 * GET /clazz/list/all
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clazz/list/all", method = RequestMethod.GET)
	DataWrapper all() {
		return cls.getall();
	}

	/**
	 * GET /clazz/{id}/student/list/page/{pageSize}/{pageNumber}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clazz/{id}/student/list/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
	DataWrapper studentList(@PathVariable long id, @PathVariable int pageSize,
			@PathVariable int pageNumber) {
		return cls.studentListById(id, pageNumber, pageSize);
	}

	/**
	 * GET /clazz/studentExist/{stuId}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clazz/{clazzId}/studentExist/{stuId}", method = RequestMethod.GET)
	DataWrapper studentList(@PathVariable("clazzId") long clazzId,
			@PathVariable long stuId) {
		return cls.isStudentExistInClazz(clazzId, stuId);
	}

	/**
	 * GET /clazz/list/teacher/{teacherId}/
	 */
	@RequestMapping(value = "/clazz/list/semester/{semesterId}/teacher/{teacherId}", method = RequestMethod.GET)
	DataWrapper teacherClazzList(@PathVariable("semesterId") long semesterId,
			@PathVariable("teacherId") long teacherId) {
		return cls.getTeacherClazzList(semesterId, teacherId);
	}
}
