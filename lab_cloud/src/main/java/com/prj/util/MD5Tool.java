package com.prj.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
   
public class MD5Tool {
	
	public static String MD5_RANDOM = "tc_intelligence";
	
	public static String GetMd5(String plainText) {
		plainText = plainText.trim();
		plainText = plainText + MD5_RANDOM;
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		System.out.println(MD5Tool.GetMd5("1234"));
	}
}
