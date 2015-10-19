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
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.OpenStatus;
import com.prj.entity.LabPlan.Weekday;
import com.prj.entity.Reservation.SlotNo;
import com.prj.service.LabService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;

@Controller
@RequestMapping(value = "/Lab")
public class LabController {

	@Resource(name = "LabServiceImpl")
	LabService ls;
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Lab> addLab(@RequestBody DataWrapper<Lab> lab) {
		return ls.addLab(lab.getAccountId(), lab.getData());
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public DataWrapper<Lab> deleteLab(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ls.deleteLabById(id, wrapper.getAccountId());
	}

	@AccountAccess
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Lab>> getActiveLabList(@RequestBody DataWrapper<?> wrapper) {
		return ls.getAllActiveLab();
	}

	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Lab> getLab(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
		return ls.getLabById(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Lab> updateLab(@RequestBody DataWrapper<Lab> lab,  @PathVariable int id) {
		return ls.updateLab(id, lab.getData(), lab.getAccountId());
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{labid}/experiment/add/{experimentid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Lab> addExperiment(@RequestBody DataWrapper<?> wrapper,  @PathVariable int labid, @PathVariable int experimentid) {
		return ls.addExperiment(labid, experimentid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{labid}/experiment/delete/{experimentid}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Lab> deleteExperiment(@RequestBody DataWrapper<?> wrapper,  @PathVariable int labid, @PathVariable int experimentid) {
		return ls.deleteExperiment(labid, experimentid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{labid}/experiment/list", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Experiment>> getExperimentList(@RequestBody DataWrapper<?> wrapper, @PathVariable int labid) {
		return ls.getExperimentList(labid);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{labId}/date/{date}/slot/{slot}/status/{openStatus}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<LabPlan> setLabSlotStatus(
			@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable int labId, @PathVariable String date, 
			@PathVariable SlotNo slot, @PathVariable OpenStatus openStatus) {
		return ls.setLabSlotStatus(labId, DateUtils.getDateFromString(date), slot, openStatus);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{id}/slot/{slot}/close", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Void> closeLabSlot(
			@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable int id, @PathVariable SlotNo slot) {
		return ls.closeLabSlot(id, slot);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/{id}/slot/{slot}/weekday/{weekday}/close", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Void> closeLabSlot(
			@RequestBody DataWrapper<Void> wrapper, 
			@PathVariable int id, @PathVariable SlotNo slot,
			@PathVariable Weekday weekday) {
		return ls.closeLabSlot(id, slot, weekday);
	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/{labId}/date/{date}/slot/{slot}/type/{slotType}/max/{max}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<LabPlan> setLabSlotType(
//			@RequestBody DataWrapper<Void> wrapper, 
//			@PathVariable int labId, @PathVariable String date, 
//			@PathVariable int max,
//			@PathVariable Slot slot, @PathVariable SlotType slotType) {
//		return vs.setLabSlotType(labId, DateUtils.getDateFormString(date), slot, slotType, max);
//	}
	
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
