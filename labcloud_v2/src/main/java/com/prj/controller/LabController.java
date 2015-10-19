package com.prj.controller;

import com.prj.entity.Account.Role;
import com.prj.entity.Lab;
import com.prj.util.DataWrapper;
import com.prj.util.RequiredRole;
import org.springframework.web.bind.annotation.*;

@RestController
public class LabController extends BaseController {

	/**
	 * POST /lab
	 * <p>
	 * BODY Lab
	 *
	 * @param labProfile
	 * @return
	 */
	@RequestMapping(value = "/lab", method = RequestMethod.POST)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper add(@RequestBody Lab labProfile) {
		return ls.create(labProfile);
	}

	/**
	 * DELETE /lab/{id}
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/lab/{id}", method = RequestMethod.DELETE)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper delete(@PathVariable long id) {
		return ls.delete(id);
	}

	/**
	 * PUT /lab/{id}
	 * <p>
	 * BODY Lab
	 *
	 * @param id
	 * @param labProfile
	 * @return
	 */
	@RequestMapping(value = "/lab/{id}", method = RequestMethod.PUT)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper updateProfile(@PathVariable long id, @RequestBody Lab labProfile) {
		return ls.update(id, labProfile);
	}

	/**
	 * GET /lab/{id}
	 * <p>
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/lab/{id}", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper get(@PathVariable long id) {
		return ls.read(id);
	}

	/**
	 * GET /lab/info?experiment={expId}
	 *
	 * @param expId
	 * @return
	 */
	@RequestMapping(value = "/lab/info", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper search(@RequestParam("experiment") long expId) {
		return ls.listInfoByExp(expId);
	}

	/**
	 * GET /lab/list/all
	 */
	@RequestMapping(value = "/lab/list/all", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper all() {
		return ls.getall();
	}
}
