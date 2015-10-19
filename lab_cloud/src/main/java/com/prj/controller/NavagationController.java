package com.prj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NavagationController {
	
	@RequestMapping(value="/files/student/{studentNumber}/{semesterId}/{classId}/{expId}/ExpData/{fileName}", 
			method = RequestMethod.GET)
	public String expDataFilter(
			@PathVariable String studentNumber,
			@PathVariable int semesterId, @PathVariable int classId, @PathVariable int expId,
			@PathVariable String fileName){
		return "/404.html";
	}
	
	@RequestMapping(value="/files/student/{studentNumber}/{semesterId}/{classId}/{expId}/Report/{fileName}", 
			method = RequestMethod.GET)
	public String reportFilter(
			@PathVariable String studentNumber,
			@PathVariable int semesterId, @PathVariable int classId, @PathVariable int expId,
			@PathVariable String fileName){
		return "/404.html";
	}
}
