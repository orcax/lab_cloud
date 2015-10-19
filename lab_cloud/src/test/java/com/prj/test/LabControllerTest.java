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
import com.prj.controller.ExperimentController;
import com.prj.controller.LabController;
import com.prj.entity.Account;
import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.util.AccountCharacter;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration("/proj-test.xml") 
@Transactional() 
public class LabControllerTest {
//	@Autowired
//	AccountController accountCtrl;
//	@Autowired
//	LabController ctrl;	
//	@Autowired
//	ExperimentController expCtrl;
//	
//	private Account admin;
//	private String adminToken;
//	private DataWrapper<Lab> postData;
//	private DataWrapper<Lab> ret;
//	
//	private Lab lab;
//	
//	@Before
//	public void initialize() {
//		this.admin = login("1435000", "admin", AccountCharacter.ADMINISTRATOR).getData();
//		this.adminToken = admin.getLoginToken();
//		this.postData = new DataWrapper<Lab>(adminToken);
//		this.postData.setAccountCharacter(AccountCharacter.ADMINISTRATOR);
//		this.postData.setAccountId(admin.getId());
//	}
//
//	@Test
//	public void testAddLab() {
//		lab = new Lab();
//		lab.setCapacity(40);
//		lab.setLabNumber("328");
//		lab.setLabName("room328");
//		lab.setStatus(Lab.Status.OPEN);
//		postData.setData(lab);
//		ret = ctrl.addLab(postData);
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
////		lab = ret.getData();
//		ret = ctrl.addLab(postData);
//		assertEquals(ErrorCodeEnum.Lab_Exist, ret.getErrorCode());
//	}
//	
//	@Test
//	public void testDeleteLab() {
//		testAddLab();
////		DataWrapper<Void> wrapper = new DataWrapper<Void>();
////		postData.setAccountId(admin.getId());
//		ret = ctrl.deleteLab(postData, lab.getId());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		ret = ctrl.deleteLab(postData, 1);
//		assertEquals(ErrorCodeEnum.Lab_Not_Exist, ret.getErrorCode());
//		
//	}
//	
//	@Test
//	public void testGetList() {
//		testAddLab();
//		lab = getTestLab2();
//		postData.setData(lab);
//		ctrl.addLab(postData);
//		
//		DataWrapper<List<Lab>> listRet = new DataWrapper<List<Lab>>(); 
//		listRet = ctrl.getActiveLabList(postData);
//		assertEquals(listRet.getData().size(), 2);
//		assertEquals(ErrorCodeEnum.No_Error, listRet.getErrorCode());
//	}
//	
//	@Test
//	public void testGetLab() {
//		testAddLab();
//		ret = ctrl.getLab(postData, lab.getId());
//		assertEquals(ErrorCodeEnum.No_Error,ret.getErrorCode());
//		assertEquals("328", ret.getData().getLabNumber());
//	}
//	
//	@Test
//	public void testUpdateLab() {
//		testAddLab();
//		int labId = lab.getId();
//		lab = getTestLab1();
//		lab.setId(labId);
//		lab.setLabNumber("666");
//		lab.setLabName("room666");
//		postData.setData(lab);
//		ret = ctrl.updateLab(postData, lab.getId());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());;
//		ret = ctrl.getLab(postData, labId);
//		assertEquals("666", ret.getData().getLabNumber());
//	}
//	
//	@Test
//	public void testDeleteExperiment() {
//		testAddLab();
//		int labId = lab.getId();
//		Experiment exper = new Experiment();
//		exper.setExperimentName("力学实验");
////		ctrl.addExperiment(postData, labId, experimentid);
//	}
//
//	
//	private DataWrapper<Account> login(String number, String password, AccountCharacter accountCharacter) {
//		Account loginAdmin = new Account();
//		loginAdmin.setNumber(number);
//		loginAdmin.setPassword(password);
//		DataWrapper<Account> wrapper = new DataWrapper<Account>(loginAdmin);
//		wrapper.setAccountCharacter(accountCharacter);
//		return accountCtrl.login(wrapper);
//	}
//	
//	private Lab getTestLab1() {
//		Lab lab = new Lab();
//		lab.setCapacity(40);
//		lab.setLabNumber("328");
//		lab.setLabName("room328");
//		lab.setStatus(Lab.Status.OPEN);
//		return lab;
//	}
//	
//	private Lab getTestLab2() {
//		Lab lab = new Lab();
//		lab.setCapacity(30);
//		lab.setLabNumber("330");
//		lab.setLabName("room330");
//		lab.setStatus(Lab.Status.CLOSED);
//		return lab;
//	}
}
