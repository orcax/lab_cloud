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
import com.prj.entity.Account;
import com.prj.entity.Administrator;
import com.prj.entity.Student;
import com.prj.entity.Teacher;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.CallStatusEnum;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.PasswordReset;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration("/proj-test.xml") 
@Transactional() 
public class AccountControllerTest {
	@Autowired 
	private AccountController ctrl; 
	
	private Administrator admin;
	private Teacher teacher;
	private Student student;
	
	private DataWrapper<Account>  postData;
	private DataWrapper<Account> ret; 
	
//	private String adminToken;
//	private String teacherToken;
//	private String studentToken;
	
	@Before
	public void initialize() {
		//this.admin = new Account();
//		System.out.println("===========");
	}
//	@Test 
//	public void testGetAcccountById() { 
//		Account a = new Account();
//		a.setAccountNumber("130");
//		a.setAccountPassword("123");
//		DataWrapper<Account> ret = controller.login(new DataWrapper<Account>(a));
//		assertEquals("2ca358ec562e1e2f6cda17390739cb62:2014/12/13", ret.getToken()); 
//	}
	
//	@Test
//	public void testAccountAccessAspect() {
//		testLogin();
//		testLogin();
//		
//		testAddStudent();
//		student = login("1435600", "student").getData();
//		testAddTeacher();
//		teacher = login("1435100", "teacher").getData();
//		postData = new DataWrapper<Account>(student);
//		try {
//			ctrl.add(postData);
//		} catch (AuthorityException ae) {
//			assertEquals(ErrorCodeEnum.Token_Invalid, ae.getErrorCode());
//		}
//		postData.setToken("9e0bc2b483d7953224cc0b9a5afa7754:2014/10/14");
//		try {
//			ctrl.add(postData);
//		} catch (AuthorityException ae) {
//			assertEquals(ErrorCodeEnum.Token_Expired, ae.getErrorCode());
//		}
//		postData.setToken(admin.getLoginToken().toUpperCase());
//		try {
//			ctrl.add(postData);
//		} catch (AuthorityException ae) {
//			assertEquals(ErrorCodeEnum.Token_Invalid, ae.getErrorCode());
//		}
//		
//		postData.setToken(student.getLoginToken());
//		try {
//			ctrl.add(postData);
//		} catch (AuthorityException ae) {
//			assertEquals(ErrorCodeEnum.Access_Denied, ae.getErrorCode());
//		}
//		postData.setToken(teacher.getLoginToken());
//		try {
//			ctrl.add(postData);
//		} catch (AuthorityException ae) {
//			assertEquals(ErrorCodeEnum.Access_Denied, ae.getErrorCode());
//		}
//	}
//
//	@Test
//	public void testLogin() {
//		ret = login("1435000", "admin");
////		System.out.println(ret.getToken());
////		System.out.println(admin.getAccountPassword());
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		admin = ret.getData();
//	}
//
//	@Test 
//	public void testLoginError() {
//		ret = login("1435099", "admin");
//		assertEquals(CallStatusEnum.FAILED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.Account_Not_Exist, ret.getErrorCode());
//		ret = login("1435002", "admin");
//		assertEquals(CallStatusEnum.FAILED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.Account_Not_Active, ret.getErrorCode());
//		ret = login("1435000", "123");
//		assertEquals(CallStatusEnum.FAILED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.Password_Wrong, ret.getErrorCode());
//	}
//	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Test
//	public void testLogout() {
//		testLogin();
//		postData = new DataWrapper(admin.getLoginToken());
//		ctrl.logout(postData);
//		try {
//			ctrl.getAccount(postData);
//		} catch (AuthorityException ae) {
//			assertEquals(ErrorCodeEnum.Token_Invalid, ae.getErrorCode());
//		}
//	}
//
//	@Test
//	public void testAddAdmin() {
//		testLogin();
//		postData = new DataWrapper<Account>();
//		Account admin3 = new Account();
//		admin3.setAccountCharacter(AccountCharacter.ADMINISTRATOR);
//		admin3.setAccountEmail("1435003@tongji.edu.cn");
//		admin3.setAccountName("管理员3");
//		admin3.setAccountNumber("1435003");
//		admin3.setAccountPassword("admin");
//		postData.setData(admin3);
//		postData.setToken(admin.getLoginToken());
//		DataWrapper<Account> ret = ctrl.add(postData);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//	}
//	
//	@Test
//	public void testAddStudent() {
//		testLogin();
//		postData = new DataWrapper<Account>();
//		student = new Student();
//		student.setAccountCharacter(AccountCharacter.STUDENT);
//		student.setEmail("1435600@tongji.edu.cn");
//		student.setName("李三");
//		student.setNumber("1435600");
//		student.setPassword("student");
//		student.setEntranceYearMonth(DateUtils.getDateFormString("2014-9-1 0:0:0"));
//		student.setGrade("2014"); 
//		postData.setData(student);
//		postData.setToken(admin.getLoginToken());
//		ret = ctrl.add(postData);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
////		System.out.println(student.getAccountPassword());
//		student = ret.getData();
//		ret = login("1435600", "student");
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//	}
//	
//	@Test
//	public void testAddTeacher() {
//		testLogin();
//		postData = new DataWrapper<Account>();
//		teacher = new Account();
//		teacher.setAccountCharacter(AccountCharacter.TEACHER);
//		teacher.setAccountEmail("1435100@tongji.edu.cn");
//		teacher.setAccountName("赵老师");
//		teacher.setAccountNumber("1435100");
//		teacher.setAccountPassword("teacher");
//		postData.setData(teacher);
//		postData.setToken(admin.getLoginToken());
//		ret = ctrl.add(postData);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		teacher = ret.getData();
//		ret = login("1435100", "teacher");
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//	}
//	
//	@Test
//	public void testAddError() {
//		testAddStudent();
//		postData = new DataWrapper<Account>(student);
//		postData.setToken(admin.getLoginToken());
//		ret = ctrl.add(postData);
//		assertEquals(CallStatusEnum.FAILED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.Account_Exist, ret.getErrorCode());
//	}
//	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Test
//	public void testDeleteById() {
//		testLogin();
//		ret = login("1435001", "admin");
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		
//		postData = new DataWrapper(admin.getLoginToken());
//		ret = ctrl.deleteAccount(37);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		
//		ret = login("1435001", "admin");
//		assertEquals(CallStatusEnum.FAILED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.Account_Not_Active, ret.getErrorCode());
//		
//		ret = ctrl.deleteAccount(2);
//		assertEquals(CallStatusEnum.FAILED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.Account_Not_Exist, ret.getErrorCode());
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Test
//	public void testGetAccount() {
//		testLogin();
//		DataWrapper postData = new DataWrapper(admin.getLoginToken());
//		ret = ctrl.getAccount(postData);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		assertEquals(AccountCharacter.ADMINISTRATOR, ret.getData().getAccountCharacter());
//		assertEquals("admin@tongji.edu.cn", ret.getData().getAccountEmail());
//		assertEquals("admin", ret.getData().getAccountName());
//		assertEquals("1435000", ret.getData().getAccountNumber());
//		assertEquals("c06839f4d24fa0cf1f72ec06d7c3b4d9", ret.getData().getAccountPassword());
//	}
//	
//	@Test
//	public void testGetAccountById() {
//		testLogin();
//		postData = new DataWrapper<Account>(admin.getLoginToken());
//		ret = ctrl.getAccount(postData, 37);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		assertEquals(ret.getData().getAccountNumber(), "1435001");
//	}
//
//	@Test
//	public void testGetAccountList() {
//		testLogin();
//		postData = new DataWrapper<Account>(admin.getLoginToken());
//		DataWrapper<List<Account>> ret = ctrl.getAccountList(postData);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		assertEquals(ret.getData().size(), 3);
//	}
//
//	@Test
//	public void testUpdateAccountById() {
//		testLogin();
//		postData = new DataWrapper<Account>(admin.getLoginToken());
//		Account a = ctrl.getAccount(postData, 37).getData();
//		a.setAccountName("飘飘悠悠");
//		postData.setData(a);
//		ret = ctrl.updateAccount(postData, 37);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		assertEquals(ret.getData().getAccountName(), "飘飘悠悠");
//	}
//	
//	@Test
//	public void testUpdateAccountCharacterById() {
//		testLogin();
//		postData = new DataWrapper<Account>(admin.getLoginToken());
//		ret = ctrl.getAccount(postData, 37);
//		assertEquals(AccountCharacter.ADMINISTRATOR, ret.getData().getAccountCharacter());
//		Account a = new Account();
//		a.setAccountCharacter(AccountCharacter.STUDENT);
//		postData.setData(a);
//		ctrl.updateAccountCharacter(postData, 37);
//		ret = ctrl.getAccount(postData, 37);
//		assertEquals(AccountCharacter.STUDENT, ret.getData().getAccountCharacter());
//	}
//
//	@Test
//	public void testReset() {
//		testLogin();
//		PasswordReset reset = new PasswordReset();
//		reset.setOldPassword("admin");
//		reset.setNewPassword("root");
//		DataWrapper<PasswordReset> postData = new DataWrapper<PasswordReset>(reset);
//		postData.setToken(admin.getLoginToken());
//		ret = ctrl.resetPassword(postData);
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		
//		try {
//			ctrl.resetPassword(postData);
//		} catch (AuthorityException ae) {
//			assertEquals(ErrorCodeEnum.Token_Invalid, ae.getErrorCode());
//		}
//		
//		ret = login("1435000", "root");
//		assertEquals(CallStatusEnum.SUCCEED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		
//		postData.setToken(ret.getToken());
//		ret = ctrl.resetPassword(postData);
//		assertEquals(CallStatusEnum.FAILED, ret.getCallStatus());
//		assertEquals(ErrorCodeEnum.Password_Wrong, ret.getErrorCode());
//	}
//	
//	private DataWrapper<Account> login(String number, String password) {
//		Account loginAdmin = new Account();
//		loginAdmin.setAccountNumber(number);
//		loginAdmin.setAccountPassword(password);
//		return ctrl.login(new DataWrapper<Account>(loginAdmin));
//	}
}
