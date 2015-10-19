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

import com.prj.entity.Semester;
import com.prj.service.SemesterService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;

@Controller
@RequestMapping(value = "/Semester")
public class SemesterController {
	
	@Resource(name = "SemesterServiceImpl")
	SemesterService ss;
	// TODO CHECK 直接使用accountId
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Semester> add(@RequestBody DataWrapper<Semester> s)
	{
		return ss.addSemester(s.getData(), s.getAccountId());
	}
	
	@RequestMapping(value = "/profile/current", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Semester> getCurSemester() {
		return ss.getCurSemester();
	}
	
	@AccountAccess
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<Semester>> getAllSemester(@RequestBody DataWrapper<Void> wrapper) {
		return ss.getAllSemester();
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
	@RequestMapping(value = "/setCurrent/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Semester> setCurSemester(@RequestBody DataWrapper<Semester> s, @PathVariable Integer id)
	{
		return ss.setCurSemester(id);
	}
	
	@AccountAccess(checkAccountCharacter = AccountCharacter.ANY)
	@RequestMapping(value = "/weekDate/{weekindex}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<List<String>> getDateByWeek(@RequestBody DataWrapper<?> wrapper,@PathVariable Integer weekindex)
	{
		return ss.getDateByWeekIndex(weekindex);
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
