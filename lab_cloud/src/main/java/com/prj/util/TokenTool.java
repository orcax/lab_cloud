package com.prj.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import com.prj.entity.Account;


public class TokenTool {

	private final static String TOKEN_RANDOM = "lab_cloud_good";
	private final static String TOKEN_DATE_FORMAT = "yyyy/MM/dd";
	private final static String TOKEN_SPLIT = ":";

	private static Logger logger = Logger.getLogger(TokenTool.class);

	public static String generateToken(Account account) {
		String token = MD5Tool.GetMd5(account.getNumber() + ":"
				+ account.getPassword() + ":" + TOKEN_RANDOM);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
		DateFormat f = new SimpleDateFormat(TOKEN_DATE_FORMAT);
		token = token + TOKEN_SPLIT + f.format(c.getTime());
		return token;
	}

	public static boolean isTokenValid(String token) {
		if (token == null) return false;
		try {
			String date_time = token.split(TOKEN_SPLIT)[1];
			DateFormat f = new SimpleDateFormat(TOKEN_DATE_FORMAT);
			Date d = f.parse(date_time);
			Calendar c = Calendar.getInstance();
			if (d.before(c.getTime())) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
	}
	
//	public static Integer getId(String token) {
//		return Integer.valueOf(token.split(TOKEN_SPLIT)[2]);
//	}
	
	public static String randomToken() {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
		int len = base.length();
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < 20; i++) {     
	        int number = random.nextInt(len);     
	        sb.append(base.charAt(number));     
	    }     
	    return MD5Tool.GetMd5(sb.toString()); 
	}
}
