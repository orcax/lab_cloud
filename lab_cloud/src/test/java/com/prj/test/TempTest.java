package com.prj.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prj.entity.Account;
import com.prj.util.DateUtils;
import com.prj.util.MD5Tool;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration("/proj-test.xml") 
@Transactional() 
public class TempTest {
//	@Resource(name = "AccountDaoImpl")
//	private AccountDao dao;
	
	@Test
	public static void testAccount() {
//		Account2 a = dao.getAccount2();
//		System.out.println(a.getAccountName());
//		String s = "-----------------------------633119332551420391206797616 Content-Disposition: form-data; name=\"token\" asdasd -----------------------------633119332551420391206797616 Content-Disposition: form-data; name=\"file\"; filename=\"1.txt\" Content-Type: text/plain 123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123 -----------------------------633119332551420391206797616--";
		Account a = new Account();
		a.setNumber("1435000");
		a.setPassword("admin");
		System.out.println(MD5Tool.GetMd5("222222"));
		// 0146a44e5d3be5d95fba2c22c3a3c9ae
	}
	
	public static void test1() {
		System.out.println(MD5Tool.GetMd5("drowssap"));
		System.out.println("asd"+null);
		Calendar cal = Calendar.getInstance();
		cal.set(2015, 0, 31, 0, 0, 0);
		cal.roll(Calendar.DAY_OF_YEAR, true);
		System.out.println(cal.getTime());
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtils.getDateFromLong(DateUtils.getCurrentDateLong()));
	}

	private static void test3() {
		try {
			System.out.println(URLDecoder.decode("proc%E5%89%AF%E6%9C%AC.sql", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void test2() {
		Calendar cal = Calendar.getInstance();
		System.out.println(String.format("%tF", cal.getTime()));
	}
}
