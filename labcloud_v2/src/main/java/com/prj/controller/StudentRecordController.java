package com.prj.controller;

import com.prj.entity.Account;
import com.prj.entity.StudentRecord;
import org.springframework.web.bind.annotation.*;

import com.prj.entity.Account.Role;
import com.prj.util.DataWrapper;
import com.prj.util.RequiredRole;

@RestController
public class StudentRecordController extends BaseController {

	/**
	 * GET /studentRecords?student={stuId}&class={clazzId}&experiment={expId}
	 *
	 * @param stuId
	 * @param clazzId
	 * @param expId
	 * @return
	 */
	@RequestMapping(value = "/studentRecords", method = RequestMethod.GET)
	@RequiredRole({Role.STUDENT ,Role.ALL_TEACHER, Role.NOR_TEACHER})
	DataWrapper search(	@RequestParam("student") long stuId,
						@RequestParam("clazz") long clazzId,
						@RequestParam("experiment") long expId	) {
		return srecs.findByCES(clazzId, expId, stuId);
	}

	/**
	 * GET /studentRecords/info?class={clazzId}&experiment={expId}
	 *
	 * @param stuId
	 * @param clazzId
	 * @param expId
	 * @return
	 */
	@RequestMapping(value = "/studentRecords/info", method = RequestMethod.GET)
	@RequiredRole({Role.ALL_TEACHER, Role.NOR_TEACHER})
	DataWrapper search(	@RequestParam("class") long clazzId,
						@RequestParam("experiment") long expId	) {
		return srecs.listInfoByCE(clazzId, expId);
	}

  /**
   * GET /class/{classId}/record/list
   */
  @RequestMapping(value = "/clazz/{clazzId}/record/list", method = RequestMethod.GET)
  @RequiredRole(Role.STUDENT)
  DataWrapper getRecordByStuClass(@PathVariable long clazzId){
    Account a = currentAccount();
    return srecs.getRecordByStuClass(clazzId, a.getId());
  }

  /**
   * GET /file/list/{recordId}
   *
   */
  @RequestMapping(value = "/studentRecords/file/list/{recordId}", method = RequestMethod.GET)
  @RequiredRole({Role.STUDENT,Role.ALL_TEACHER,Role.LAB_TEACHER,Role.NOR_TEACHER})
  DataWrapper getFileListByRecord(@PathVariable long recordId){
    return srecs.getFileListByRecord(recordId);
  }

  /**
   * GET /studentRecords/clazz/{clazzId}/experiment/{experimentId}
   *
   */
  @RequestMapping(value = "/studentRecords/clazz/{clazzId}/experiment/{experimentId}/{pageNumber}/{pageSize}", method = RequestMethod.GET)
  @RequiredRole({Role.STUDENT,Role.ALL_TEACHER,Role.LAB_TEACHER,Role.NOR_TEACHER})
  DataWrapper getRecordsByClazzExp(@PathVariable long clazzId, @PathVariable long experimentId, @PathVariable int pageNumber, @PathVariable int  pageSize){
    return srecs.getRecordsByClazzExp(clazzId, experimentId,pageSize,pageNumber);
  }

  /**
   * PUT /studentRecords/{id}
   *
   */
  @RequestMapping(value = "/studentRecords/{id}", method = RequestMethod.PUT)
  @RequiredRole({Role.ALL_TEACHER,Role.LAB_TEACHER,Role.NOR_TEACHER})
  DataWrapper updateStudentRecord(@PathVariable long id,@RequestBody StudentRecord studentRecord){
    return srecs.updateStudentRecord(id,studentRecord);
  }
  
}
