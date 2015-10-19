package com.prj.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Slot;
import com.prj.service.SlotService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.DataWrapper;

@Controller
@RequestMapping(value = "/Slot")
public class SlotController {
	@Resource(name = "SlotServiceImpl")
	SlotService sls;
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Slot> setSlotTime(
			@RequestBody DataWrapper<Slot> wrapper) {
		return sls.setSlotTime(wrapper.getAccountId(), wrapper.getData());
	}
	
	// @AccountAccess(checkAccountCharacter = AccountCharacter.ANY)
	@RequestMapping(value = "/profile/{slotNo}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Slot> getSlot(
			@PathVariable SlotNo slotNo) {
		return sls.getSlot(slotNo);
	}
}
