package com.prj.controller;

import com.prj.entity.Account.Role;
import com.prj.entity.Experiment;
import com.prj.exception.AppException;
import com.prj.util.DataWrapper;
import com.prj.util.RequiredRole;
import org.springframework.web.bind.annotation.*;

/**
 * The Class ExperimentController.
 */
@RestController
public class ExperimentController extends BaseController {

	/**
	 * POST /
	 * <p>
	 * BODY Experiment.
	 *
	 * @param data the data
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment", method = RequestMethod.POST)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper add(@RequestBody Experiment data) {
		return es.create(data);
	}

	/**
	 * DELETE /experiment/{id}.
	 *
	 * @param id the id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/{id}", method = RequestMethod.DELETE)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper delete(@PathVariable long id) {
		return es.delete(id);
	}

	/**
	 * PUT /experiment/{id}
	 * <p>
	 * BODY Experiment.
	 *
	 * @param id the id
	 * @param expProfile the exp profile
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/{id}", method = RequestMethod.PUT)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper updateProfile(@PathVariable long id,
			@RequestBody Experiment expProfile) {
		return es.update(id, expProfile);
	}

	/**
	 * GET /experiment/{id}.
	 *
	 * @param id the id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/{id}", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper get(@PathVariable long id) {
		return es.read(id);
	}

	/**
	 * GET /experiments/info?lab={labId}
	 * <p>
	 * List experiment infos of lab.
	 * <p>
	 * GET /experiments/info?courseId
	 * <p>
	 * List experiment infos of course GET /experiments/info?clazzId
	 * <p>
	 * List experiment infos of clazz
	 *
	 * @param labId the lab id
	 * @param courseId the course id
	 * @param clazzId the clazz id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiments/info", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper searchInfo(
			@RequestParam(value = "lab", required = false) Long labId,
			@RequestParam(value = "course", required = false) Long courseId,
			@RequestParam(value = "class", required = false) Long clazzId) {
		if (labId != null && courseId == null && clazzId == null) {
			return es.listInfoByLab(labId);
		} else if (labId == null && courseId != null && clazzId == null) {
			return es.listInfoByCourse(courseId);
		} else if (labId == null && courseId == null && clazzId != null) {
			return es.listInfoByClazz(courseId);
		}
		throw AppException.newInstanceOfWrongParameterException();
	}

	/**
	 * GET /experiments/info?lab={labId}
	 * <p>
	 * List experiments of lab.
	 * <p>
	 * GET /experiments/info?class={clazzId}
	 * <p>
	 * List experiments of clazz
	 *
	 * @param labId the lab id
	 * @param clazzId the clazz id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiments", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper search(
			@RequestParam(value = "lab", required = false) Long labId,
			@RequestParam(value = "class", required = false) Long clazzId) {
		if (clazzId != null && labId == null) {
			return es.listByClazz(clazzId);
		} else if (labId != null && clazzId == null) {
			return es.listByLab(labId);
		}
		throw AppException.newInstanceOfWrongParameterException();
	}

	/**
	 * POST /experiment/{expId}/lab/{labId}.
	 *
	 * @param expId the exp id
	 * @param labId the lab id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/{expId}/lab/{labId}", method = RequestMethod.POST)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper addLab(@PathVariable long expId, @PathVariable long labId) {
		return es.addLab(expId, labId);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * GET /experiment/{expId}/labs get all labs int the experiment.
	 *
	 * @param expId the exp id
	 * @return the all lab
	 */
	@RequestMapping(value = "/experiment/{expId}/labs", method = RequestMethod.GET)
	DataWrapper getAllLab(@PathVariable long expId) {
		// return es.addLab(expId, labId);
		return es.listLabs(expId);
	}

	/**
	 * DELETE /experiment/{expId}/lab/{labId}.
	 *
	 * @param expId the exp id
	 * @param labId the lab id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/{expId}/lab/{labId}", method = RequestMethod.DELETE)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper deleteLab(@PathVariable long expId, @PathVariable long labId) {
		return es.deleteLabExp(expId, labId);
	}

	/**
	 * GET /experiment/list/all.
	 *
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/list/all", method = RequestMethod.GET)
	@RequiredRole(Role.ADMINISTRATOR)
	DataWrapper all() {
		return es.getall();
	}

	/**
	 * GET /experiment/class/{clazzId}.
	 *
	 * @param clazzId the clazz id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/class/{clazzId}", method = RequestMethod.GET)
	@RequiredRole({ Role.ALL_TEACHER, Role.NOR_TEACHER, Role.LAB_TEACHER })
	DataWrapper listByClazz(@PathVariable long clazzId) {
		return es.listByClazz(clazzId);
	}

	/**
	 * GET /experiment/statuslist/{clazzId}.
	 *
	 * @param clazzId the clazz id
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/statuslist/{clazzId}", method = RequestMethod.GET)
	@RequiredRole({ Role.ALL_TEACHER, Role.NOR_TEACHER, Role.LAB_TEACHER })
	DataWrapper statusListByClazz(@PathVariable long clazzId) {
		return es.statusListByClazz(clazzId);
	}

	/**
	 * Student semester experiments.
	 *
	 * @param accountId the account id
	 * @param semester the semester
	 * @return the data wrapper
	 */
	@RequestMapping(value = "/experiment/student/{accountId}/aviable")
	DataWrapper studentSemesterExperiments(@PathVariable int accountId,
			@RequestParam("semester") long semester) {
		return es.getStudentAvailableExperimentOfSermester(accountId, semester);
	}
}
