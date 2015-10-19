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

import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.entity.Course;
import com.prj.service.ExperimentService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;

@Controller
@RequestMapping(value = "/Experiment")
public class ExperimentController {

	@Resource(name = "ExperimentServiceImpl")
	ExperimentService vs;
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Experiment> add(@RequestBody DataWrapper<Experiment> experiment) {
		return vs.addExperiment(experiment.getData());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public DataWrapper<Experiment> deleteExperiment(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return vs.deleteExperimentById(id);
	}

	@AccountAccess
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Experiment>> getExperimentList(@RequestBody DataWrapper<?> wrapper) {
		return vs.getAllActiveExperiment();
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Experiment> getExperiment(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return vs.getExperimentById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Experiment> updateExperiment(@RequestBody DataWrapper<Experiment> experiment,  @PathVariable int id) {
		return vs.updateExperiment(id, experiment.getData());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/page/{pageSize}/{pageNumber}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Experiment>> getExperimentList(@RequestBody DataWrapper<?> wrapper, @PathVariable Integer pageSize, @PathVariable Integer pageNumber) {
		return vs.getExperimentByPage(pageNumber, pageSize);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{experimentId}/lab/list", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Lab>> getLabList(@RequestBody DataWrapper<?> wrapper, @PathVariable int experimentId) {
		return vs.getLabList(experimentId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{experimentId}/lab/add/{labId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Experiment> addLab(@RequestBody DataWrapper<?> wrapper, @PathVariable int experimentId, @PathVariable int labId) {
		return vs.addLab(experimentId, labId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{experimentId}/lab/delete/{labId}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Experiment> deleteLab(@RequestBody DataWrapper<?> wrapper, @PathVariable int experimentId, @PathVariable int labId) {
		return vs.deleteLab(experimentId, labId);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{experimentId}/course/list", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Course>> getCourseList(@RequestBody DataWrapper<?> wrapper, @PathVariable int experimentId) {
		return vs.getCourseList(experimentId);
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
