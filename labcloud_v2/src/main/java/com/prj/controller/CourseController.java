package com.prj.controller;

import com.prj.entity.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prj.entity.Account.Role;
import com.prj.entity.Course;
import com.prj.util.RequiredRole;
import com.prj.util.DataWrapper;

/**
 * The Class CourseController.
 */
@RestController
public class CourseController extends BaseController {

	/**
	 * POST /course
	 * <p>
	 * BODY Course.
	 *
	 * @param data the data
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course", method = RequestMethod.POST)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper add(@RequestBody Course data) {
		return cs.create(data);
	}

	/**
	 * DELETE /course/{id}.
	 *
	 * @param id the id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course/{id}", method = RequestMethod.DELETE)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper delete(@PathVariable long id) {
		return cs.delete(id);
	}

	/**
	 * PUT /course/{id}
	 * <p>
	 * BODY Course.
	 *
	 * @param id the id
	 * @param data the data
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course/{id}", method = RequestMethod.PUT)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper updateProfile(@PathVariable long id, @RequestBody Course data) {
		return cs.update(id, data);
	}

	/**
	 * GET /course/{id}.
	 *
	 * @param id the id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course/{id}", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper get(@PathVariable long id) {
		return cs.read(id);
	}

	/**
	 * POST /course/{courseId}/experiment/{expId}.
	 *
	 * @param courseId the course id
	 * @param expId the exp id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course/{courseId}/experiment/{expId}", method = RequestMethod.POST)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper addExp(@PathVariable long courseId, @PathVariable long expId) {
		return cs.addExp(courseId, expId);
	}

	/**
	 * DELETE /course/{courseId}/experiment/{expId}.
	 *
	 * @param courseId the course id
	 * @param expId the exp id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course/{courseId}/experiment/{expId}", method = RequestMethod.DELETE)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper delExp(@PathVariable long courseId, @PathVariable long expId) {
		return cs.deleteExp(courseId, expId);
	}


	
	/**
	 * GET /course/{courseId}/experiments_list
	 * TODO.这里需要改
	 *
	 * @param courseId the course id
	 * @return the course exps
	 */
	@RequestMapping(value = "/course/{courseId}/experiments_list", method = RequestMethod.GET)
	DataWrapper getCourseExps(@PathVariable long courseId) {
		return cs.listCourseExperiments(courseId);
	}
	
	/**
	 * GET /course/{courseId}/experiments
	 * TODO.这里需要改,移动到clazz controller 里面
	 *
	 * @param clazzId the clazz id
	 * @return the all exp
	 */
	@RequestMapping(value = "/course/{clazzId}/experiments", method = RequestMethod.GET)
	DataWrapper getallExp(@PathVariable long clazzId) {
		return cs.listExperiments(clazzId, currentAccount().getId());
	}

	/**
	 * GET /course/list/all.
	 *
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course/list/all", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper all() {
		return cs.getall();
	}

	/**
	 * GET /course/list/student.
	 *
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/course/list/student", method = RequestMethod.GET)
	@RequiredRole(Role.STUDENT)
	DataWrapper listByStudent() {
		Account a = currentAccount();
		System.out.println(a.getId());
		return cs.listByStudent(a.getId());
	}

}
