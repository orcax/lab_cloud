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
import com.prj.entity.Account;
import com.prj.entity.Experiment;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.ErrorCodeEnum;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration("/proj-test.xml") 
@Transactional() 
public class ExperimentControllerTest {
//	@Autowired
//	AccountController accountCtrl;
//	@Autowired
//	ExperimentController ctrl;
//	
//	private Account admin;
//	private String adminToken;
//	private DataWrapper<Experiment> postData;
//	private DataWrapper<Experiment> ret;
//	
//	private Experiment experiment;
//	
//	@Before
//	public void initialize() {
//		this.admin = login("1435000", "admin").getData();
//		this.adminToken = admin.getLoginToken();
//		this.postData = new DataWrapper<Experiment>(adminToken);
//	}
//
//	@Test
//	public void testAddExperiment() {
//		experiment = getTestExperiment1();
//		postData.setData(experiment);
//		ret = ctrl.add(postData);
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
////		experiment = ret.getData();
//		ret = ctrl.add(postData);
//		assertEquals(ErrorCodeEnum.Experiment_Exist, ret.getErrorCode());
//	}
//	
//	@Test
//	public void testDeleteExperiment() {
//		testAddExperiment();
//		ret = ctrl.deleteExperiment( experiment.getId());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());
//		ret = ctrl.deleteExperiment(1);
//		assertEquals(ErrorCodeEnum.Experiment_Not_Exist, ret.getErrorCode());
//		
//	}
//	
//	@Test
//	public void testGetList() {
//		testAddExperiment();
//		experiment = getTestExperiment2();
//		postData.setData(experiment);
//		ctrl.add(postData);
//		
//		DataWrapper<List<Experiment>> listRet = new DataWrapper<List<Experiment>>(); 
//		listRet = ctrl.getExperimentList(postData);
//		assertEquals(listRet.getData().size(), 2);
//		assertEquals(ErrorCodeEnum.No_Error, listRet.getErrorCode());
//	}
//	
//	@Test
//	public void testGetExperiment() {
//		testAddExperiment();
//		ret = ctrl.getExperiment(postData, experiment.getId());
//		assertEquals(ErrorCodeEnum.No_Error,ret.getErrorCode());
//		assertEquals("5341000-1", ret.getData().getExperimentNumber());
//	}
//	
//	@Test
//	public void testUpdateExperiment() {
//		testAddExperiment();
//		int experimentId = experiment.getId();
//		experiment = getTestExperiment1();
//		experiment.setId(experimentId);
//		experiment.setExperimentNumber("5341000-3");
//		postData.setData(experiment);
//		ret = ctrl.updateExperiment(postData, experiment.getId());
//		assertEquals(ErrorCodeEnum.No_Error, ret.getErrorCode());;
//		ret = ctrl.getExperiment(postData, experimentId);
//		assertEquals("5341000-3", ret.getData().getExperimentNumber());
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
//	private Experiment getTestExperiment1() {
//		Experiment experiment = new Experiment();
//		experiment.setExperimentName("C Experiment 1");
//		experiment.setExperimentNumber("5341000-1");
//		experiment.setMaximumStudent(40);
//		experiment.setMinimumStudent(10);
//		return experiment;
//	}
//	
//	private Experiment getTestExperiment2() {
//		Experiment experiment = new Experiment();
//		experiment.setExperimentName("C Experiment 2");
//		experiment.setExperimentNumber("5341000-2");
//		experiment.setMaximumStudent(45);
//		experiment.setMinimumStudent(15);
//		return experiment;
//	}
}
