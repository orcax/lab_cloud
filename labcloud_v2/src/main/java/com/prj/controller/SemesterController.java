package com.prj.controller;

import com.prj.entity.Account.Role;
import com.prj.entity.Semester;
import com.prj.util.DataWrapper;
import com.prj.util.RequiredRole;
import org.springframework.web.bind.annotation.*;

@RestController
public class SemesterController extends BaseController {

	/**
	 * POST /semester
	 * <p>
	 * BODY Semester
	 *
	 * @param semesterProfile
	 * @return
	 */
	@RequestMapping(value = "/semester", method = RequestMethod.POST)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper add(@RequestBody Semester semesterProfile) {
		return ss.create(semesterProfile);
	}

	/**
	 * DELETE /semester/{id}
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/semester/{id}", method = RequestMethod.DELETE)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper delete(@PathVariable long id) {
		return ss.delete(id);
	}

	/**
	 * GET /semester/list/all
	 *
	 * @return
	 */
	@RequestMapping(value = "/semester/list/all", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper search() {
		return ss.getall();
	}

	/**
	 * PUT /semester/current
	 * <p>
	 * BODY Semester
	 *
	 * @param semesterId
	 * @return
	 */
	@RequestMapping(value = "/semester/current", method = RequestMethod.PUT)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper setCurrent(@RequestParam("semester") long semesterId) {
		return ss.setCurrent(semesterId);
	}

	/**
	 * GET /semester/current
	 *
	 * @return
	 */
	@RequestMapping(value = "/semester/current", method = RequestMethod.GET)
	DataWrapper getCurrent() {
		return ss.getCurrent();
	}

	/**
	 * GET /clazzs/page/10/1
	 *
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	@RequestMapping(value = "/semester/list/page/{pageSize}/{pageNumber}", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper getByPage(@PathVariable int pageSize,
			@PathVariable int pageNumber) {
		return ss.getPage(pageNumber, pageSize);
	}

	/**
	 * GET /semester/slots
	 *
	 * @return
	 */
	@RequestMapping(value = "/semester/slots", method = RequestMethod.GET)
	DataWrapper getAllSlot() {
		return ss.getSlots();
	}
}
