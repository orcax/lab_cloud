package com.prj.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prj.controller.AccountController;
import com.prj.controller.CourseController;
import com.prj.entity.Account;
import com.prj.entity.Course;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.ErrorCodeEnum;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration("/proj-test.xml") 
@Transactional() 
public class CourseControllerTest {
//	@Autowired
//	AccountController accountCtrl;
//	@Autowired
//	CourseController ctrl;
//	
//	private Account admin;
//	private String adminToken;
//	private DataWrapper<Course> postData;
//	private DataWrapper<Course> ret;
//	
//	private Course course;
//	
//	@Before
//	public void initialize() {
//		this.admin = login("1435000", "admin").getData();
//		this.adminToken = admin.getLoginToken();
//		this.postData = new DataWrapper<Course>(adminToken);
//	}
//
//	@Test
//	public void testAddCourse() {
//		course = getTestCourse1();
//		postData.setData(course);
//		ret = ctrl.add(postData);
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
////		course = ret.getData();
//		ret = ctrl.add(postData);
//		assertEquals(ErrorCodeEnum.Course_Exist, ret.getErrorCode());
//	}
//	
////	@Test
////	public void testDeleteCourse() {
////		testAddCourse();
////		ret = ctrl.deleteCourse( course.getId());
////		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
////		ret = ctrl.deleteCourse( 1);
////		assertEquals(ErrorCodeEnum.Course_Not_Exist, ret.getErrorCode());
////		
////	}
//	
//	@Test
//	public void testGetList() {
//		testAddCourse();
//		course = getTestCourse2();
//		postData.setData(course);
//		ctrl.add(postData);
//		
//		DataWrapper<List<Course>> listRet = new DataWrapper<List<Course>>(); 
//		listRet = ctrl.getCourseList(postData);
//		assertEquals(listRet.getData().size(), 2);
//		assertEquals(ErrorCodeEnum.No_Error, listRet.getErrorCode());
//	}
//	
//	@Test
//	public void testGetCourse() {
//		testAddCourse();
//		ret = ctrl.getCourse(postData, course.getId());
//		assertEquals(ErrorCodeEnum.No_Error,ret.getErrorCode());
//		assertEquals("5341000", ret.getData().getCourseNumber());
//	}
//	
//	@Test
//	public void testUpdateCourse() {
//		testAddCourse();
//		int courseId = course.getId();
//		course = getTestCourse1();
//		course.setId(courseId);
//		course.setCourseNumber("5341666");
//		postData.setData(course);
//		ret = ctrl.updateCourse(postData, course.getId());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());;
//		ret = ctrl.getCourse(postData, courseId);
//		assertEquals("5341666", ret.getData().getCourseNumber());
//	}
//	
//
//	
//	private DataWrapper<Account> login(String number, String password) {
//		Account loginAdmin = new Account();
//		loginAdmin.setNumber(number);
//		loginAdmin.setPassword(password);
//		return accountCtrl.login(new DataWrapper<Account>(loginAdmin));
//	}
//	
//	private Course getTestCourse1() {
//		Course course = new Course();
//		course.setCourseName("C");
//		course.setCourseNumber("5341000");
//		course.setDepartment("软件学院");
//		course.setStartYear(DateUtils.getDateFormString("2014-1-1 0:0:0"));
//		return course;
//	}
//	
//	private Course getTestCourse2() {
//		Course course = new Course();
//		course.setCourseName("C++");
//		course.setCourseNumber("5341001");
//		course.setDepartment("软件学院");
//		course.setStartYear(DateUtils.getDateFormString("2014-1-1 0:0:0"));
//		return course;
//	}
}
